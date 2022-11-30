package com.wechat.pay.java.core.notification;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public interface NotificationConfigTest {

  NotificationConfig buildNotificationConfig();

  @Test
  default void testGetSignType() {
    NotificationConfig config = buildNotificationConfig();
    assertNotNull(config.getSignType());
  }

  @Test
  default void testGetCipherType() {
    NotificationConfig config = buildNotificationConfig();
    assertNotNull(config.getCipherType());
  }

  @Test
  default void testCreateVerifier() {
    NotificationConfig config = buildNotificationConfig();
    assertNotNull(config.createVerifier());
  }

  @Test
  default void testCreateAeadCipher() {
    NotificationConfig config = buildNotificationConfig();
    assertNotNull(config.createAeadCipher());
  }
}
