package com.wechat.pay.java.core.http;

import com.wechat.pay.java.core.auth.Validator;

/** HttpClient构造器，可以返回一个新的HttpClient实例 */
public interface HttpClientBuilder {

  /**
   * 更新构造器的验证器
   *
   * @param validator 验证器
   * @return 构造器
   */
  HttpClientBuilder updateValidator(Validator validator);

  /**
   * 使用builder的配置构造一个新的HttpClient
   *
   * @return httpClient
   */
  HttpClient build();
}
