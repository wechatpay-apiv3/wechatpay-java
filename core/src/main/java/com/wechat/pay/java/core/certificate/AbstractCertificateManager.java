package com.wechat.pay.java.core.certificate;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.wechat.pay.java.core.certificate.model.Data;
import com.wechat.pay.java.core.certificate.model.DownloadCertificateResponse;
import com.wechat.pay.java.core.certificate.model.EncryptCertificate;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.HttpResponse;
import com.wechat.pay.java.core.http.MediaType;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCertificateManager implements CertificateManager {
  private static final String THREAD_NAME_SUFFIX = "_scheduled_update_thread"; // 更新线程名称
  private static final Logger log = LoggerFactory.getLogger(AbstractCertificateManager.class);
  protected static final int UPDATE_INTERVAL_MINUTE = 1440; // 定时更新时间，1天
  protected SafeSingleScheduleExecutor executor; // 安全的单例定时执行器
  protected final Map<String, Map<String, X509Certificate>> certificateMap =
      new HashMap<>(); // 商户号对应的平台证书

  protected final Map<String, HttpClient> httpClientMap = new HashMap<>(); // 商户号对应的httpClient
  protected final Map<String, AeadCipher> aeadCipherMap = new HashMap<>(); // 商户号对应的aeadCipher
  private String requestUrl; // 下载证书的请求url
  private Function<String, X509Certificate> certificateGenerator; // 将证书从String转为X509Certificate的方法
  private Function<List<X509Certificate>, Verifier> verifierGenerator; // 生成verifier的方法

  /**
   * 将商户加入平台证书管理器
   *
   * @param merchantId 商户号
   * @param httpClient 下载平台证书的httpClient
   * @param aeadCipher 解密平台证书的aeadCipher
   * @param requestUrl 下载平台证书的请求url
   * @param certificateGenerator 将证书从String转为X509Certificate的方法
   * @param verifierGenerator 生成verifier的方法
   */
  protected void putMerchant(
      String merchantId,
      HttpClient httpClient,
      AeadCipher aeadCipher,
      String requestUrl,
      Function<String, X509Certificate> certificateGenerator,
      Function<List<X509Certificate>, Verifier> verifierGenerator,
      String algorithmType) {
    requireNonNull(merchantId);
    requireNonNull(httpClient);
    requireNonNull(aeadCipher);
    if (certificateMap.get(merchantId) != null) {
      return;
    }
    this.requestUrl = requestUrl;
    this.certificateGenerator = certificateGenerator;
    this.verifierGenerator = verifierGenerator;
    synchronized (this) {
      this.aeadCipherMap.put(merchantId, aeadCipher);
      this.httpClientMap.put(merchantId, httpClient);
      downloadAndUpdateMerchant(merchantId);
      if (executor == null) {
        beginScheduleUpdate(algorithmType);
      }
    }
  }

  /**
   * 开启自动更新
   *
   * @param algorithmType 算法类型
   */
  protected void beginScheduleUpdate(String algorithmType) {
    if (executor != null) {
      return;
    }
    executor = new SafeSingleScheduleExecutor();
    Runnable runnable =
        () -> {
          String realThreadName = algorithmType + THREAD_NAME_SUFFIX;
          Thread.currentThread().setName(realThreadName);
          log.info("{}:Begin update Certificates.Date:{}", realThreadName, Instant.now());
          for (Map.Entry<String, HttpClient> entry : httpClientMap.entrySet()) {
            String merchantId = entry.getKey();
            downloadAndUpdateMerchant(merchantId);
          }
          log.info("{}:Finish update Certificates.Date:{}", realThreadName, Instant.now());
        };
    executor.scheduleAtFixedRate(runnable, 0, UPDATE_INTERVAL_MINUTE, TimeUnit.MINUTES);
  }

  @Override
  public void stopScheduleUpdate() {
    if (executor == null) {
      return;
    }
    try {
      executor.shutdownNow();
    } catch (Exception e) {
      log.error("Executor shutdown now failed", e);
    }
  }

  /**
   * 下载并更新商户信息
   *
   * @param merchantId 商户号
   */
  protected void downloadAndUpdateMerchant(String merchantId) {
    HttpClient httpClient = httpClientMap.get(merchantId);
    AeadCipher aeadCipher = aeadCipherMap.get(merchantId);
    HttpRequest request =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(requestUrl)
            .addHeader(Constant.ACCEPT, " */*")
            .addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue())
            .build();
    HttpResponse<DownloadCertificateResponse> httpResponse =
        httpClient.execute(request, DownloadCertificateResponse.class);
    List<Data> dataList = httpResponse.getServiceResponse().getData();
    Map<String, X509Certificate> downloadCertMap = new HashMap<>();
    for (Data data : dataList) {
      EncryptCertificate encryptCertificate = data.getEncryptCertificate();
      String decryptCertificate =
          aeadCipher.decrypt(
              encryptCertificate.getAssociatedData().getBytes(StandardCharsets.UTF_8),
              encryptCertificate.getNonce().getBytes(StandardCharsets.UTF_8),
              Base64.getDecoder().decode(encryptCertificate.getCiphertext()));
      X509Certificate certificate = certificateGenerator.apply(decryptCertificate);
      downloadCertMap.put(certificate.getSerialNumber().toString(16).toUpperCase(), certificate);
    }
    Validator validator =
        new WechatPay2Validator(verifierGenerator.apply(new ArrayList<>(downloadCertMap.values())));
    HttpClient newHttpClient = httpClient.newBuilder().validator(validator).build();
    this.httpClientMap.put(merchantId, newHttpClient);
    this.certificateMap.put(merchantId, downloadCertMap);
  }

  @Override
  public X509Certificate getCertificate(String merchantId, String wechatPayCertSerialNum) {
    return certificateMap.get(merchantId).get(wechatPayCertSerialNum);
  }

  @Override
  public X509Certificate getAvailableCertificate(String merchantId) {
    if (certificateMap.isEmpty()) {
      throw new IllegalArgumentException("certificateMap is empty.");
    }
    // 假设拿到的都是可用的，选取一个能用最久的
    X509Certificate longest = null;
    for (X509Certificate cert : certificateMap.get(merchantId).values()) {
      if (longest == null || cert.getNotAfter().after(longest.getNotAfter())) {
        longest = cert;
      }
    }
    return longest;
  }
}
