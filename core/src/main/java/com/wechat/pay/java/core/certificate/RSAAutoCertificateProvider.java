package com.wechat.pay.java.core.certificate;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import java.security.cert.X509Certificate;
import okhttp3.OkHttpClient;

/** RSA自动更新平台证书提供器 */
public class RSAAutoCertificateProvider implements CertificateProvider {

  private final CertificateManager certificateManager =
      RSACertificateManager.getInstance(); // 平台证书管理器
  private final String merchantId; // 商户号

  private RSAAutoCertificateProvider(
      HttpClient httpClient, String merchantId, AeadCipher aeadCipher) {
    this.merchantId = merchantId;
    certificateManager.putMerchant(merchantId, httpClient, aeadCipher);
  }

  @Override
  public X509Certificate getCertificate(String serialNumber) {
    return certificateManager.getCertificate(merchantId, serialNumber);
  }

  @Override
  public X509Certificate getAvailableCertificate() {
    return certificateManager.getAvailableCertificate(merchantId);
  }

  public static class Builder {
    private String merchantId;
    private byte[] apiV3Key;

    private Credential credential;
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

    private final Validator emptyValidator =
        new Validator() {
          @Override
          public boolean validate(HttpHeaders responseHeaders, String body) {
            return true;
          }
        };

    public RSAAutoCertificateProvider build() {
      if (httpClient == null) {
        httpClient = new OkHttpClientAdapter(credential, emptyValidator, new OkHttpClient());
      }
      return new RSAAutoCertificateProvider(httpClient, merchantId, new AeadAesCipher(apiV3Key));
    }
  }
}
