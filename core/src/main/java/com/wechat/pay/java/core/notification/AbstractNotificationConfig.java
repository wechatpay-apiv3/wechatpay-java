package com.wechat.pay.java.core.notification;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Verifier;

public abstract class AbstractNotificationConfig implements NotificationConfig {

  public final String signType;
  private final String cipherAlgorithm;
  private final CertificateProvider certificateProvider;
  private final AeadCipher aeadCipher;

  protected AbstractNotificationConfig(
      String signType,
      String cipherAlgorithm,
      CertificateProvider certificateProvider,
      AeadCipher aeadCipher) {
    this.signType = signType;
    this.cipherAlgorithm = cipherAlgorithm;
    this.certificateProvider = certificateProvider;
    this.aeadCipher = aeadCipher;
  }

  @Override
  public String getSignType() {
    return signType;
  }

  @Override
  public String getCipherType() {
    return cipherAlgorithm;
  }

  @Override
  public Verifier createVerifier() {
    return new RSAVerifier(certificateProvider);
  }

  @Override
  public AeadCipher createAeadCipher() {
    return aeadCipher;
  }
}
