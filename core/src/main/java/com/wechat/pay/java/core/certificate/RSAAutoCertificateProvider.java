package com.wechat.pay.java.core.certificate;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import java.security.PrivateKey;

/** RSA自动更新平台证书提供器 */
public class RSAAutoCertificateProvider extends AbstractAutoCertificateProvider {

  private static final String REQUEST_URL =
      "https://api.mch.weixin.qq.com/v3/certificates?algorithm_type=RSA"; // 下载证书url

  private static final CertificateHandler certificateHandler = new RSACertificateHandler();

  private RSAAutoCertificateProvider(AeadCipher aeadCipher, HttpClient httpClient) {
    super(REQUEST_URL, certificateHandler, aeadCipher, httpClient);
  }

  public static class Builder {
    private String merchantId;
    private byte[] apiV3Key;

    private Credential credential;
    private PrivateKey privateKey;
    private String merchantSerialNumber;
    private HttpClient httpClient;

    public Builder merchantId(String merchantId) {
      this.merchantId = merchantId;
      return this;
    }

    public Builder apiV3Key(byte[] apiV3Key) {
      this.apiV3Key = apiV3Key;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder credential(Credential credential) {
      this.credential = credential;
      return this;
    }

    public Builder privateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
      return this;
    }

    public Builder merchantSerialNumber(String merchantSerialNumber) {
      this.merchantSerialNumber = merchantSerialNumber;
      return this;
    }

    private final Validator emptyValidator =
        new Validator() {
          @Override
          public boolean validate(HttpHeaders responseHeaders, String body) {
            return true;
          }
        };

    public RSAAutoCertificateProvider build() {
      if (httpClient == null) {
        DefaultHttpClientBuilder httpClientBuilder =
            new DefaultHttpClientBuilder().validator(emptyValidator);
        if (credential == null) {
          credential =
              new WechatPay2Credential(
                  requireNonNull(merchantId),
                  new RSASigner(requireNonNull(merchantSerialNumber), requireNonNull(privateKey)));
        }
        httpClient = httpClientBuilder.credential(credential).build();
      }
      return new RSAAutoCertificateProvider(
          new AeadAesCipher(requireNonNull(apiV3Key)), httpClient);
    }
  }
}
