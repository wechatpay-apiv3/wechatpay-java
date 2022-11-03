package com.wechat.pay.java.shangmi;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.notification.NotificationConfig;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class SMNotificationConfig implements NotificationConfig {
  public static final String SIGN_TYPE = "WECHATPAY2-SM2-WITH-SM3";
  public static final String CIPHER_ALGORITHM = "AEAD_SM4_GCM";
  private static final int API_V3_KEY_LENGTH_BYTE = 32;
  private final CertificateProvider certificateProvider;
  private final byte[] apiV3Key;

  private SMNotificationConfig(Builder builder) {
    this.certificateProvider = requireNonNull(builder.certificateProvider);
    this.apiV3Key = requireNonNull(builder.apiV3Key);
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

    private CertificateProvider certificateProvider;

    private List<X509Certificate> certificates = new ArrayList<>();
    private byte[] apiV3Key;

    /**
     * 移除之前的微信支付平台证书，加入新的证书
     *
     * @param wechatpayCertificates 微信支付平台证书列表
     * @return Builder
     */
    public Builder wechatPayCertificates(List<X509Certificate> wechatpayCertificates) {
      this.certificates = wechatpayCertificates;
      return this;
    }

    /**
     * 设置微信支付平台证书提供器
     *
     * @param certificateProvider 微信支付平台证书提供器
     * @return Builder
     */
    public Builder wechatPayCertificateProvider(CertificateProvider certificateProvider) {
      this.certificateProvider = certificateProvider;
      return this;
    }

    /**
     * 添加一个微信支付平台证书对象
     *
     * @param certificate 微信支付平台证书对象
     * @return Builder
     */
    public Builder addWechatPayCertificate(X509Certificate certificate) {
      certificates.add(certificate);
      return this;
    }

    /**
     * 添加一个字符串的微信支付平台证书
     *
     * @param certificate 微信支付平台证书
     * @return Builder
     */
    public Builder addWechatPayCertificate(String certificate) {
      return addWechatPayCertificate(SMPemUtil.loadX509FromString(certificate));
    }

    /**
     * 设置APIv3密钥
     *
     * @param apiV3Key APIv3密钥
     * @return Builder
     */
    public Builder apiV3Key(String apiV3Key) {
      if (apiV3Key.length() != API_V3_KEY_LENGTH_BYTE) {
        throw new IllegalArgumentException(
            "The length of apiV3Key is invalid, it should be 32 bytes.");
      }
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public SMNotificationConfig build() {
      if (certificateProvider == null) {
        if (certificates.isEmpty()) {
          throw new IllegalArgumentException(
              "neither certificate provider nor certificates must not be empty");
        }
        certificateProvider = new InMemoryCertificateProvider(certificates);
      }
      return new SMNotificationConfig(this);
    }
  }
}
