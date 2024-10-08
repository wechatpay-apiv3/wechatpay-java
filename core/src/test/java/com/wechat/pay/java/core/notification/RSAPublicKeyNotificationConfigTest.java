package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY_STRING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.notification.RSAPublicKeyNotificationConfig.Builder;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RSAPublicKeyNotificationConfigTest implements NotificationConfigTest {

  @ParameterizedTest
  @MethodSource("BuilderProvider")
  void testConfigWithBuilderProvider(Builder builder) {
    RSAPublicKeyNotificationConfig c = builder.build();

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
            .publicKey(WECHAT_PAY_PUBLIC_KEY_STRING)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY),

        // from path
        new Builder()
            .publicKeyFromPath(WECHAT_PAY_PUBLIC_KEY_PATH)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY),

        // with publickey
        new Builder()
            .publicKey(WECHAT_PAY_PUBLIC_KEY)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
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
        .publicKey(WECHAT_PAY_PUBLIC_KEY)
        .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
        .apiV3Key(API_V3_KEY)
        .build();
  }
}
