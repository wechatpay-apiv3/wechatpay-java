package com.wechat.pay.java.core.notification;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Verifier;
import java.security.PublicKey;

public abstract class AbstractNotificationConfig implements NotificationConfig {

  public final String signType;
  private final String cipherAlgorithm;
  private final CertificateProvider certificateProvider;
  private final AeadCipher aeadCipher;
  private final PublicKey publicKey;
  private final String publicKeyId;

  protected AbstractNotificationConfig(
      String signType,
      String cipherAlgorithm,
      CertificateProvider certificateProvider,
      AeadCipher aeadCipher) {
    this.signType = signType;
    this.cipherAlgorithm = cipherAlgorithm;
    this.certificateProvider = certificateProvider;
    this.aeadCipher = aeadCipher;
    this.publicKey = null;
    this.publicKeyId = null;
  }

  protected AbstractNotificationConfig(
      String signType,
      String cipherAlgorithm,
      PublicKey publicKey,
      String publicKeyId,
      AeadCipher aeadCipher) {
    this.signType = signType;
    this.cipherAlgorithm = cipherAlgorithm;
    this.publicKey = publicKey;
    this.publicKeyId = publicKeyId;
    this.aeadCipher = aeadCipher;
    this.certificateProvider = null;
  }

  protected AbstractNotificationConfig(
      String signType,
      String cipherAlgorithm,
      CertificateProvider certificateProvider,
      PublicKey publicKey,
      String publicKeyId,
      AeadCipher aeadCipher) {
    this.signType = signType;
    this.cipherAlgorithm = cipherAlgorithm;
    this.publicKey = publicKey;
    this.publicKeyId = publicKeyId;
    this.aeadCipher = aeadCipher;
    this.certificateProvider = certificateProvider;
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
    if (publicKey != null && certificateProvider != null) {
      return new RSAVerifier(publicKey, publicKeyId, certificateProvider);
    }
    if (publicKey != null) {
      return new RSAVerifier(publicKey, publicKeyId);
    }
    return new RSAVerifier(certificateProvider);
  }

  @Override
  public AeadCipher createAeadCipher() {
    return aeadCipher;
  }
}
