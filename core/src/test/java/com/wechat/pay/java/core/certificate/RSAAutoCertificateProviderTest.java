package com.wechat.pay.java.core.certificate;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.DOWNLOAD_CERTIFICATE_RESPONSE;
import static com.wechat.pay.java.core.model.TestConfig.DOWNLOAD_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider.Builder;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class RSAAutoCertificateProviderTest implements CertificateProviderTest {

  static RSAAutoCertificateProvider spyAutoProvider;
  static OkHttpClient okHttpClient;

  static class EmtpyValidator implements Validator {
    @Override
    public boolean validate(HttpHeaders responseHeaders, String body) {
      return true;
    }
  }

  static class FalseValidator implements Validator {
    @Override
    public boolean validate(HttpHeaders responseHeaders, String body) {
      return false;
    }
  }

  @BeforeAll
  static void beforeAll() {
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
    HttpClient httpClient =
        new OkHttpClientAdapter(
            new WechatPay2Credential(
                "5123456", new RSASigner(MERCHANT_CERTIFICATE_SERIAL_NUMBER, MERCHANT_PRIVATE_KEY)),
            new EmtpyValidator(),
            okHttpClient);
    spyAutoProvider =
        new Builder()
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantId("5123456")
            .httpClient(httpClient)
            .apiV3Key(API_V3_KEY.getBytes(StandardCharsets.UTF_8))
            .build();
  }

  @Override
  public CertificateProvider createCertificateProvider() {
    return spyAutoProvider;
  }

  @Test
  public void testCreateWithHttpClientBuilder() {
    Builder provider =
        new Builder()
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .merchantId("5123456-0-1")
            .apiV3Key(API_V3_KEY.getBytes(StandardCharsets.UTF_8));

    DefaultHttpClientBuilder builder =
        new DefaultHttpClientBuilder()
            .okHttpClient(okHttpClient)
            .connectTimeoutMs(1000)
            .readTimeoutMs(1000)
            .writeTimeoutMs(1000);

    assertDoesNotThrow(
        () -> {
          provider.httpClientBuilder(builder);
          provider.build();
        });
  }

  @ParameterizedTest
  @MethodSource("BuilderProvider")
  void testCreateProvider(Builder builder) {
    RSAAutoCertificateProvider provider = builder.build();
    assertNotNull(provider.getAvailableCertificate());
  }

  static Stream<Builder> BuilderProvider() {
    return Stream.of(
        // httpclient builder with timeout
        new Builder()
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .merchantId("5123456-1-0")
            .apiV3Key(API_V3_KEY.getBytes(StandardCharsets.UTF_8))
            .httpClientBuilder(
                new DefaultHttpClientBuilder()
                    .writeTimeoutMs(1000)
                    .readTimeoutMs(1000)
                    .okHttpClient(okHttpClient)),
        // httpclient builder with non-empty validator
        new Builder()
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .merchantId("5123456-1-1")
            .apiV3Key(API_V3_KEY.getBytes(StandardCharsets.UTF_8))
            .httpClientBuilder(
                new DefaultHttpClientBuilder()
                    .okHttpClient(okHttpClient)
                    .validator(new FalseValidator())));
  }

  @Override
  @Test
  public void testGetCertificates() {
    assertNotNull(createCertificateProvider().getCertificate(DOWNLOAD_CERTIFICATE_SERIAL_NUMBER));
  }
}
