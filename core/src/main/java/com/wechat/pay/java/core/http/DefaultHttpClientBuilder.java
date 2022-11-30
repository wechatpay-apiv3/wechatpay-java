package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/** 默认HttpClient构造器 */
public class DefaultHttpClientBuilder implements HttpClientBuilder {

  private Credential credential;
  private Validator validator;
  private static final okhttp3.OkHttpClient defaultOkHttpClient = new okhttp3.OkHttpClient();
  private okhttp3.OkHttpClient customizeOkHttpClient;
  private int readTimeoutMs = -1;
  private int writeTimeoutMs = -1;
  private int connectTimeoutMs = -1;
  private Proxy proxy;

  /**
   * 设置读超时
   *
   * @param readTimeoutMs 读超时，单位毫秒
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder readTimeoutMs(int readTimeoutMs) {
    this.readTimeoutMs = readTimeoutMs;
    return this;
  }

  /**
   * 设置写超时
   *
   * @param writeTimeoutMs 写超时，单位毫秒
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder writeTimeoutMs(int writeTimeoutMs) {
    this.writeTimeoutMs = writeTimeoutMs;
    return this;
  }

  /**
   * 设置连接超时
   *
   * @param connectTimeoutMs 连接超时，单位毫秒
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder connectTimeoutMs(int connectTimeoutMs) {
    this.connectTimeoutMs = connectTimeoutMs;
    return this;
  }

  /**
   * 设置凭据生成器
   *
   * @param credential 凭据生成器
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder credential(Credential credential) {
    this.credential = credential;
    return this;
  }

  /**
   * 设置验证器
   *
   * @param validator 验证器
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder validator(Validator validator) {
    this.validator = validator;
    return this;
  }

  /**
   * 设置 okHttpClient 若设置该参数，会覆盖client中的原有配置
   *
   * @param okHttpClient 用户自定义的okHttpClient
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder okHttpClient(okhttp3.OkHttpClient okHttpClient) {
    this.customizeOkHttpClient = okHttpClient;
    return this;
  }

  public DefaultHttpClientBuilder config(Config config) {
    requireNonNull(config);
    this.credential = config.createCredential();
    this.validator = config.createValidator();
    return this;
  }

  public DefaultHttpClientBuilder proxy(Proxy proxy) {
    requireNonNull(proxy);
    this.proxy = proxy;
    return this;
  }
  /**
   * 构建默认HttpClient
   *
   * @return httpClient
   */
  public HttpClient build() {
    requireNonNull(credential);
    requireNonNull(validator);
    okhttp3.OkHttpClient.Builder okHttpClientBuilder =
        (customizeOkHttpClient == null ? defaultOkHttpClient : customizeOkHttpClient).newBuilder();
    if (connectTimeoutMs >= 0) {
      okHttpClientBuilder.connectTimeout(connectTimeoutMs, TimeUnit.MILLISECONDS);
    }
    if (readTimeoutMs >= 0) {
      okHttpClientBuilder.readTimeout(readTimeoutMs, TimeUnit.MILLISECONDS);
    }
    if (writeTimeoutMs >= 0) {
      okHttpClientBuilder.writeTimeout(writeTimeoutMs, TimeUnit.MILLISECONDS);
    }
    if (proxy != null) {
      okHttpClientBuilder.proxy(proxy);
    }
    return new OkHttpClientAdapter(credential, validator, okHttpClientBuilder.build());
  }
}
