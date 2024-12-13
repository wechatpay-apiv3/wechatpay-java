package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.http.apache.ApacheHttpClientAdapter;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/** 默认HttpClient构造器 */
public class ApacheHttpClientBuilder implements AbstractHttpClientBuilder<ApacheHttpClientBuilder> {

  private Credential credential;
  private Validator validator;

  private CloseableHttpClient customizeApacheHttpClient;

  static PoolingHttpClientConnectionManager apacheHttpClientConnectionManager =
      new PoolingHttpClientConnectionManager();

  private CloseableHttpClient initDefaultApacheHttpClient() {
    return HttpClientBuilder.create()
        .setConnectionManager(apacheHttpClientConnectionManager)
        .setConnectionManagerShared(true)
        .build();
  }

  /**
   * 复制工厂，复制一个当前对象
   *
   * @return 对象的副本
   */
  @Override
  public ApacheHttpClientBuilder newInstance() {
    ApacheHttpClientBuilder result = new ApacheHttpClientBuilder();
    result.credential = this.credential;
    result.validator = this.validator;
    result.customizeApacheHttpClient = this.customizeApacheHttpClient;
    return result;
  }

  /**
   * 设置凭据生成器
   *
   * @param credential 凭据生成器
   * @return apacheHttpClientBuilder
   */
  @Override
  public ApacheHttpClientBuilder credential(Credential credential) {
    this.credential = credential;
    return this;
  }

  /**
   * 设置验证器
   *
   * @param validator 验证器
   * @return apacheHttpClientBuilder
   */
  @Override
  public ApacheHttpClientBuilder validator(Validator validator) {
    this.validator = validator;
    return this;
  }

  /**
   * 设置 appacheHttpClient，若没有设置，则使用默认创建的 appacheHttpClient
   *
   * @param apacheHttpClient 用户自定义的apacheHttpClient
   * @return apacheHttpClientBuilder
   */
  public ApacheHttpClientBuilder apacheHttpClient(CloseableHttpClient apacheHttpClient) {
    this.customizeApacheHttpClient = apacheHttpClient;
    return this;
  }

  public ApacheHttpClientBuilder config(Config config) {
    requireNonNull(config);
    this.credential = config.createCredential();
    this.validator = config.createValidator();
    return this;
  }

  /**
   * 构建默认HttpClient
   *
   * @return httpClient
   */
  @Override
  public AbstractHttpClient build() {
    requireNonNull(credential);
    requireNonNull(validator);

    CloseableHttpClient httpclient =
        customizeApacheHttpClient == null
            ? initDefaultApacheHttpClient()
            : customizeApacheHttpClient;
    return new ApacheHttpClientAdapter(credential, validator, httpclient);
  }
}
