package com.wechat.pay.java.core;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/** 具有自动下载平台证书能力的RSA配置类 */
public class RSAAutoCertificateConfig extends AbstractRSAConfig {

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
    protected Proxy proxy;

    public Builder apiV3Key(String apiV3key) {
      this.apiV3Key = apiV3key.getBytes(StandardCharsets.UTF_8);
      return self();
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder proxy(Proxy proxy) {
      this.proxy = proxy;
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
      if (httpClient != null && proxy != null) {
        throw new IllegalArgumentException(
            "Only one of httpClient() and proxy() method can be called.");
      }
      if (proxy != null) {
        initHttpClientWithProxy();
      }
      if (httpClient != null) {
        providerBuilder.httpClient(httpClient);
      }
      return new RSAAutoCertificateConfig(
          merchantId, privateKey, merchantSerialNumber, providerBuilder.build());
    }

    private void initHttpClientWithProxy() {
      Credential credential =
          new WechatPay2Credential(merchantId, new RSASigner(merchantSerialNumber, privateKey));
      Validator validator = new WechatPay2Validator((serialNumber, message, signature) -> true);
      httpClient =
          new DefaultHttpClientBuilder()
              .proxy(proxy)
              .credential(credential)
              .validator(validator)
              .build();
    }
  }
}
