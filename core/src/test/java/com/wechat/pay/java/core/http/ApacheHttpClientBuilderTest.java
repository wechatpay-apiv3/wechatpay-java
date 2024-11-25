package com.wechat.pay.java.core.http;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.Signer;
import okhttp3.OkHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApacheHttpClientBuilderTest {

  Credential credential =
      new Credential() {
        @Override
        public String getSchema() {
          return "schema";
        }

        @Override
        public String getMerchantId() {
          return "123456";
        }

        @Override
        public String getAuthorization(URI uri, String httpMethod, String signBody) {
          return "authorization";
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

  CloseableHttpClient apacheHttpClient = HttpClientBuilder.create().build();

  @Test
  void buildWithCredentialAndValidator() {
    HttpClient httpClient =
        new ApacheHttpClientBuilder()
            .credential(credential)
            .validator(validator)
            .apacheHttpClient(apacheHttpClient)
            .build();
    assertNotNull(httpClient);
  }

  @Test
  void buildWithConfig() {
    Config config =
        new Config() {
          @Override
          public PrivacyEncryptor createEncryptor() {
            return null;
          }

          @Override
          public PrivacyDecryptor createDecryptor() {
            return null;
          }

          @Override
          public Credential createCredential() {
            return credential;
          }

          @Override
          public Validator createValidator() {
            return validator;
          }

          @Override
          public Signer createSigner() {
            return null;
          }
        };
    HttpClient httpClient =
        new ApacheHttpClientBuilder().config(config).build();
    assertNotNull(httpClient);
  }
}
