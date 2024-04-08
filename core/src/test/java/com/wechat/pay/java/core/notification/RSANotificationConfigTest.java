package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_STRING;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_PUBLIC_KEY_STRING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.notification.RSANotificationConfig.Builder;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RSANotificationConfigTest implements NotificationConfigTest {

  @ParameterizedTest
  @MethodSource("BuilderProvider")
  void testConfigWithBuilderProvider(Builder builder) {
    RSANotificationConfig c = builder.build();

    assertNotNull(c);
    assertNotNull(c.createAeadCipher());
    assertNotNull(c.createVerifier());
    assertNotNull(c.getCipherType());
    assertNotNull(c.getSignType());
  }

  @Test
  void testBuildConfigFromStr() {
    NotificationConfig config =
        new RSANotificationConfig.Builder()
            .certificates(WECHAT_PAY_CERTIFICATE_STRING)
            .apiV3Key(API_V3_KEY)
            .build();
    assertNotNull(config);
  }

  static Stream<Builder> BuilderProvider() {
    return Stream.of(
        // certificates from string
        new RSANotificationConfig.Builder()
            .apiV3Key(API_V3_KEY)
            .certificates(WECHAT_PAY_CERTIFICATE),

        // certificates form path
        new RSANotificationConfig.Builder()
            .apiV3Key(API_V3_KEY)
            .certificatesFromPath(WECHAT_PAY_CERTIFICATE_PATH),

        // publickey form string
        new RSANotificationConfig.Builder()
            .apiV3Key(API_V3_KEY)
            .publicKey(WECHAT_PAY_PUBLIC_KEY_STRING)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER),

        // publickey form path
        new RSANotificationConfig.Builder()
            .apiV3Key(API_V3_KEY)
            .publicFromPath(WECHAT_PAY_PUBLIC_KEY_PATH)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER),

        // publickey
        new RSANotificationConfig.Builder()
            .apiV3Key(API_V3_KEY)
            .publicKey(WECHAT_PAY_PUBLIC_KEY)
            .publicKeyId(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER));
  }

  @Test
  void testBuildConfigWithoutEnoughParam() {
    Builder builder = new RSANotificationConfig.Builder().apiV3Key(API_V3_KEY);
    assertThrows(NullPointerException.class, builder::build);
  }

  @Override
  public NotificationConfig buildNotificationConfig() {
    return new RSANotificationConfig.Builder()
        .certificates(WECHAT_PAY_CERTIFICATE)
        .apiV3Key(API_V3_KEY)
        .build();
  }
}
