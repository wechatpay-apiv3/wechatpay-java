package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.RSAAutoCertificateConfig.Builder;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import java.util.Collections;
import java.util.stream.Stream;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class RSAAutoCertificateConfigTest {

  @ParameterizedTest
  @MethodSource("BuilderProvider")
  void testConfigWithBuilderProvider(Builder builder) {

    assertThrows(ServiceException.class, builder::build);
  }

  static Stream<Builder> BuilderProvider() {
    return Stream.of(
        // from string
        new Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY),

        // from path
        new Builder()
            .merchantId(MERCHANT_ID)
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY),

        // with HttpClient
        new Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY)
            .httpClient(
                new OkHttpClientAdapter(
                    new WechatPay2Credential(
                        MERCHANT_ID,
                        new RSASigner(MERCHANT_CERTIFICATE_SERIAL_NUMBER, MERCHANT_PRIVATE_KEY)),
                    new WechatPay2Validator(
                        new RSAVerifier(
                            new InMemoryCertificateProvider(
                                Collections.singletonList(WECHAT_PAY_CERTIFICATE)))),
                    new OkHttpClient())));
  }

  @Test
  void testBuildConfigWithoutEnoughParam() {
    RSAAutoCertificateConfig.Builder builder =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER);
    assertThrows(NullPointerException.class, builder::build);
  }

  @Test
  void testBuildConfigWithHttpClient() {
    RSAAutoCertificateConfig.Builder builder =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY)
            .httpClient(
                new OkHttpClientAdapter(
                    new WechatPay2Credential(
                        MERCHANT_ID,
                        new RSASigner(MERCHANT_CERTIFICATE_SERIAL_NUMBER, MERCHANT_PRIVATE_KEY)),
                    new WechatPay2Validator(
                        new RSAVerifier(
                            new InMemoryCertificateProvider(
                                Collections.singletonList(WECHAT_PAY_CERTIFICATE)))),
                    new OkHttpClient()));
    assertThrows(ServiceException.class, builder::build);
  }
}
