package com.wechat.pay.java.shangmi;

import static com.wechat.pay.java.core.cipher.Constant.HEX;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.Signer;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/** 国密配置 */
public final class SMConfig implements Config {

  /** 商户号 */
  private final String merchantId;
  /** 商户私钥 */
  private final PrivateKey privateKey;
  /** 商户证书序列号 */
  private final String merchantSerialNumber;
  /** 微信支付平台证书Provider */
  private final CertificateProvider certificateProvider;

  private SMConfig(Builder builder) {
    this.merchantId = requireNonNull(builder.merchantId);
    this.privateKey = requireNonNull(builder.privateKey);
    this.merchantSerialNumber = requireNonNull(builder.merchantSerialNumber);
    this.certificateProvider = requireNonNull(builder.certificateProvider);
  }

  /**
   * 创建敏感信息加密器
   *
   * @return 敏感信息加密器
   */
  @Override
  public PrivacyEncryptor createEncryptor() {
    X509Certificate certificate = certificateProvider.getAvailableCertificate();
    return new SM2PrivacyEncryptor(
        certificate.getPublicKey(), certificate.getSerialNumber().toString(HEX));
  }

  /**
   * 创建敏感信息解密器
   *
   * @return 敏感信息解密器
   */
  @Override
  public PrivacyDecryptor createDecryptor() {
    return new SM2PrivacyDecryptor(privateKey);
  }

  /**
   * 创建认证凭据生成器
   *
   * @return 认证凭据生成器
   */
  @Override
  public Credential createCredential() {
    return new WechatPay2Credential(merchantId, new SM2Signer(merchantSerialNumber, privateKey));
  }

  /**
   * 创建请求验证器
   *
   * @return 请求验证器
   */
  @Override
  public Validator createValidator() {
    return new WechatPay2Validator(new SM2Verifier(certificateProvider));
  }

  /**
   * 创建签名器
   *
   * @return 签名器
   */
  @Override
  public Signer createSigner() {
    return new SM2Signer(merchantSerialNumber, privateKey);
  }

  public static class Builder {

    private String merchantId;
    private PrivateKey privateKey;
    private String merchantSerialNumber;
    private List<X509Certificate> wechatPayCertificates = new ArrayList<>();
    private CertificateProvider certificateProvider;

    public Builder merchantId(String merchantId) {
      this.merchantId = merchantId;
      return this;
    }

    public Builder privateKey(String privateKey) {
      return privateKey(SMPemUtil.loadPrivateKeyFromString(privateKey));
    }

    public Builder privateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
      return this;
    }

    /**
     * 设置私钥，从指定的本地私钥文件获得
     *
     * @param privateKeyPath 本地私钥文件路径
     * @return Builder
     */
    public Builder privateKeyFromPath(String privateKeyPath) {
      return privateKey(SMPemUtil.loadPrivateKeyFromPath(privateKeyPath));
    }

    /**
     * 设置商户API证书序列号
     *
     * @param merchantSerialNumber 商户API证书序列号
     * @return Builder
     */
    public Builder merchantSerialNumber(String merchantSerialNumber) {
      this.merchantSerialNumber = merchantSerialNumber;
      return this;
    }

    /**
     * 设置微信支付平台证书提供器
     *
     * @param provider 微信支付平台证书提供器
     * @return Builder
     */
    public Builder wechatPayCertificateProvider(CertificateProvider provider) {
      this.certificateProvider = provider;
      return this;
    }

    /**
     * 添加一个微信支付平台证书对象
     *
     * @param certificate 微信支付平台证书对象
     * @return Builder
     */
    public Builder addWechatPayCertificate(X509Certificate certificate) {
      wechatPayCertificates.add(certificate);
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
     * 添加一个微信支付平台证书,从指定的本地证书文件
     *
     * @param certificatePath 微信支付平台证书路径
     * @return Builder
     */
    public Builder addWechatPayCertificateFromPath(String certificatePath) {
      return addWechatPayCertificate(SMPemUtil.loadX509FromPath(certificatePath));
    }

    /**
     * 移除所有的微信支付平台证书，加入新的证书列表
     *
     * @param wechatPayCertificates 微信支付平台证书列表
     * @return Builder
     */
    public Builder wechatPayCertificates(List<X509Certificate> wechatPayCertificates) {
      this.wechatPayCertificates = wechatPayCertificates;
      return this;
    }

    public SMConfig build() {
      if (certificateProvider == null) {
        wechatPayCertificateProvider(new InMemoryCertificateProvider(wechatPayCertificates));
      }
      return new SMConfig(this);
    }
  }
}
