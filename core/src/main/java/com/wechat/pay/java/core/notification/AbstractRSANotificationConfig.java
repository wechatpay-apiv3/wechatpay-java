package com.wechat.pay.java.core.notification;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Verifier;

public abstract class AbstractRSANotificationConfig implements NotificationConfig {

  public static final String RSA_SIGN_TYPE = "WECHATPAY2-SHA256-RSA2048";
  private static final String CIPHER_ALGORITHM = "AEAD_AES_256_GCM";
  private final CertificateProvider certificateProvider;
  private final byte[] apiV3Key;

  protected AbstractRSANotificationConfig(
      CertificateProvider certificateProvider, byte[] apiV3Key) {
    this.certificateProvider = certificateProvider;
    this.apiV3Key = apiV3Key;
  }

  @Override
  public String getSignType() {
    return RSA_SIGN_TYPE;
  }

  @Override
  public String getCipherType() {
    return CIPHER_ALGORITHM;
  }

  @Override
  public Verifier createVerifier() {
    return new RSAVerifier(certificateProvider);
  }

  @Override
  public AeadCipher createAeadCipher() {
    return new AeadAesCipher(apiV3Key);
  }
}
