package com.wechat.pay.java.core.certificate;

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

/** 自动更新平台证书提供器抽象类 */
public abstract class AbstractAutoCertificateProvider implements CertificateProvider {
  private static final Logger log = LoggerFactory.getLogger(AbstractAutoCertificateProvider.class);
  private final String requestUrl; // 下载证书url
  private final Function<String, X509Certificate>
      certificateGenerator; // 将证书从String转为X509Certificate的方法
  private final Function<List<X509Certificate>, Verifier> verifierGenerator; // 生成verifier的方法
  protected static final int UPDATE_INTERVAL_MINUTE = 60; // 定时更新时间，1小时
  protected final SafeSingleScheduleExecutor executor =
      SafeSingleScheduleExecutor.getInstance(); // 安全的单线程定时执行器实例
  protected AeadCipher aeadCipher; // 解密平台证书的aeadCipher
  protected HttpClient httpClient; // 下载平台证书的httpClient
  protected Map<String, X509Certificate> certificateMap = new HashMap<>(); // 证书map

  AbstractAutoCertificateProvider(
      String requestUrl,
      Function<String, X509Certificate> certificateGenerator,
      Function<List<X509Certificate>, Verifier> verifierGenerator,
      AeadCipher aeadCipher,
      HttpClient httpClient) {
    this.requestUrl = requestUrl;
    this.certificateGenerator = certificateGenerator;
    this.verifierGenerator = verifierGenerator;
    this.aeadCipher = aeadCipher;
    this.httpClient = httpClient;
    downloadAndUpdate();
    Runnable runnable =
        () -> {
          log.info("Begin update Certificates.Date:{}", Instant.now());
          downloadAndUpdate();
          log.info("Finish update Certificates.Date:{}", Instant.now());
        };
    executor.scheduleAtFixedRate(runnable, 0, UPDATE_INTERVAL_MINUTE, TimeUnit.MINUTES);
  }

  /** 下载和更新证书 */
  protected void downloadAndUpdate() {
    this.certificateMap = download();
    Validator validator =
        new WechatPay2Validator(verifierGenerator.apply(new ArrayList<>(certificateMap.values())));
    httpClient = httpClient.newBuilder().validator(validator).build();
  }

  /**
   * 下载证书
   *
   * @return 下载得到的证书map
   */
  protected Map<String, X509Certificate> download() {
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
    return downloadCertMap;
  }

  @Override
  public X509Certificate getAvailableCertificate() {
    if (certificateMap.isEmpty()) {
      throw new IllegalArgumentException("The parameter list of constructor is empty.");
    }
    // 假设拿到的都是可用的，选取一个能用最久的
    X509Certificate longest = null;
    for (X509Certificate item : certificateMap.values()) {
      if (longest == null || item.getNotAfter().after(longest.getNotAfter())) {
        longest = item;
      }
    }
    return longest;
  }

  @Override
  public X509Certificate getCertificate(String serialNumber) {
    return certificateMap.get(serialNumber);
  }
}
