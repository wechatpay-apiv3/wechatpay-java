package com.wechat.pay.java.core.http;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;

public interface AbstractHttpClientBuilder<T extends AbstractHttpClientBuilder<T>> {

  /**
   * 复制工厂，复制一个当前对象
   *
   * @return 对象的副本
   */
  T newInstance();

  /**
   * 设置验证器
   *
   * @param validator 验证器
   * @return the AbstractHttpClientBuilder
   */
  T validator(Validator validator);

  /**
   * 设置凭据生成器
   *
   * @param credential 凭据生成器
   * @return the AbstractHttpClientBuilder
   */
  T credential(Credential credential);

  /**
   * 构建 AbstractHttpClient
   *
   * @return AbstractHttpClient
   */
  AbstractHttpClient build();
}
