package com.wechat.pay.java.core.http;

import java.net.Proxy;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.http.apache.ApacheHttpClientAdapter;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import com.wechat.pay.java.core.http.okhttp.OkHttpMultiDomainInterceptor;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/** 默认HttpClient构造器 */
public class DefaultHttpClientBuilder
    implements AbstractHttpClientBuilder<DefaultHttpClientBuilder> {

  private Credential credential;
  private Validator validator;

  // 最大空闲连接 maxIdleConnections 为5个，跟 OkHttp 默认值保持一致
  private static final int MAX_IDLE_CONNECTIONS = 5;
  // 连接的 Keep-Alive 空闲时长为7秒, 微信支付服务器目前是8秒
  // 防止空闲客户端被服务器断掉，而造成不必要的重试
  private static final int KEEP_ALIVE_SECONDS = 7;
  private static final okhttp3.OkHttpClient defaultOkHttpClient =
      new OkHttpClient.Builder()
          .connectionPool(
              new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS))
          .build();
  private okhttp3.OkHttpClient customizeOkHttpClient;
  private CloseableHttpClient customizeApacheHttpClient;
  private boolean useApacheHttpClient = false; //
  private int readTimeoutMs = -1;
  private int writeTimeoutMs = -1;
  private int connectTimeoutMs = -1;
  private Proxy proxy;
  private boolean retryMultiDomain = false;
  private Boolean retryOnConnectionFailure = null;
  private static final OkHttpMultiDomainInterceptor multiDomainInterceptor =
      new OkHttpMultiDomainInterceptor();

  private CloseableHttpClient initDefaultApacheHttpClient() {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(MAX_IDLE_CONNECTIONS);
    connectionManager.setDefaultMaxPerRoute(MAX_IDLE_CONNECTIONS);

    RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
  
    if (connectTimeoutMs >= 0) {
      requestConfigBuilder.setConnectTimeout(connectTimeoutMs);
    }
    if (readTimeoutMs >= 0) {
      requestConfigBuilder.setSocketTimeout(readTimeoutMs);
    }
    if (proxy != null) {
      requestConfigBuilder.setProxy(new HttpHost(proxy.address().toString()));
    }

    return HttpClientBuilder.create()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfigBuilder.build())
            .build();
  }

  public DefaultHttpClientBuilder useApacheHttpClient() {
    this.useApacheHttpClient = true;
    return this;
}

  /**
   * 复制工厂，复制一个当前对象
   *
   * @return 对象的副本
   */
  @Override
  public DefaultHttpClientBuilder newInstance() {
    DefaultHttpClientBuilder result = new DefaultHttpClientBuilder();
    result.credential = this.credential;
    result.validator = this.validator;
    result.customizeOkHttpClient = this.customizeOkHttpClient;
    result.customizeApacheHttpClient = this.customizeApacheHttpClient;
    result.readTimeoutMs = this.readTimeoutMs;
    result.writeTimeoutMs = this.writeTimeoutMs;
    result.connectTimeoutMs = this.connectTimeoutMs;
    result.proxy = this.proxy;
    return result;
  }

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
  @Override
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
  @Override
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
    this.useApacheHttpClient = false;
    return this;
  }

    /**
   * 设置 appacheHttpClient 若设置该参数，会覆盖client中的原有配置
   *
   * @param apacheHttpClient 用户自定义的apacheHttpClient
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder apacheHttpClient(CloseableHttpClient apacheHttpClient) {
    this.customizeApacheHttpClient = apacheHttpClient;
    this.useApacheHttpClient = true;
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
   * 启用双域名容灾
   *
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder enableRetryMultiDomain() {
    this.retryMultiDomain = true;
    return this;
  }

  /** OkHttp 在网络问题时不重试 */
  public DefaultHttpClientBuilder disableRetryOnConnectionFailure() {
    this.retryOnConnectionFailure = false;
    return this;
  }

  private AbstractHttpClient buildOkHttpClient() {
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
    if (retryMultiDomain) {
      okHttpClientBuilder.addInterceptor(multiDomainInterceptor);
    }
    if (retryOnConnectionFailure != null && !retryOnConnectionFailure) {
      okHttpClientBuilder.retryOnConnectionFailure(false);
    }
    return new OkHttpClientAdapter(credential, validator, okHttpClientBuilder.build());
  }

  private AbstractHttpClient buildApacheHttpClient() {
    CloseableHttpClient httpclient = customizeApacheHttpClient == null ? initDefaultApacheHttpClient() : customizeApacheHttpClient;
    // 每次都会创建一个 httpclient 实例，和 okhttp 不同
    // You can customize a shared OkH(ttpClient instance with newBuilder(). This builds a client that shares the same connection po)ol, thread pools, and configuration.
    return new ApacheHttpClientAdapter(credential, validator, httpclient);
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

    if (useApacheHttpClient) {
      return buildApacheHttpClient();
    }
    return buildOkHttpClient();
  }
}
