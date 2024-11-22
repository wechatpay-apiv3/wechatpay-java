package com.wechat.pay.java.core.http;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import java.net.URI;

public class ApacheHttpClientAdapterV2Test implements HttpClientTest {
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

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    return new DefaultHttpClientBuilder().credential(credential).validator(validator).useApacheHttpClient().build();
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

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    return new DefaultHttpClientBuilder().credential(credential).validator(validator).useApacheHttpClient().build();
  }
}
