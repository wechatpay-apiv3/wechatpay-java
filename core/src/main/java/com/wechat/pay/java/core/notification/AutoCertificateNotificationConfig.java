package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.AbstractRSAConfigBuilder;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.http.HttpClient;
import java.nio.charset.StandardCharsets;

/**
 * 通知回调配置类
 *
 * @deprecated 请使用 RSAAutoCertificateConfig。 从版本 v0.2.4 起, 该类是多余的，而且功能不完整。
 *     开发者应尽快迁移。我们将在未来某个时间移除这段废弃的代码。
 */
@Deprecated
public final class AutoCertificateNotificationConfig extends AbstractNotificationConfig {

  private AutoCertificateNotificationConfig(
      CertificateProvider certificateProvider, AeadCipher aeadAesCipher) {
    super(RSA_SIGN_TYPE, AES_CIPHER_ALGORITHM, certificateProvider, aeadAesCipher);
  }

  public static class Builder extends AbstractRSAConfigBuilder<Builder> {
    protected HttpClient httpClient;
    protected byte[] apiV3Key;

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
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

    public AutoCertificateNotificationConfig build() {
      RSAAutoCertificateProvider.Builder builder =
          new RSAAutoCertificateProvider.Builder()
              .apiV3Key(requireNonNull(apiV3Key))
              .privateKey(requireNonNull(privateKey))
              .merchantId(requireNonNull(merchantId))
              .merchantSerialNumber(requireNonNull(merchantSerialNumber));
      if (httpClient != null) {
        builder.httpClient(httpClient);
      }
      return new AutoCertificateNotificationConfig(
          builder.build(), new AeadAesCipher(requireNonNull(apiV3Key)));
    }
  }
}
