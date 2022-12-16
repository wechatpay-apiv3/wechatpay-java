package com.wechat.pay.java.core;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider;
import com.wechat.pay.java.core.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/** 具有自动下载平台证书能力的RSA配置类 */
public final class RSAAutoCertificateConfig extends AbstractRSAConfig {

  private RSAAutoCertificateConfig(
      String merchantId,
      PrivateKey privateKey,
      String merchantSerialNumber,
      CertificateProvider certificateProvider) {
    super(merchantId, privateKey, merchantSerialNumber, certificateProvider);
  }

  public static class Builder extends AbstractRSAConfigBuilder<Builder> {
    protected HttpClient httpClient;
    protected byte[] apiV3Key;

    public Builder apiV3Key(String apiV3key) {
      this.apiV3Key = apiV3key.getBytes(StandardCharsets.UTF_8);
      return self();
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    @Override
    protected Builder self() {
      return this;
    }

    public RSAAutoCertificateConfig build() {
      RSAAutoCertificateProvider.Builder providerBuilder =
          new RSAAutoCertificateProvider.Builder()
              .merchantId(requireNonNull(merchantId))
              .apiV3Key(requireNonNull(apiV3Key))
              .privateKey(requireNonNull(privateKey))
              .merchantSerialNumber(requireNonNull(merchantSerialNumber));
      if (httpClient != null) {
        providerBuilder.httpClient(httpClient);
      }
      return new RSAAutoCertificateConfig(
          merchantId, privateKey, merchantSerialNumber, providerBuilder.build());
    }
  }
}
