package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.model.TestConfig.*;
import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.*;

import com.wechat.pay.java.core.RSAAutoCertificateConfig.Builder;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import com.wechat.pay.java.core.util.NonceUtil;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.stream.Stream;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RSAAutoCertificateConfigTest implements ConfigTest {

  static HttpClient httpClient;
  static OkHttpClient okHttpClient;

  @BeforeAll
  static void initHttpClient() {
    Validator validator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            return true;
          }
        };
    okHttpClient =
        new OkHttpClient.Builder()
            .addInterceptor(
                chain ->
                    new Response.Builder()
                        .request(chain.request())
                        .code(HTTP_OK)
                        .header("key", "val")
                        .message("ok")
                        .protocol(Protocol.HTTP_1_1)
                        .body(
                            ResponseBody.create(
                                DOWNLOAD_CERTIFICATE_RESPONSE,
                                MediaType.parse(
                                    com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                        .getValue())))
                        .build())
            .build();
    httpClient =
        new OkHttpClientAdapter(
            new WechatPay2Credential(
                "8123456", new RSASigner(MERCHANT_CERTIFICATE_SERIAL_NUMBER, MERCHANT_PRIVATE_KEY)),
            validator,
            okHttpClient);
  }

  @ParameterizedTest
  @MethodSource("BuilderProvider")
  void testConfigWithBuilderProvider(Builder builder) {
    RSAAutoCertificateConfig config = builder.build();
    assertNotNull(config.createValidator());
    assertNotNull(config.createCredential());
    assertNotNull(config.createEncryptor());
    assertNotNull(config.createDecryptor());
    assertNotNull(config.createAeadCipher());
    assertNotNull(config.createVerifier());

    assertEquals(RSA_SIGN_TYPE, config.getSignType());
    assertEquals(AES_CIPHER_ALGORITHM, config.getCipherType());
  }

  static Stream<Builder> BuilderProvider() {
    return Stream.of(
        // from string
        new Builder()
            .merchantId("123456")
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .httpClient(httpClient)
            .apiV3Key(API_V3_KEY),

        // from path
        new Builder()
            .merchantId("223456")
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .httpClient(httpClient)
            .apiV3Key(API_V3_KEY),

        // with http client builder
        new Builder()
            .merchantId("1123456")
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY)
            .httpClientBuilder(
                new DefaultHttpClientBuilder().writeTimeoutMs(1000).okHttpClient(okHttpClient)));
  }

  @Test
  void testBuildConfigWithoutEnoughParam() {
    RSAAutoCertificateConfig.Builder builder =
        new Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER);
    assertThrows(NullPointerException.class, builder::build);
  }

  @Override
  public Config createConfig() {
    return new RSAAutoCertificateConfig.Builder()
        .apiV3Key(API_V3_KEY)
        .merchantId(NonceUtil.createNonce(6))
        .privateKey(MERCHANT_PRIVATE_KEY)
        .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
        .httpClient(httpClient)
        .build();
  }

  @Disabled("only available in production environment")
  @Test
  void testAutoCertificateWithProxy() {
    DefaultHttpClientBuilder clientBuilder =
        new DefaultHttpClientBuilder()
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 12639)));

    RSAAutoCertificateConfig config =
        new Builder()
            .merchantId("")
            .privateKey("")
            .merchantSerialNumber("")
            .httpClientBuilder(clientBuilder)
            .apiV3Key("")
            .build();

    assertNotNull(config.createValidator());
  }
}
