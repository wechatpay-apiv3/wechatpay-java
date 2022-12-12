package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.cipher.Constant.HEX;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.util.PemUtil;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/** 调用微信支付服务需要的RSA相关配置 */
public final class RSAConfig extends AbstractRSAConfig {

  private RSAConfig(
      String merchantId,
      PrivateKey privateKey,
      String merchantSerialNumber,
      CertificateProvider certificateProvider) {
    super(merchantId, privateKey, merchantSerialNumber, certificateProvider);
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
      return new RSAConfig(
          merchantId,
          privateKey,
          merchantSerialNumber,
          new InMemoryCertificateProvider(requireNonNull(wechatPayCertificates)));
    }
  }
}
