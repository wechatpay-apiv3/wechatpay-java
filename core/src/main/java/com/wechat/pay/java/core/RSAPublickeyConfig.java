package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.http.AbstractHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

/**
 * RSA配置类
 */
public final class RSAPublickeyConfig extends AbstractRSAConfig
    implements NotificationConfig {

  private final PublicKey publicKey;
  private final AeadCipher aeadCipher;
  private RSAPublickeyConfig(Builder builder) {
    super(
        builder.merchantId,
        builder.privateKey,
        builder.merchantSerialNumber,
        builder.publickKey);
    this.publicKey = builder.publickKey;
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
    return new RSAVerifier(publicKey);
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
    protected PublicKey publickKey;
    protected AbstractHttpClientBuilder<?> httpClientBuilder;

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return self();
    }

    public Builder PublicKey(String privateKeyPath) {
      this.publickKey = PemUtil.loadPublicKeyFromPath(privateKeyPath);
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

    public RSAPublickeyConfig build() {
      requireNonNull(merchantId);
      requireNonNull(publickKey);
      requireNonNull(privateKey);
      requireNonNull(apiV3Key);
      requireNonNull(merchantSerialNumber);

      return new RSAPublickeyConfig(this);
    }
  }
}
