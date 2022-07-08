package com.wechat.pay.java.core.auth;

import java.net.URI;

/** 认证凭据生成器 */
public interface Credential {

  /**
   * 获取认证类型
   *
   * @return 认证类型
   */
  String getSchema();

  /**
   * 获取商户号
   *
   * @return 商户号
   */
  String getMerchantId();

  /**
   * 获取认证信息
   *
   * @param uri 请求uri
   * @param httpMethod HTTP方法，GET、POST等
   * @param signBody 用于签名的请求体
   * @return 认证信息
   */
  String getAuthorization(URI uri, String httpMethod, String signBody);
}
