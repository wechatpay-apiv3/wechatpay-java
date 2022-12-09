package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_STRING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.RSAConfig.Builder;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RSAConfigTest {

  @ParameterizedTest
  @MethodSource("BuilderProvider")
  void testConfigWithBuilderProvider(Builder builder) {
    RSAConfig c = builder.build();

    assertNotNull(c);
    assertNotNull(c.createCredential());
    assertNotNull(c.createValidator());
    assertNotNull(c.createDecryptor());
    assertNotNull(c.createEncryptor());
    assertNotNull(c.createSigner());
  }

  static Stream<Builder> BuilderProvider() {
    return Stream.of(
        // from string
        new Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .wechatPayCertificates(WECHAT_PAY_CERTIFICATE_STRING),

        // from path
        new Builder()
            .merchantId(MERCHANT_ID)
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .wechatPayCertificatesFromPath(WECHAT_PAY_CERTIFICATE_PATH));
  }

  @Test
  void testBuildConfigWithoutEnoughParam() {
    RSAConfig.Builder builder =
        new RSAConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER);
    assertThrows(NullPointerException.class, builder::build);
  }
}
