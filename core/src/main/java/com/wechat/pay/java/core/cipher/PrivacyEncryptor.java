package com.wechat.pay.java.core.cipher;

/** 敏感信息加密器 */
public interface PrivacyEncryptor {
  /**
   * 加密并转换为字符串
   *
   * @param plaintext 明文
   * @return Base64编码的密文
   */
  String encrypt(String plaintext);

  /**
   * 获取加密使用公钥所属证书的证书序列号，可设置到请求的 HTTP 头部 Wechatpay-Serial
   *
   * @return 证书序列号
   */
  String getWechatpaySerial();
}
