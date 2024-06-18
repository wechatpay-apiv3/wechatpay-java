package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

/** 签名类型为RSA的通知配置参数 */
public final class RSAPublicKeyNotificationConfig extends AbstractNotificationConfig {

  private RSAPublicKeyNotificationConfig(
      PublicKey publicKey, String publicKeyId, AeadCipher aeadCipher) {
    super(RSA_SIGN_TYPE, AES_CIPHER_ALGORITHM, publicKey, publicKeyId, aeadCipher);
  }

  public static class Builder {
    private byte[] apiV3Key;

    private PublicKey publicKey;
    private String publicKeyId;

    public Builder publicKey(String publicKey) {
      this.publicKey = PemUtil.loadPublicKeyFromString(publicKey);
      return this;
    }

    public Builder publicKey(PublicKey publicKey) {
      this.publicKey = publicKey;
      return this;
    }

    public Builder publicKeyFromPath(String publicKeyPath) {
      this.publicKey = PemUtil.loadPublicKeyFromPath(publicKeyPath);
      return this;
    }

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public Builder publicKeyId(String publicKeyId) {
      this.publicKeyId = publicKeyId;
      return this;
    }

    public RSAPublicKeyNotificationConfig build() {
      requireNonNull(publicKey);
      requireNonNull(publicKeyId);
      requireNonNull(apiV3Key);
      return new RSAPublicKeyNotificationConfig(
          publicKey, requireNonNull(publicKeyId), new AeadAesCipher(requireNonNull(apiV3Key)));
    }
  }
}
