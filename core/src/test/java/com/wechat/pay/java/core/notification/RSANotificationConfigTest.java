package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_STRING;

import org.junit.Assert;
import org.junit.Test;

public class RSANotificationConfigTest {

  @Test
  public void testBuildConfigFromString() {
    NotificationConfig config =
        new RSANotificationConfig.Builder()
            .certificates(WECHAT_PAY_CERTIFICATE_STRING)
            .apiV3Key(API_V3_KEY)
            .build();
    Assert.assertNotNull(config.createAeadCipher());
    Assert.assertNotNull(config.createVerifier());
  }

  @Test
  public void testBuildConfigFromPath() {
    NotificationConfig config =
        new RSANotificationConfig.Builder()
            .certificatesFromPath(WECHAT_PAY_CERTIFICATE_PATH)
            .apiV3Key(API_V3_KEY)
            .build();
    Assert.assertNotNull(config.createAeadCipher());
    Assert.assertNotNull(config.createVerifier());
  }
}
