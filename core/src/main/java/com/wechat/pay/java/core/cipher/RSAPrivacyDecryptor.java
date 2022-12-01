package com.wechat.pay.java.core.cipher;

import java.security.PrivateKey;

/** RSA敏感信息解密器 */
public final class RSAPrivacyDecryptor extends AbstractPrivacyDecryptor {

  /**
   * RSAPrivacyDecryptor 构造函数
   *
   * @param privateKey 应答的敏感信息解密时使用的商户私钥
   */
  public RSAPrivacyDecryptor(PrivateKey privateKey) {
    super("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", privateKey);
  }
}
