package com.wechat.pay.java.core.notification;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 签名类型为RSA的通知配置参数 */
public final class RSANotificationConfig extends AbstractRSANotificationConfig {

  private RSANotificationConfig(CertificateProvider certificateProvider, byte[] apiV3Key) {
    super(certificateProvider, apiV3Key);
  }

  public static class Builder {

    private List<X509Certificate> certificates;
    private byte[] apiV3Key;

    public Builder certificates(X509Certificate... certificates) {
      this.certificates = Arrays.asList(certificates);
      return this;
    }

    public Builder certificates(String... certificates) {
      List<X509Certificate> certificateList = new ArrayList<>();
      for (String certificateString : certificates) {
        X509Certificate x509Certificate = PemUtil.loadX509FromString(certificateString);
        certificateList.add(x509Certificate);
      }
      this.certificates = certificateList;
      return this;
    }

    public Builder certificatesFromPath(String... certificatePaths) {
      List<X509Certificate> certificateList = new ArrayList<>();
      for (String certificatePath : certificatePaths) {
        X509Certificate x509Certificate = PemUtil.loadX509FromPath(certificatePath);
        certificateList.add(x509Certificate);
      }
      this.certificates = certificateList;
      return this;
    }

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public RSANotificationConfig build() {
      CertificateProvider certificateProvider = new InMemoryCertificateProvider(certificates);
      return new RSANotificationConfig(certificateProvider, apiV3Key);
    }
  }
}
