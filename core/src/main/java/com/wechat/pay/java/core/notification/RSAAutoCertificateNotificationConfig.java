package com.wechat.pay.java.core.notification;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.AbstractRSAConfigBuilder;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider;
import com.wechat.pay.java.core.http.HttpClient;
import java.nio.charset.StandardCharsets;

public final class RSAAutoCertificateNotificationConfig extends AbstractRSANotificationConfig {

  private RSAAutoCertificateNotificationConfig(
      CertificateProvider certificateProvider, byte[] apiV3Key) {
    super(certificateProvider, apiV3Key);
  }

  public static class Builder extends AbstractRSAConfigBuilder<Builder> {
    protected HttpClient httpClient;
    protected byte[] apiV3Key;

    public Builder apiV3Key(String apiV3key) {
      this.apiV3Key = apiV3key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    @Override
    protected Builder self() {
      return this;
    }

    public RSAAutoCertificateNotificationConfig build() {
      RSAAutoCertificateProvider.Builder builder =
          new RSAAutoCertificateProvider.Builder()
              .apiV3Key(requireNonNull(apiV3Key))
              .privateKey(requireNonNull(privateKey))
              .merchantId(requireNonNull(merchantId))
              .merchantSerialNumber(requireNonNull(merchantSerialNumber));
      if (httpClient != null) {
        builder.httpClient(httpClient);
      }
      return new RSAAutoCertificateNotificationConfig(builder.build(), apiV3Key);
    }
  }
}
