package com.wechat.pay.java.core.http.okhttp;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.http.HttpClientBuilder;
import okhttp3.OkHttpClient;

/** OkHttpClientAdapter构造器 */
public class OkHttpClientAdapterBuilder implements HttpClientBuilder {

  private final Credential credential;
  private final okhttp3.OkHttpClient okHttpClient;
  private Validator validator;

  public OkHttpClientAdapterBuilder(
      Credential credential, Validator validator, OkHttpClient okHttpClient) {
    this.credential = requireNonNull(credential);
    this.okHttpClient = requireNonNull(okHttpClient);
    this.validator = requireNonNull(validator);
  }

  @Override
  public HttpClientBuilder validator(Validator validator) {
    this.validator = requireNonNull(validator);
    return this;
  }

  @Override
  public OkHttpClientAdapter build() {
    return new OkHttpClientAdapter(credential, validator, okHttpClient);
  }
}
