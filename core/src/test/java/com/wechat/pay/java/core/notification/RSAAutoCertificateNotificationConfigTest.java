package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.model.TestConfig.*;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import com.wechat.pay.java.core.notification.AutoCertificateNotificationConfig.Builder;
import java.util.stream.Stream;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RSAAutoCertificateNotificationConfigTest {
  static HttpClient httpClient;

  @BeforeAll
  static void initHttpClient() {
    Validator validator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            return true;
          }
        };
    OkHttpClient okHttpClient =
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
                MERCHANT_ID,
                new RSASigner(MERCHANT_CERTIFICATE_SERIAL_NUMBER, MERCHANT_PRIVATE_KEY)),
            validator,
            okHttpClient);
  }

  @ParameterizedTest
  @MethodSource("BuilderProvider")
  void testConfigWithBuilderProvider(Builder builder) {
    NotificationConfig config = builder.build();
    assertNotNull(config.createAeadCipher());
    assertNotNull(config.createVerifier());
  }

  static Stream<Builder> BuilderProvider() {
    return Stream.of(
        // from string
        new Builder()
            .merchantId("323456")
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY)
            .httpClient(httpClient)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING),

        // form path
        new Builder()
            .merchantId("423456")
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .httpClient(httpClient)
            .apiV3Key(API_V3_KEY)
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH));
  }

  @Test
  void testBuildConfigWithoutEnoughParam() {
    Builder builder = new Builder().apiV3Key(API_V3_KEY);
    assertThrows(NullPointerException.class, builder::build);
  }
}
