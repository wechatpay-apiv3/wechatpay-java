package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.cipher.Constant.HEX;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.RSAPrivacyDecryptor;
import com.wechat.pay.java.core.cipher.RSAPrivacyEncryptor;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.util.PemUtil;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 调用微信支付服务需要的RSA相关配置 */
public final class RSAConfig implements Config {

  /** 商户号 */
  private final String merchantId;
  /** 商户私钥 */
  private final PrivateKey privateKey;
  /** 商户证书序列号 */
  private final String merchantSerialNumber;
  /** 微信支付平台证书Provider */
  private final CertificateProvider certificateProvider;

  private RSAConfig(
      String merchantId,
      PrivateKey privateKey,
      String merchantSerialNumber,
      CertificateProvider certificateProvider) {
    this.merchantId = merchantId;
    this.privateKey = privateKey;
    this.merchantSerialNumber = merchantSerialNumber;
    this.certificateProvider = certificateProvider;
  }

  @Override
  public PrivacyEncryptor createEncryptor() {
    X509Certificate certificate = certificateProvider.getAvailableCertificate();
    return new RSAPrivacyEncryptor(
        certificate.getPublicKey(), certificate.getSerialNumber().toString(HEX));
  }

  @Override
  public PrivacyDecryptor createDecryptor() {
    return new RSAPrivacyDecryptor(privateKey);
  }

  @Override
  public Credential createCredential() {
    return new WechatPay2Credential(merchantId, new RSASigner(merchantSerialNumber, privateKey));
  }

  @Override
  public Validator createValidator() {
    return new WechatPay2Validator(new RSAVerifier(certificateProvider));
  }

  public static class Builder {

    private String merchantId;
    private PrivateKey privateKey;
    private String merchantSerialNumber;
    private List<X509Certificate> wechatPayCertificates;

    public Builder merchantId(String merchantId) {
      this.merchantId = merchantId;
      return this;
    }

    public Builder privateKey(String privateKey) {
      this.privateKey = PemUtil.loadPrivateKeyFromString(privateKey);
      return this;
    }

    public Builder privateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
      return this;
    }

    public Builder privateKeyFromPath(String keyPath) {
      this.privateKey = PemUtil.loadPrivateKeyFromPath(keyPath);
      return this;
    }

    public Builder merchantSerialNumber(String merchantSerialNumber) {
      this.merchantSerialNumber = merchantSerialNumber;
      return this;
    }

    public Builder merchantSerialNumberFromCertificate(X509Certificate merchantCertificate) {
      this.merchantSerialNumber = merchantCertificate.getSerialNumber().toString(HEX);
      return this;
    }

    public Builder merchantSerialNumberFromCertificate(String merchantCertificate) {
      X509Certificate x509Certificate = PemUtil.loadX509FromString(merchantCertificate);
      this.merchantSerialNumber = x509Certificate.getSerialNumber().toString(HEX);
      return this;
    }

    public Builder merchantSerialNumberFromPath(String path) {
      X509Certificate x509Certificate = PemUtil.loadX509FromPath(path);
      this.merchantSerialNumber = x509Certificate.getSerialNumber().toString(HEX);
      return this;
    }

    public Builder wechatPayCertificates(String... wechatPayCertificates) {
      this.wechatPayCertificates = new ArrayList<>();
      for (String certificate : wechatPayCertificates) {
        X509Certificate x509Certificate = PemUtil.loadX509FromString(certificate);
        if (x509Certificate != null) {
          this.wechatPayCertificates.add(x509Certificate);
        }
      }
      return this;
    }

    public Builder wechatPayCertificates(X509Certificate... wechatPayCertificates) {
      this.wechatPayCertificates = Arrays.asList(wechatPayCertificates);
      return this;
    }

    public Builder wechatPayCertificatesFromPath(String... certPaths) {
      this.wechatPayCertificates = new ArrayList<>();
      for (String certPath : certPaths) {
        X509Certificate x509Certificate = PemUtil.loadX509FromPath(certPath);
        if (x509Certificate != null) {
          this.wechatPayCertificates.add(x509Certificate);
        }
      }
      return this;
    }

    public RSAConfig build() {
      requireNonNull(privateKey);
      requireNonNull(merchantSerialNumber);
      requireNonNull(merchantId);
      if (wechatPayCertificates.isEmpty()) {
        throw new IllegalArgumentException(
            "Build RSAConfig, wechatPayCertificates is empty.Please "
                + "call wechatPayCertificates() or wechatPayCertificatesFromPath() method.");
      }
      return new RSAConfig(
          merchantId,
          privateKey,
          merchantSerialNumber,
          new InMemoryCertificateProvider(wechatPayCertificates));
    }
  }
}
