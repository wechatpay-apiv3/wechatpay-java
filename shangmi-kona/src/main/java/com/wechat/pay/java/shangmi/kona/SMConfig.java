package com.wechat.pay.java.shangmi.kona;

import static com.wechat.pay.java.core.cipher.Constant.HEX;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public final class SMConfig implements Config {
  /** 商户号 */
  private final String merchantId;
  /** 商户私钥 */
  private final PrivateKey privateKey;
  /** 商户证书序列号 */
  private final String merchantSerialNumber;
  /** 微信支付平台证书Provider */
  private final CertificateProvider certificateProvider;

  private SMConfig(Builder builder) {
    this.merchantId = requireNonNull(builder.merchantId);
    this.privateKey = requireNonNull(builder.privateKey);
    this.merchantSerialNumber = requireNonNull(builder.merchantSerialNumber);
    this.certificateProvider = requireNonNull(builder.certificateProvider);
  }

  @Override
  public PrivacyEncryptor createEncryptor() {
    X509Certificate certificate = certificateProvider.getAvailableCertificate();
    return new SM2PrivacyEncryptor(
        certificate.getPublicKey(), certificate.getSerialNumber().toString(HEX));
  }

  @Override
  public PrivacyDecryptor createDecryptor() {
    return new SM2PrivacyDecryptor(privateKey);
  }

  @Override
  public Credential createCredential() {
    return new WechatPay2Credential(merchantId, new SM2Signer(merchantSerialNumber, privateKey));
  }

  @Override
  public Validator createValidator() {
    return new WechatPay2Validator(new SM2Verifier(certificateProvider));
  }

  public static class Builder {

    private String merchantId;
    private PrivateKey privateKey;
    private String merchantSerialNumber;
    private List<X509Certificate> wechatPayCertificates = new ArrayList<>();
    private CertificateProvider certificateProvider;

    public Builder merchantId(String merchantId) {
      this.merchantId = merchantId;
      return this;
    }

    public Builder privateKey(String privateKey) {

      return privateKey(SMPemUtil.loadPrivateKeyFromString(privateKey));
    }

    public Builder privateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
      return this;
    }

    /** 从指定的本地私钥文件加载私钥 */
    public Builder privateKeyFromPath(String privateKeyPath) {
      return privateKey(SMPemUtil.loadPrivateKeyFromPath(privateKeyPath));
    }

    public Builder merchantSerialNumber(String merchantSerialNumber) {
      this.merchantSerialNumber = merchantSerialNumber;
      return this;
    }

    /** 设置微信支付平台证书提供器 */
    public Builder wechatPayCertificateProvider(CertificateProvider provider) {
      this.certificateProvider = provider;
      return this;
    }

    /** 添加一个微信支付平台证书对象 */
    public Builder addWechatPayCertificate(X509Certificate certificate) {
      wechatPayCertificates.add(certificate);
      return this;
    }

    /** 添加一个字符串的微信支付平台证书 */
    public Builder addWechatPayCertificate(String certificate) {
      return addWechatPayCertificate(SMPemUtil.loadCertificateFromString(certificate));
    }

    /** 从指定的本地证书文件添加一个微信支付平台证书 */
    public Builder addWechatPayCertificateFromPath(String certificatePath) {
      return addWechatPayCertificate(SMPemUtil.loadCertificateFromPath(certificatePath));
    }

    /** 移除之前的微信支付平台证书，加入新的证书 */
    public Builder wechatPayCertificates(List<X509Certificate> wechatPayCertificates) {
      this.wechatPayCertificates = wechatPayCertificates;
      return this;
    }

    public SMConfig build() {
      if (wechatPayCertificates.isEmpty()) {
        throw new IllegalArgumentException(
            "Build SMConfig, wechatPayCertificates is empty.Please "
                + "call wechatPayCertificates() or wechatPayCertificatesFromPath() method.");
      }
      certificateProvider = new InMemoryCertificateProvider(wechatPayCertificates);
      return new SMConfig(this);
    }
  }
}
