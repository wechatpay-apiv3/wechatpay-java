package com.wechat.pay.java.core.http;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import java.net.URI;

public class OkHttpClientAdapterV2Test implements HttpClientTest {
  @Override
  public HttpClient createHttpClient() {
    Credential credential =
        new Credential() {
          @Override
          public String getSchema() {
            return "foo";
          }

          @Override
          public String getMerchantId() {
            return "1234567890";
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            return "bar";
          }
        };

    Validator validator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            return true;
          }
        };

    return new DefaultHttpClientBuilder().credential(credential).validator(validator).build();
  }

  @Override
  public HttpClient createFalseValidationHttpClient() {
    Credential credential =
        new Credential() {
          @Override
          public String getSchema() {
            return "foo";
          }

          @Override
          public String getMerchantId() {
            return "1234567890";
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            return "bar";
          }
        };

    Validator validator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            return false;
          }
        };

    return new DefaultHttpClientBuilder().credential(credential).validator(validator).build();
  }
}
