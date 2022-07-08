package com.wechat.pay.java.core.notification;

import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.Verifier;

/** 用于解析通知的配置 */
public interface NotificationConfig {

  /**
   * 获取签名类型
   *
   * @return 签名类型
   */
  String getSignType();

  /**
   * 获取认证加解密器类型
   *
   * @return 认证加解密器类型
   */
  String getCipherType();

  /**
   * 创建验签器
   *
   * @return 验签器
   */
  Verifier createVerifier();

  /**
   * 创建认证加解密器
   *
   * @return 认证加解密器
   */
  AeadCipher createAeadCipher();
}
