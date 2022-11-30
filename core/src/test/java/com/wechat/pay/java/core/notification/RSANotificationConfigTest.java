package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_STRING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.exception.ServiceException;
import org.junit.jupiter.api.Test;

public class RSANotificationConfigTest implements NotificationConfigTest {

  @Test
  public void testBuildConfigFromStr() {
    NotificationConfig config =
        new RSANotificationConfig.Builder()
            .certificates(WECHAT_PAY_CERTIFICATE_STRING)
            .apiV3Key(API_V3_KEY)
            .build();
    assertNotNull(config);
  }

  @Test
  public void testBuildConfigFromPath() {
    NotificationConfig config =
        new RSANotificationConfig.Builder()
            .certificatesFromPath(WECHAT_PAY_CERTIFICATE_PATH)
            .apiV3Key(API_V3_KEY)
            .build();
    assertNotNull(config);
  }

  @Test
  public void testBuildConfigWithCertificateProvider() {
    assertThrows(
        ServiceException.class,
        () -> {
          NotificationConfig config =
              new RSANotificationConfig.Builder()
                  .privateKeyFromStr(MERCHANT_PRIVATE_KEY_STRING)
                  .merchantId(MERCHANT_ID)
                  .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
                  .apiV3Key(API_V3_KEY)
                  .build();
        });
  }

  @Override
  public NotificationConfig buildNotificationConfig() {
    return new RSANotificationConfig.Builder()
        .certificates(WECHAT_PAY_CERTIFICATE)
        .apiV3Key(API_V3_KEY)
        .build();
  }
}
