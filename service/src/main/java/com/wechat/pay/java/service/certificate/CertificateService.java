package com.wechat.pay.java.service.certificate;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.ParseException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.HttpResponse;
import com.wechat.pay.java.core.http.MediaType;
import com.wechat.pay.java.core.util.PemUtil;
import com.wechat.pay.java.service.certificate.model.Data;
import com.wechat.pay.java.service.certificate.model.DownloadCertificateResponse;
import com.wechat.pay.java.service.certificate.model.EncryptCertificate;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/** 证书服务 */
public class CertificateService {

  public static final String CERTIFICATE_DOWNLOAD_PATH =
      "https://api.mch.weixin.qq.com/v3/certificates";
  private final HttpClient httpClient;

  public CertificateService(Config config) {
    this.httpClient =
        new DefaultHttpClientBuilder()
            .credential(requireNonNull(config.createCredential()))
            .validator(requireNonNull(config.createValidator()))
            .build();
  }

  public CertificateService(HttpClient httpClient) {
    this.httpClient = requireNonNull(httpClient);
  }

  /**
   * 获取平台证书列表
   *
   * @param aeadCipher 认证加解密器，用于解密证书
   * @return 证书列表
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public List<X509Certificate> downloadCertificate(AeadCipher aeadCipher) {
    requireNonNull(aeadCipher);
    HttpRequest request =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(CERTIFICATE_DOWNLOAD_PATH)
            .addHeader(Constant.ACCEPT, " */*")
            .addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue())
            .build();
    HttpResponse<DownloadCertificateResponse> httpResponse =
        httpClient.execute(request, DownloadCertificateResponse.class);
    List<Data> dataList = httpResponse.getServiceResponse().getData();
    List<X509Certificate> certificates = new ArrayList<>();
    for (Data data : dataList) {
      EncryptCertificate encryptCertificate = data.getEncryptCertificate();
      String decryptCertificate =
          aeadCipher.decryptToString(
              encryptCertificate.getAssociatedData().getBytes(StandardCharsets.UTF_8),
              encryptCertificate.getNonce().getBytes(StandardCharsets.UTF_8),
              Base64.getDecoder().decode(encryptCertificate.getCiphertext()));
      certificates.add(PemUtil.loadX509FromString(decryptCertificate));
    }
    return certificates;
  }
}
