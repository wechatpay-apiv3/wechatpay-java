package com.wechat.pay.java.core.notification;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

public class RSAAutoCertificateNotificationConfig implements NotificationConfig {

  public static final String RSA_SIGN_TYPE = "WECHATPAY2-SHA256-RSA2048";
  private static final String CIPHER_ALGORITHM = "AEAD_AES_256_GCM";
  private final CertificateProvider certificateProvider;
  private final byte[] apiV3Key;

  private RSAAutoCertificateNotificationConfig(
      CertificateProvider certificateProvider, byte[] apiV3Key) {
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

    private String merchantId;
    private String merchantSerialNumber;
    private PrivateKey privateKey;

    private byte[] apiV3Key;

    HttpClient httpClient;

    public Builder merchantId(String merchantId) {
      this.merchantId = merchantId;
      return this;
    }

    public Builder privateKey(String privateKeyStr) {
      this.privateKey = PemUtil.loadPrivateKeyFromString(privateKeyStr);
      return this;
    }

    public Builder privateKeyFromPath(String privateKeyPath) {
      this.privateKey = PemUtil.loadPrivateKeyFromPath(privateKeyPath);
      return this;
    }

    public Builder merchantSerialNumber(String merchantSerialNumber) {
      this.merchantSerialNumber = merchantSerialNumber;
      return this;
    }

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public RSAAutoCertificateNotificationConfig build() {
      RSAAutoCertificateProvider.Builder builder =
          new RSAAutoCertificateProvider.Builder()
              .apiV3Key(requireNonNull(apiV3Key))
              .privateKey(requireNonNull(privateKey))
              .merchantId(requireNonNull(merchantId))
              .merchantSerialNumber(requireNonNull(merchantSerialNumber));
      if (httpClient != null) {
        builder.httpClient(httpClient);
      }
      return new RSAAutoCertificateNotificationConfig(builder.build(), apiV3Key);
    }
  }
}
