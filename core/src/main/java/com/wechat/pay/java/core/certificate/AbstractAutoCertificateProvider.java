package com.wechat.pay.java.core.certificate;

import static com.wechat.pay.java.core.cipher.Constant.HEX;

import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.wechat.pay.java.core.certificate.model.Data;
import com.wechat.pay.java.core.certificate.model.DownloadCertificateResponse;
import com.wechat.pay.java.core.certificate.model.EncryptCertificate;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.HttpResponse;
import com.wechat.pay.java.core.http.JsonResponseBody;
import com.wechat.pay.java.core.http.MediaType;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 自动更新平台证书提供器抽象类 */
public abstract class AbstractAutoCertificateProvider implements CertificateProvider {
  private static final Logger log = LoggerFactory.getLogger(AbstractAutoCertificateProvider.class);
  protected static final int UPDATE_INTERVAL_MINUTE = 60; // 定时更新时间，1小时
  protected final SafeSingleScheduleExecutor executor =
      SafeSingleScheduleExecutor.getInstance(); // 安全的单线程定时执行器实例
  protected String requestUrl; // 请求URl
  protected String merchantId; // 商户号

  protected CertificateHandler certificateHandler; // 证书处理器
  protected AeadCipher aeadCipher; // 解密平台证书的aeadCipher;
  protected HttpClient httpClient; // 下载平台证书的httpClient
  private final HttpRequest httpRequest; // http请求
  private Validator validator; // 验证器

  private int updateTime; // 自动更新次数
  private final Map<String, Map<String, X509Certificate>> certificateMap; // 证书map

  protected AbstractAutoCertificateProvider(
      String requestUrl,
      CertificateHandler certificateHandler,
      AeadCipher aeadCipher,
      HttpClient httpClient,
      String merchantId,
      Map<String, Map<String, X509Certificate>> wechatPayCertificateMap) {
    this.merchantId = merchantId;
    synchronized (AbstractAutoCertificateProvider.class) {
      if (!wechatPayCertificateMap.containsKey(merchantId)) {
        wechatPayCertificateMap.put(merchantId, new HashMap<>());
      } else {
        throw new IllegalStateException(
            "The corresponding provider for the merchant already exists.");
      }
    }
    this.requestUrl = requestUrl;
    this.certificateHandler = certificateHandler;
    this.aeadCipher = aeadCipher;
    this.httpClient = httpClient;
    this.certificateMap = wechatPayCertificateMap;
    httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(requestUrl)
            .addHeader(Constant.ACCEPT, " */*")
            .addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue())
            .build();
    downloadAndUpdate(wechatPayCertificateMap);
    Runnable runnable =
        () -> {
          log.info(
              "Begin update Certificates.merchantId:{},total updates:{}", merchantId, updateTime);
          downloadAndUpdate(wechatPayCertificateMap);
          log.info(
              "Finish update Certificates.merchantId:{},total updates:{}", merchantId, updateTime);
        };
    executor.scheduleAtFixedRate(
        runnable, UPDATE_INTERVAL_MINUTE, UPDATE_INTERVAL_MINUTE, TimeUnit.MINUTES);
  }

  /** 下载和更新证书 */
  protected void downloadAndUpdate(
      Map<String, Map<String, X509Certificate>> wechatPayCertificateMap) {
    try {
      HttpResponse<DownloadCertificateResponse> httpResponse = downloadCertificate(httpClient);
      validateCertificate(httpResponse);
      updateCertificate(httpResponse, wechatPayCertificateMap);
      validator =
          new WechatPay2Validator(
              certificateHandler.generateVerifier(
                  new ArrayList<>(wechatPayCertificateMap.get(merchantId).values())));
      updateTime++;
    } catch (Exception e) {
      if (validator == null) {
        throw e;
      }
      log.error("Download and update WechatPay certificates failed.", e);
    }
  }

  /**
   * 下载证书
   *
   * @return httpResponse
   */
  protected HttpResponse<DownloadCertificateResponse> downloadCertificate(HttpClient httpClient) {
    HttpResponse<DownloadCertificateResponse> httpResponse;
    httpResponse = httpClient.execute(httpRequest, DownloadCertificateResponse.class);
    return httpResponse;
  }

  /**
   * 校验下载证书
   *
   * @param httpResponse httpResponse
   */
  protected void validateCertificate(HttpResponse<DownloadCertificateResponse> httpResponse) {
    JsonResponseBody responseBody = (JsonResponseBody) (httpResponse.getBody());
    if (validator != null
        && !validator.validate(httpResponse.getHeaders(), responseBody.getBody())) {
      throw new ValidationException(
          String.format(
              "Validate response failed,the WechatPay signature is incorrect.responseHeader[%s]\tresponseBody[%.1024s]",
              httpResponse.getHeaders(), httpResponse.getServiceResponse()));
    }
  }

  /**
   * 更新证书
   *
   * @param httpResponse httpResponse
   */
  protected void updateCertificate(
      HttpResponse<DownloadCertificateResponse> httpResponse,
      Map<String, Map<String, X509Certificate>> wechatPayCertificateMap) {
    List<Data> dataList = httpResponse.getServiceResponse().getData();
    Map<String, X509Certificate> downloadCertMap = new HashMap<>();
    for (Data data : dataList) {
      EncryptCertificate encryptCertificate = data.getEncryptCertificate();
      X509Certificate certificate;
      String decryptCertificate =
          aeadCipher.decrypt(
              encryptCertificate.getAssociatedData().getBytes(StandardCharsets.UTF_8),
              encryptCertificate.getNonce().getBytes(StandardCharsets.UTF_8),
              Base64.getDecoder().decode(encryptCertificate.getCiphertext()));
      certificate = certificateHandler.generateCertificate(decryptCertificate);
      downloadCertMap.put(certificate.getSerialNumber().toString(HEX).toUpperCase(), certificate);
    }
    wechatPayCertificateMap.put(merchantId, downloadCertMap);
  }

  public X509Certificate getAvailableCertificate(Map<String, X509Certificate> certificateMap) {
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
    return certificateMap.get(merchantId).get(serialNumber);
  }

  @Override
  public X509Certificate getAvailableCertificate() {
    return getAvailableCertificate(certificateMap.get(merchantId));
  }
}
