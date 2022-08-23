package com.wechat.pay.java.shangmi.kona;

import static com.wechat.pay.java.shangmi.kona.TestConfig.*;
import static org.junit.jupiter.api.Assertions.*;

import com.wechat.pay.java.core.notification.NotificationConfig;
import org.junit.jupiter.api.Test;

class SMNotificationConfigTest {
  @Test
  public void testBuildConfigFromString() {
    NotificationConfig config =
        new SMNotificationConfig.Builder()
            .addWechatPayCertificate(WECHAT_PAY_CERTIFICATE_STRING)
            .apiV3Key(API_V3_KEY)
            .build();
    assertNotNull(config.createAeadCipher());
    assertNotNull(config.createVerifier());
  }
}
