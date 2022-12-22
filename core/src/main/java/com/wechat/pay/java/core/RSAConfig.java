package com.wechat.pay.java.core;

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

  public static class Builder extends AbstractRSAConfigBuilder<Builder> {

    private List<X509Certificate> wechatPayCertificates;

    @Override
    protected Builder self() {
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
      return new RSAConfig(
          requireNonNull(merchantId),
          requireNonNull(privateKey),
          requireNonNull(merchantSerialNumber),
          new InMemoryCertificateProvider(requireNonNull(wechatPayCertificates)));
    }
  }
}
