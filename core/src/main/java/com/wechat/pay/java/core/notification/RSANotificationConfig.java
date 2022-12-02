package com.wechat.pay.java.core.notification;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider;
import com.wechat.pay.java.core.cipher.*;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 签名类型为RSA的通知配置参数 */
public final class RSANotificationConfig implements NotificationConfig {

  public static final String RSA_SIGN_TYPE = "WECHATPAY2-SHA256-RSA2048";
  private static final String CIPHER_ALGORITHM = "AEAD_AES_256_GCM";
  private final CertificateProvider certificateProvider;
  private final byte[] apiV3Key;

  private RSANotificationConfig(CertificateProvider certificateProvider, byte[] apiV3Key) {
    this.certificateProvider = certificateProvider;
    this.apiV3Key = apiV3Key;
  }

  @Override
  public String getSignType() {
    return RSA_SIGN_TYPE;
  }

  @Override
  public String getCipherType() {
    return CIPHER_ALGORITHM;
  }

  @Override
  public Verifier createVerifier() {
    return new RSAVerifier(certificateProvider);
  }

  @Override
  public AeadCipher createAeadCipher() {
    return new AeadAesCipher(apiV3Key);
  }

  public static class Builder {

    private List<X509Certificate> certificates;
    private byte[] apiV3Key;
    private String merchantId;
    private PrivateKey privateKey;
    private String merchantSerialNumber;

    private CertificateProvider certificateProvider;

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

    public Builder merchantId(String merchantId) {
      this.merchantId = merchantId;
      return this;
    }

    public Builder autoUpdateCertWithKeyStr(String privateKeyStr) {
      this.privateKey = PemUtil.loadPrivateKeyFromString(privateKeyStr);
      return this;
    }

    public Builder autoUpdateCertWithKeyPath(String privateKeyPath) {
      this.privateKey = PemUtil.loadPrivateKeyFromPath(privateKeyPath);
      return this;
    }

    public Builder merchantSerialNumber(String merchantSerialNumber) {
      this.merchantSerialNumber = merchantSerialNumber;
      return this;
    }

    public Builder certificateProvider(CertificateProvider certificateProvider) {
      this.certificateProvider = certificateProvider;
      return this;
    }

    public RSANotificationConfig build() {
      requireNonNull(apiV3Key);
      if (certificateProvider != null) {
        return new RSANotificationConfig(certificateProvider, apiV3Key);
      }
      if (privateKey == null && (certificates == null || certificates.isEmpty())) {
        throw new IllegalArgumentException("One of privateKey and certificates must be set.");
      }
      if (privateKey != null && certificates != null && !certificates.isEmpty()) {
        throw new IllegalArgumentException("Only One of privateKey and certificates can be set.");
      }
      if (privateKey != null) {
        // 使用自动更新证书提供器
        certificateProvider =
            new RSAAutoCertificateProvider.Builder()
                .merchantId(requireNonNull(merchantId))
                .apiV3Key(apiV3Key)
                .privateKey(requireNonNull(privateKey))
                .merchantSerialNumber(requireNonNull(merchantSerialNumber))
                .build();
        return new RSANotificationConfig(certificateProvider, apiV3Key);
      }
      return new RSANotificationConfig(
          new InMemoryCertificateProvider(requireNonNull(certificates)), apiV3Key);
    }
  }
}
