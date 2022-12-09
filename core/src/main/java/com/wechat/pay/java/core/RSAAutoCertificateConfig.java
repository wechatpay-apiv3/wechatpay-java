package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.cipher.Constant.HEX;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.RSAPrivacyDecryptor;
import com.wechat.pay.java.core.cipher.RSAPrivacyEncryptor;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.util.PemUtil;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/** 具有自动下载平台证书能力的RSA配置类 */
public class RSAAutoCertificateConfig implements Config {

  /** 商户号 */
  private final String merchantId;
  /** 商户私钥 */
  private final PrivateKey privateKey;
  /** 商户证书序列号 */
  private final String merchantSerialNumber;
  /** 微信支付平台证书Provider */
  private final CertificateProvider certificateProvider;

  private RSAAutoCertificateConfig(
      String merchantId,
      PrivateKey privateKey,
      String merchantSerialNumber,
      CertificateProvider certificateProvider) {
    this.merchantId = merchantId;
    this.privateKey = privateKey;
    this.merchantSerialNumber = merchantSerialNumber;
    this.certificateProvider = certificateProvider;
  }

  @Override
  public PrivacyEncryptor createEncryptor() {
    X509Certificate certificate = certificateProvider.getAvailableCertificate();
    return new RSAPrivacyEncryptor(
        certificate.getPublicKey(), certificate.getSerialNumber().toString(HEX));
  }

  @Override
  public PrivacyDecryptor createDecryptor() {
    return new RSAPrivacyDecryptor(privateKey);
  }

  @Override
  public Credential createCredential() {
    return new WechatPay2Credential(merchantId, createSigner());
  }

  @Override
  public Validator createValidator() {
    return new WechatPay2Validator(new RSAVerifier(certificateProvider));
  }

  @Override
  public Signer createSigner() {
    return new RSASigner(merchantSerialNumber, privateKey);
  }

  public static class Builder {

    private String merchantId;
    private PrivateKey privateKey;
    private String merchantSerialNumber;
    private HttpClient httpClient;

    private byte[] apiV3Key;

    private Proxy proxy;

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

    public Builder apiV3Key(String apiV3key) {
      this.apiV3Key = apiV3key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder proxy(Proxy proxy) {
      this.proxy = proxy;
      return this;
    }

    public RSAAutoCertificateConfig build() {
      RSAAutoCertificateProvider.Builder providerBuilder =
          new RSAAutoCertificateProvider.Builder()
              .merchantId(requireNonNull(merchantId))
              .apiV3Key(requireNonNull(apiV3Key))
              .privateKey(requireNonNull(privateKey))
              .merchantSerialNumber(requireNonNull(merchantSerialNumber));
      if (httpClient != null && proxy != null) {
        throw new IllegalArgumentException(
            "Only one of httpClient() and proxy() method can be called.");
      }
      if (proxy != null) {
        initHttpClientWithProxy();
      }
      if (httpClient != null) {
        providerBuilder.httpClient(httpClient);
      }
      return new RSAAutoCertificateConfig(
          merchantId, privateKey, merchantSerialNumber, providerBuilder.build());
    }

    private void initHttpClientWithProxy() {
      Credential credential =
          new WechatPay2Credential(merchantId, new RSASigner(merchantSerialNumber, privateKey));
      Validator validator = new WechatPay2Validator((serialNumber, message, signature) -> true);
      httpClient =
          new DefaultHttpClientBuilder()
              .proxy(proxy)
              .credential(credential)
              .validator(validator)
              .build();
    }
  }
}
