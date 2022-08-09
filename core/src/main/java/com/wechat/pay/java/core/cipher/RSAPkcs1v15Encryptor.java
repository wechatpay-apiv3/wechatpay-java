package com.wechat.pay.java.core.cipher;

import java.security.PublicKey;

/** RSA-Pkcs1v15 敏感信息加密器 */
public final class RSAPkcs1v15Encryptor extends AbstractPrivacyEncryptor {

  public RSAPkcs1v15Encryptor(PublicKey publicKey, String wechatpaySerialNumber) {
    super("RSA/ECB/PKCS1Padding", publicKey, wechatpaySerialNumber);
  }
}
