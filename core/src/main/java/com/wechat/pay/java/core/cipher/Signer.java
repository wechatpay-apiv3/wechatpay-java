package com.wechat.pay.java.core.cipher;

/** 签名器 */
public interface Signer {

  /**
   * 生成签名结果
   *
   * @param message 签名信息
   * @return 签名结果
   */
  SignatureResult sign(String message);

  /**
   * 获取签名算法
   *
   * @return 签名算法
   */
  String getAlgorithm();
}
