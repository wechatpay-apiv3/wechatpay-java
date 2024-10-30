package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

/** 使用微信支付平台公钥的RSA配置类。 每次构造都要求传入平台公钥以及平台公钥id，如果使用平台证书建议用RSAAutoCertificateConfig类 */
public final class RSAPublicKeyConfig extends AbstractRSAConfig implements NotificationConfig {

  private final PublicKey publicKey;
  private final AeadCipher aeadCipher;
  private final String publicKeyId;

  private RSAPublicKeyConfig(Builder builder) {
    super(
        builder.merchantId,
        builder.privateKey,
        builder.merchantSerialNumber,
        builder.publicKey,
        builder.publicKeyId);
    this.publicKey = builder.publicKey;
    this.publicKeyId = builder.publicKeyId;
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
    return new RSAVerifier(publicKey, publicKeyId);
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
    protected byte[] apiV3Key;
    protected PublicKey publicKey;
    protected String publicKeyId;

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return self();
    }

    public Builder publicKey(String publicKey) {
      this.publicKey = PemUtil.loadPublicKeyFromString(publicKey);
      return self();
    }

    public Builder publicKey(PublicKey publicKey) {
      this.publicKey = publicKey;
      return self();
    }

    public Builder publicKeyFromPath(String publicKeyPath) {
      this.publicKey = PemUtil.loadPublicKeyFromPath(publicKeyPath);
      return self();
    }

    public Builder publicKeyId(String publicKeyId) {
      this.publicKeyId = publicKeyId;
      return self();
    }

    @Override
    protected Builder self() {
      return this;
    }

    public RSAPublicKeyConfig build() {
      requireNonNull(merchantId);
      requireNonNull(publicKey);
      requireNonNull(publicKeyId);
      requireNonNull(privateKey);
      requireNonNull(merchantSerialNumber);
      requireNonNull(apiV3Key);

      return new RSAPublicKeyConfig(this);
    }
  }
}
