package com.wechat.pay.java.core;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.RSAPrivacyDecryptor;
import com.wechat.pay.java.core.cipher.RSAPrivacyEncryptor;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.util.PemUtil;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/** RSAConfig抽象类 */
public abstract class AbstractRSAConfig implements Config {

  /** 使用微信支付平台证书验签 */
  protected AbstractRSAConfig(
      String merchantId,
      PrivateKey privateKey,
      String merchantSerialNumber,
      CertificateProvider certificateProvider) {
    this.merchantId = merchantId;
    this.privateKey = privateKey;
    this.merchantSerialNumber = merchantSerialNumber;
    this.certificateProvider = certificateProvider;
    this.publicKey = null;
    this.publicKeyId = null;
  }

  /** 使用微信支付公钥验签 */
  protected AbstractRSAConfig(
      String merchantId,
      PrivateKey privateKey,
      String merchantSerialNumber,
      PublicKey publicKey,
      String publicKeyId) {
    this.merchantId = merchantId;
    this.privateKey = privateKey;
    this.merchantSerialNumber = merchantSerialNumber;
    this.certificateProvider = null;
    this.publicKey = publicKey;
    this.publicKeyId = publicKeyId;
  }

  /** 商户号 */
  private final String merchantId;

  /** 商户私钥 */
  private final PrivateKey privateKey;

  /** 商户证书序列号 */
  private final String merchantSerialNumber;

  /** 微信支付平台证书Provider */
  private final CertificateProvider certificateProvider;
  /** 微信支付平台公钥 */
  private final PublicKey publicKey;
  /** 微信支付平台公钥Id */
  private final String publicKeyId;

  @Override
  public PrivacyEncryptor createEncryptor() {
    if (publicKey != null) {
      return new RSAPrivacyEncryptor(publicKey, publicKeyId);
    }
    X509Certificate certificate = certificateProvider.getAvailableCertificate();
    return new RSAPrivacyEncryptor(
        certificate.getPublicKey(), PemUtil.getSerialNumber(certificate));
  }

  @Override
  public PrivacyDecryptor createDecryptor() {
    return new RSAPrivacyDecryptor(privateKey);
  }

  @Override
  public Credential createCredential() {
    return new WechatPay2Credential(merchantId, createSigner());
  }

  @Override
  public Validator createValidator() {
    if (publicKey != null) {
      return new WechatPay2Validator(new RSAVerifier(publicKey, publicKeyId));
    }
    return new WechatPay2Validator(new RSAVerifier(certificateProvider));
  }

  @Override
  public Signer createSigner() {
    return new RSASigner(merchantSerialNumber, privateKey);
  }
}
