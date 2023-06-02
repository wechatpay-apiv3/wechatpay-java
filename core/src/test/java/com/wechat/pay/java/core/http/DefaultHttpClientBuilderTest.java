package com.wechat.pay.java.core.http;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.Signer;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

class DefaultHttpClientBuilderTest {

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
      };

  OkHttpClient okHttpClient = new OkHttpClient();

  @Test
  void buildWithCredentialAndValidator() {
    HttpClient httpClient =
        new DefaultHttpClientBuilder()
            .credential(credential)
            .validator(validator)
            .okHttpClient(okHttpClient)
            .proxy(new Proxy(Type.SOCKS, new InetSocketAddress("localhost", 8099)))
            .enableRetryMultiDomain()
            .disableRetryOnConnectionFailure()
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
        new DefaultHttpClientBuilder().config(config).okHttpClient(okHttpClient).build();
    assertNotNull(httpClient);
  }
}
