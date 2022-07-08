package com.wechat.pay.java.core.cipher;

/** 敏感信息解密器 */
public interface PrivacyDecryptor {

  /**
   * 解密并转换为字符串
   *
   * @param ciphertext 密文
   * @return UTF-8编码的明文
   */
  String decrypt(String ciphertext);
}
