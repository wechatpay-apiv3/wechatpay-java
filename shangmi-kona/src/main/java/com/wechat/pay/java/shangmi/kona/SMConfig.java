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
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
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
  /** 最新的微信支付平台证书 */
  private final X509Certificate latestWechatPayCertificate;

  private SMConfig(
      String merchantId,
      PrivateKey privateKey,
      String merchantSerialNumber,
      CertificateProvider certificateProvider,
      X509Certificate latestWechatPayCertificate) {
    this.merchantId = merchantId;
    this.privateKey = privateKey;
    this.merchantSerialNumber = merchantSerialNumber;
    this.latestWechatPayCertificate = latestWechatPayCertificate;
    this.certificateProvider = certificateProvider;
  }

  @Override
  public PrivacyEncryptor createEncryptor() {
    return new SM2PrivacyEncryptor(
        latestWechatPayCertificate.getPublicKey(),
        latestWechatPayCertificate.getSerialNumber().toString(HEX));
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
    private List<X509Certificate> wechatPayCertificates;

    public Builder merchantId(String merchantId) {
      this.merchantId = merchantId;
      return this;
    }

    public Builder privateKey(String privateKey) {
      this.privateKey = SMPemUtil.loadPrivateKeyFromString(privateKey);
      return this;
    }

    public Builder privateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
      return this;
    }

    public Builder privateKeyFromPath(String privateKeyPath) {
      this.privateKey = SMPemUtil.loadPrivateKeyFromPath(privateKeyPath);
      return this;
    }

    public Builder merchantSerialNumber(String merchantSerialNumber) {
      this.merchantSerialNumber = merchantSerialNumber;
      return this;
    }

    public Builder merchantSerialNumberFromCertificate(String merchantCertificate) {
      X509Certificate x509Cert = SMPemUtil.loadX509FromString(merchantCertificate);
      if (x509Cert == null) {
        throw new IllegalArgumentException(
            "Get serial number from certificate, certificate is empty.");
      }
      this.merchantSerialNumber = x509Cert.getSerialNumber().toString(16);
      return this;
    }

    public Builder merchantSerialNumberFromCertificatePath(String path) {
      X509Certificate x509Cert = SMPemUtil.loadX509FromPath(path);
      if (x509Cert == null) {
        throw new IllegalArgumentException(
            "Get serial number from certificate path, certificate is empty.");
      }
      merchantSerialNumber = x509Cert.getSerialNumber().toString(16);
      return this;
    }

    public Builder wechatPayCertificates(String... wechatPayCertificates) {
      this.wechatPayCertificates = new ArrayList<>();
      for (String certificate : wechatPayCertificates) {
        X509Certificate x509Certificate = SMPemUtil.loadX509FromString(certificate);
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

    public Builder wechatPayCertificatesFromPath(String... certificatePaths) {
      this.wechatPayCertificates = new ArrayList<>();
      for (String certificatePath : certificatePaths) {
        X509Certificate x509Certificate = SMPemUtil.loadX509FromPath(certificatePath);
        if (x509Certificate != null) {
          this.wechatPayCertificates.add(x509Certificate);
        }
      }
      return this;
    }

    public SMConfig build() {
      requireNonNull(privateKey);
      requireNonNull(merchantSerialNumber);
      requireNonNull(merchantId);
      requireNonNull(wechatPayCertificates);
      if (wechatPayCertificates.isEmpty()) {
        throw new IllegalArgumentException(
            "Build SMConfig, wechatPayCertificates is empty.Please "
                + "call wechatPayCertificates() or wechatPayCertificatesFromPath() method.");
      }
      X509Certificate latestCertificate = null;
      // 获取最近可用的微信支付平台证书
      for (X509Certificate x509Certificate : wechatPayCertificates) {
        // 如果latestCertificate为空或者x509Certificate证书的有效开始时间在latestCertificate之后
        // 更新latestCertificate
        if (latestCertificate == null
            || x509Certificate.getNotBefore().after(latestCertificate.getNotBefore())) {
          latestCertificate = x509Certificate;
        }
      }
      try {
        latestCertificate.checkValidity();
      } catch (CertificateExpiredException | CertificateNotYetValidException e) {
        throw new IllegalArgumentException(
            "Build SMConfig, The latest wechatPay certificate is not valid or has expired.", e);
      }
      return new SMConfig(
          merchantId,
          privateKey,
          merchantSerialNumber,
          new InMemoryCertificateProvider(wechatPayCertificates),
          latestCertificate);
    }
  }
}
