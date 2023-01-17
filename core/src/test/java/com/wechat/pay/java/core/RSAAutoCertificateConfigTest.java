package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.*;

import com.wechat.pay.java.core.RSAAutoCertificateConfig.Builder;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.certificate.model.Data;
import com.wechat.pay.java.core.certificate.model.DownloadCertificateResponse;
import com.wechat.pay.java.core.certificate.model.EncryptCertificate;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import com.wechat.pay.java.core.util.GsonUtil;
import com.wechat.pay.java.core.util.NonceUtil;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
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
                chain -> {
                  DownloadCertificateResponse downloadResp = new DownloadCertificateResponse();
                  ArrayList<Data> dataList = new ArrayList<>();
                  Data data = new Data();
                  data.setEffectiveTime("2022-06-08T10:34:56+08:00");
                  data.setExpireTime("2025-12-08T10:34:56+08:00");
                  data.setSerialNo("440024045C4A427599D09BB4E3DE0279F2E813FD");
                  EncryptCertificate encryptCertificate = new EncryptCertificate();
                  encryptCertificate.setAlgorithm("AEAD_AES_256_GCM");
                  encryptCertificate.setAssociatedData("certificate");
                  encryptCertificate.setNonce("61f9c719728a");
                  encryptCertificate.setCiphertext(
                      "9kRsp3GhB0VA31gv0QxUssTdPZn97GSnyMEBBk0S52lfnzSgo8v17lNbndISX+a970CjAPnx8lFhg/WKzv7v0uUno8W09mtj0l9ERsHkVnTkTWJ5dYUw+AADMVSteWRMuguLbmiwlohZRi1uMWEHbGwucy3dFYY3AF6EuCfklobSOHf6p+jvKjbN/+V6SYNE7ZYMFcHyL/IGTuk56Od9zR4CHemg93FCzxDIlymdfhadZg2Sj+X9T3L/opHtwjhsw/vg/IZEuaVCTmj6M9a7X9EN126CR88JuasauDKcVobiQyTjFlHp1iOSDU+7QxJGtpHduR06uNlgJr8P8zqACV76o4fHWACTWqYyh8CQUMxXL7GlJMQilwXx+4AOylFPPnJ7b1fvGIRA9i3ugc6ZR1qop9qeNhW0R+SAPxfWgy1xysVISe/dKLVY/UwxN/y83s7do6Eud+rm+qk7uVaaOY23681pxdq3WsnoaGsUxdTHSn0c9P8Zw7JvVvlYlN/gY49MX6JXPCI8PV4pWR8lWA4I9nxIzUDZOyZN9O3ZVQLiDJDJDFrGSwyUUEZDYmlv62myrqSoShkf8qEBa8J+nELTW7CHf/TQmnbs6DKchVOEpKMRotIBwULvoYU+Y/SoIAUbWWXjDh1m6lh40tLFsqbD+CKTlOWs17l18zIEyDqG7RP9QUxapdghNCiIvwzbBPpPvc1mZG22K3GVg3pYPeMu98AkCrOVWI+Xn7x7OCUMPp+9X0RUQo3vChD5mT5Ldg31Zjwyxhg7JSVRYG+BdS0Ova1T23Y5yBIVZFvhCTvJM2lG+uvgHE6Va25BZm6gxGAgYCgCrhQFCr6+Wd6QsM+qvsddh5jBaWOI5sO/Gt0DuAtn7WKiE7QaLsDe979+EIKYbrzaRNz3LBbSUECEjrfXmnhHuMLWInzFzwaZf7nkStwYeOLqukwV2UFGMqYvD7OYM0rOTQey7lGqId1XyVECA2XV/J4qOxGkMhYjVzLcvWltsgEqchWUrWfD9ZfA2Zcm5Utw36mBSEvDFrixkomVAS0Utj9NfUIX1e5BlZgK+8KCR+/PK5qo8aAZrXb907wpwXBZ5tEtikYcfg8b4s7K47BDeDvSf/padf6/x8vtsYGwy0hSTr69I5Uto4AoBjT0EH8h38CVVRbyDuW1qfh3vXNNd+By4vggucpzRYGhAOCdvLhKqn1qfa0ue9/wjSCoUh7/eGgBhmrvx/4mEsBbOvqJYP4vkFh8U7XqGE1myBKO2TtArVuSVjV511d+hBdPoLf2k6Z0L/FgybodrqmsqaZvCv7KgW/v7jpSwT/Ema/ZpK1WT6GxdyOomVA1xrXWiWg+wetaRnKvncgJvPAaKrv2B4JOGYhmSYC6A28kwU4BMm7CFnZuTo0PrFHl5k3ZDMAlxuKX5CHK5v52ERU2rpywavb1SlvGZXgXI0HW+dN7Mvjvnc2eWUrWLj7OZK4jPWuGXLkHvgB0pKU2lfjQn0Cyo2UbErwQiHgM8aZcKsTIl9sT/eX6OKHFSPQA/GfCM5qzPIHzBfPEbAkliQeIUXwB3gW9dd79DS+PXjt62oklcO6dPSNIlIV3kvtDBSpdaHKMll9C2VUPA+CYi/EldWDD/RxCCtK5sGf4niiHjO59F/oKGIi0kZB234hIpWSVq1tPNy58+7IPWOFKCzsK41Y18maD+M7higw+dkTeanvmP8Y/JWp/E7wiWpLmJdFV21cjrtjPIDUrg34gkJ/BaESaF+hTB/9gMCLFYidHxHBWGHqWPHM=");
                  data.setEncryptCertificate(encryptCertificate);
                  dataList.add(data);
                  downloadResp.setData(dataList);
                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header("key", "val")
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              GsonUtil.getGson().toJson(downloadResp),
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                })
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
