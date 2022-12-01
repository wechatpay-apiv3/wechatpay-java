package com.wechat.pay.java.core.cipher;

import java.security.PublicKey;

/** RSA敏感信息加密器 */
public final class RSAPrivacyEncryptor extends AbstractPrivacyEncryptor {

  /**
   * RSAPrivacyEncryptor 构造函数
   *
   * @param publicKey 请求的敏感信息加密时使用的微信支付公钥
   * @param wechatpaySerialNumber 微信支付平台证书的证书序列号
   */
  public RSAPrivacyEncryptor(PublicKey publicKey, String wechatpaySerialNumber) {
    super("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", publicKey, wechatpaySerialNumber);
  }
}
