package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通知回调配置类
 *
 * @deprecated 请使用 RSAAutoCertificateConfig，开发者应尽快迁移，我们将在未来某个时间移除这段废弃的代码。
 */
public final class RSANotificationConfig extends AbstractNotificationConfig {

  private RSANotificationConfig(CertificateProvider certificateProvider, AeadCipher aeadCipher) {
    super(RSA_SIGN_TYPE, AES_CIPHER_ALGORITHM, certificateProvider, aeadCipher);
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
      return new RSANotificationConfig(
          certificateProvider, new AeadAesCipher(requireNonNull(apiV3Key)));
    }
  }
}
