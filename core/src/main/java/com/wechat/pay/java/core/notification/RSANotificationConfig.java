package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 签名类型为RSA的通知配置参数 */
public final class RSANotificationConfig extends AbstractNotificationConfig {

  private RSANotificationConfig(CertificateProvider certificateProvider, AeadCipher aeadCipher) {
    super(RSA_SIGN_TYPE, AES_CIPHER_ALGORITHM, certificateProvider, aeadCipher);
  }

  private RSANotificationConfig(PublicKey publicKey, String publicKeyId, AeadCipher aeadCipher) {
    super(RSA_SIGN_TYPE, AES_CIPHER_ALGORITHM, publicKey, publicKeyId, aeadCipher);
  }

  public static class Builder {

    private List<X509Certificate> certificates;
    private byte[] apiV3Key;

    private PublicKey publicKey;
    private String publicKeyId;

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

    public Builder publicKey(String publicKey) {
      this.publicKey = PemUtil.loadPublicKeyFromString(publicKey);
      return this;
    }

    public Builder publicKey(PublicKey publicKey) {
      this.publicKey = publicKey;
      return this;
    }

    public Builder publicFromPath(String publicKeyPath) {
      this.publicKey = PemUtil.loadPublicKeyFromPath(publicKeyPath);
      return this;
    }

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public Builder publicKeyId(String publicKeyId) {
      this.publicKeyId = publicKeyId;
      return this;
    }

    public RSANotificationConfig build() {
      if (publicKey != null && certificates != null) {
        throw new ValidationException(
            "only one parameter can be set between publicKey and certificates");
      }
      if (publicKey != null) {
        return new RSANotificationConfig(
            publicKey, requireNonNull(publicKeyId), new AeadAesCipher(requireNonNull(apiV3Key)));
      }
      CertificateProvider certificateProvider = new InMemoryCertificateProvider(certificates);
      return new RSANotificationConfig(
          certificateProvider, new AeadAesCipher(requireNonNull(apiV3Key)));
    }
  }
}
