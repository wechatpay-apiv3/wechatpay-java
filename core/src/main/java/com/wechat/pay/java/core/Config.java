package com.wechat.pay.java.core;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;

/** 调用微信支付服务的所需配置 */
public interface Config {

  /**
   * 创建敏感信息加密器
   *
   * @return 敏感信息加密器
   */
  PrivacyEncryptor createEncryptor();

  /**
   * 创建敏感信息解密器
   *
   * @return 敏感信息解密器
   */
  PrivacyDecryptor createDecryptor();

  /**
   * 创建认证凭据生成器
   *
   * @return 认证凭据生成器
   */
  Credential createCredential();

  /**
   * 创建请求验证器
   *
   * @return 请求验证器
   */
  Validator createValidator();

  /**
   * 获取请求的base url，默认为：https://api.mch.weixin.qq.com
   *
   * @return base url
   */
  String getBaseUrl();
}
