package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY_STRING;
import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.RSAPublicKeyConfig.Builder;
import com.wechat.pay.java.core.util.NonceUtil;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RSAPublicKeyConfigTest implements ConfigTest {

  @ParameterizedTest
  @MethodSource("BuilderProvider")
  void testConfigWithBuilderProvider(Builder builder) {
    RSAPublicKeyConfig config = builder.build();
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
            .publicKey(WECHAT_PAY_PUBLIC_KEY_STRING)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY),

        // from path
        new Builder()
            .merchantId("223456")
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .publicKeyFromPath(WECHAT_PAY_PUBLIC_KEY_PATH)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY),

        // with http client builder
        new Builder()
            .merchantId("1123456")
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .publicKey(WECHAT_PAY_PUBLIC_KEY)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY));
  }

  @Test
  void testBuildConfigWithoutEnoughParam() {
    Builder builder =
        new Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER);
    assertThrows(NullPointerException.class, builder::build);
  }

  @Override
  public Config createConfig() {
    return new Builder()
        .apiV3Key(API_V3_KEY)
        .merchantId(NonceUtil.createNonce(6))
        .privateKey(MERCHANT_PRIVATE_KEY)
        .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
        .publicKeyFromPath(WECHAT_PAY_PUBLIC_KEY_PATH)
        .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
        .build();
  }
}
