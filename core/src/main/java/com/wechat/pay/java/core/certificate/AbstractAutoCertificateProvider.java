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

  protected AbstractAutoCertificateProvider(
      String requestUrl,
      CertificateHandler certificateHandler,
      AeadCipher aeadCipher,
      HttpClient httpClient,
      String merchantId,
      Map<String, Map<String, X509Certificate>> wechatPayCertificateMap,
      Map<String, Validator> validatorMap,
      Map<String, Integer> updatesMap) {
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
    httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(requestUrl)
            .addHeader(Constant.ACCEPT, " */*")
            .addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue())
            .build();
    downloadAndUpdate(wechatPayCertificateMap, validatorMap, updatesMap);
    Runnable runnable =
        () -> {
          log.info(
              "Begin update Certificates.merchantId:{},total updates:{}",
              merchantId,
              updatesMap.getOrDefault(merchantId, 1));
          downloadAndUpdate(wechatPayCertificateMap, validatorMap, updatesMap);
          log.info(
              "Finish update Certificates.merchantId:{},total updates:{}",
              merchantId,
              updatesMap.get(merchantId));
        };
    executor.scheduleAtFixedRate(
        runnable, UPDATE_INTERVAL_MINUTE, UPDATE_INTERVAL_MINUTE, TimeUnit.MINUTES);
  }

  /** 下载和更新证书 */
  protected void downloadAndUpdate(
      Map<String, Map<String, X509Certificate>> wechatPayCertificateMap,
      Map<String, Validator> validatorMap,
      Map<String, Integer> updatesMap) {
    try {
      HttpResponse<DownloadCertificateResponse> httpResponse = downloadCertificate(httpClient);
      validateCertificate(httpResponse, validatorMap.get(merchantId));
      updateCertificate(httpResponse, wechatPayCertificateMap);
      Validator validator =
          new WechatPay2Validator(
              certificateHandler.generateVerifier(
                  new ArrayList<>(wechatPayCertificateMap.get(merchantId).values())));
      validatorMap.put(merchantId, validator);
      updatesMap.put(merchantId, updatesMap.getOrDefault(merchantId, 0) + 1);
    } catch (Exception e) {
      if (validatorMap.get(merchantId) == null) {
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
  protected void validateCertificate(
      HttpResponse<DownloadCertificateResponse> httpResponse, Validator validator) {
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
}
