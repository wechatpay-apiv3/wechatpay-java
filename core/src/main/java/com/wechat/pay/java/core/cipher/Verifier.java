package com.wechat.pay.java.core.cipher;

/** 验签器 */
public interface Verifier {

  /**
   * 验证签名
   *
   * @param serialNumber 用于验证签名的证书序列号
   * @param message 签名信息
   * @param signature 待验证的签名
   * @return 是否验证通过
   */
  boolean verify(String serialNumber, String message, String signature);
}
