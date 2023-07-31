package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider;
import com.wechat.pay.java.core.cipher.*;
import com.wechat.pay.java.core.http.AbstractHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.notification.NotificationConfig;
import java.nio.charset.StandardCharsets;

/**
 * 具有自动下载并更新平台证书能力的RSA配置类。 每次构造，都会立即使用传入的商户参数下载微信支付平台证书。 如果下载成功，SDK 会将商户参数注册或更新至
 * AutoCertificateService。若下载失败，将会抛出异常。 为了提高性能，建议将配置类作为全局变量，减少不必要的证书下载，避免资源浪费
 */
public final class RSAAutoCertificateConfig extends AbstractRSAConfig
    implements NotificationConfig {

  private final CertificateProvider certificateProvider;
  private final AeadCipher aeadCipher;

  private RSAAutoCertificateConfig(Builder builder) {
    super(
        builder.merchantId,
        builder.privateKey,
        builder.merchantSerialNumber,
        builder.certificateProvider);
    this.certificateProvider = builder.certificateProvider;
    this.aeadCipher = new AeadAesCipher(builder.apiV3Key);
  }

  /**
   * 获取签名类型
   *
   * @return 签名类型
   */
  @Override
  public String getSignType() {
    return RSA_SIGN_TYPE;
  }

  /**
   * 获取认证加解密器类型
   *
   * @return 认证加解密器类型
   */
  @Override
  public String getCipherType() {
    return AES_CIPHER_ALGORITHM;
  }

  /**
   * 创建验签器
   *
   * @return 验签器
   */
  @Override
  public Verifier createVerifier() {
    return new RSAVerifier(certificateProvider);
  }

  /**
   * 创建认证加解密器
   *
   * @return 认证加解密器
   */
  @Override
  public AeadCipher createAeadCipher() {
    return aeadCipher;
  }

  public static class Builder extends AbstractRSAConfigBuilder<Builder> {
    protected HttpClient httpClient;
    protected byte[] apiV3Key;
    protected CertificateProvider certificateProvider;
    protected AbstractHttpClientBuilder<?> httpClientBuilder;

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return self();
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder httpClientBuilder(AbstractHttpClientBuilder<?> builder) {
      httpClientBuilder = builder;
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

      if (httpClientBuilder != null) {
        providerBuilder.httpClientBuilder(httpClientBuilder);
      }

      certificateProvider = providerBuilder.build();

      return new RSAAutoCertificateConfig(this);
    }
  }
}
