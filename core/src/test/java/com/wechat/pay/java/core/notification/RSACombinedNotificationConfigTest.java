package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.DOWNLOAD_CERTIFICATE_RESPONSE;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY_STRING;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import com.wechat.pay.java.core.notification.RSACombinedNotificationConfig.Builder;
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

class RSACombinedNotificationConfigTest implements NotificationConfigTest {
  static HttpClient httpClient;

  @BeforeAll
  static void initHttpClient() {
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
    RSACombinedNotificationConfig c = builder.build();

    assertNotNull(c);
    assertNotNull(c.createAeadCipher());
    assertNotNull(c.createVerifier());
    assertNotNull(c.getCipherType());
    assertNotNull(c.getSignType());
  }

  static Stream<Builder> BuilderProvider() {
    return Stream.of(
        // from string
        new Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .publicKey(WECHAT_PAY_PUBLIC_KEY_STRING)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .httpClient(httpClient)
            .apiV3Key(API_V3_KEY),

        // from path
        new Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .publicKeyFromPath(WECHAT_PAY_PUBLIC_KEY_PATH)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .httpClient(httpClient)
            .apiV3Key(API_V3_KEY),

        // with publickey
        new Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .publicKey(WECHAT_PAY_PUBLIC_KEY)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .httpClient(httpClient)
            .apiV3Key(API_V3_KEY));
  }

  @Test
  void testBuildConfigWithoutEnoughParam() {
    Builder builder = new Builder().apiV3Key(API_V3_KEY);
    assertThrows(NullPointerException.class, builder::build);
  }

  @Override
  public NotificationConfig buildNotificationConfig() {
    return new Builder()
        .merchantId(MERCHANT_ID)
        .privateKey(MERCHANT_PRIVATE_KEY_STRING)
        .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
        .publicKey(WECHAT_PAY_PUBLIC_KEY)
        .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
        .apiV3Key(API_V3_KEY)
        .httpClient(httpClient)
        .build();
  }
}
