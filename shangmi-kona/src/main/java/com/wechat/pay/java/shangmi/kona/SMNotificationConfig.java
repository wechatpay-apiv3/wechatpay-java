package com.wechat.pay.java.shangmi.kona;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.notification.NotificationConfig;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SMNotificationConfig implements NotificationConfig {
  public static final String SIGN_TYPE = "WECHATPAY2-SM2-WITH-SM3";
  public static final String CIPHER_ALGORITHM = "AEAD_SM4_GCM";
  private static final int API_V3_KEY_LENGTH_BYTE = 16;
  private final CertificateProvider certificateProvider;
  private final byte[] apiV3Key;

  private SMNotificationConfig(CertificateProvider certificateProvider, byte[] apiV3Key) {
    this.certificateProvider = certificateProvider;
    this.apiV3Key = apiV3Key;
  }

  @Override
  public String getSignType() {
    return SIGN_TYPE;
  }

  @Override
  public String getCipherType() {
    return CIPHER_ALGORITHM;
  }

  @Override
  public Verifier createVerifier() {
    return new SM2Verifier(certificateProvider);
  }

  @Override
  public AeadCipher createAeadCipher() {
    return new AeadSM4Cipher(apiV3Key);
  }

  public static class Builder {

    private List<X509Certificate> certificates;
    private byte[] apiV3Key;

    public Builder certificates(X509Certificate... certificates) {
      if (certificates == null || certificates.length == 0) {
        throw new IllegalArgumentException(
            "The parameter certificates of building SMNotificationConfig is empty.");
      }
      this.certificates = Arrays.asList(certificates);
      return this;
    }

    public Builder certificates(String... certificates) {
      if (certificates == null || certificates.length == 0) {
        throw new IllegalArgumentException(
            "The parameter certificates of building SMNotificationConfig is empty.");
      }
      List<X509Certificate> certificateList = new ArrayList<>();
      for (String certificateString : certificates) {
        X509Certificate x509Certificate = SMPemUtil.loadX509FromString(certificateString);
        certificateList.add(x509Certificate);
      }
      this.certificates = certificateList;
      return this;
    }

    public Builder certificatesFromPath(String... certificatePaths) {
      if (certificatePaths == null || certificatePaths.length == 0) {
        throw new IllegalArgumentException(
            "The parameter certificatePaths of building SMNotificationConfig is empty.");
      }
      List<X509Certificate> certificateList = new ArrayList<>();
      for (String certificatePath : certificatePaths) {
        X509Certificate x509Certificate = SMPemUtil.loadX509FromPath(certificatePath);
        certificateList.add(x509Certificate);
      }
      this.certificates = certificateList;
      return this;
    }

    public Builder apiV3Key(String apiV3Key) {
      if (requireNonNull(apiV3Key).length() != API_V3_KEY_LENGTH_BYTE) {
        throw new IllegalArgumentException(
            "The parameter apiV3Key of building SMNotificationConfig is empty.");
      }
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public SMNotificationConfig build() {
      if (this.certificates == null) {
        throw new IllegalArgumentException(
            "The parameter certificates of building SMNotificationConfig is empty.");
      }
      if (this.apiV3Key == null) {
        throw new IllegalArgumentException(
            "The parameter apiV3Key of building SMNotificationConfig is empty.");
      }
      return new SMNotificationConfig(
          new InMemoryCertificateProvider(requireNonNull(certificates)), apiV3Key);
    }
  }
}
