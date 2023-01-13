package com.wechat.pay.java.core.http;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;

public interface AbstractHttpClientBuilder<T extends AbstractHttpClientBuilder<T>> {

  /**
   * 设置验证器
   *
   * @param validator 验证器
   * @return T the AbstractHttpClientBuilder
   */
  T validator(Validator validator);

  /**
   * 设置凭据生成器
   *
   * @param credential 凭据生成器
   * @return T the AbstractHttpClientBuilder
   */
  T credential(Credential credential);

  /**
   * * 构建 AbstractHttpClient
   *
   * @return AbstractHttpClient
   */
  AbstractHttpClient build();
}
