package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_STRING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.exception.ServiceException;
import java.security.cert.X509Certificate;
import org.junit.jupiter.api.Test;

class RSANotificationConfigTest implements NotificationConfigTest {

  @Test
  void testBuildConfigFromStr() {
    NotificationConfig config =
        new RSANotificationConfig.Builder()
            .certificates(WECHAT_PAY_CERTIFICATE_STRING)
            .apiV3Key(API_V3_KEY)
            .build();
    assertNotNull(config);
  }

  @Test
  void testBuildConfigFromPath() {
    NotificationConfig config =
        new RSANotificationConfig.Builder()
            .certificatesFromPath(WECHAT_PAY_CERTIFICATE_PATH)
            .apiV3Key(API_V3_KEY)
            .build();
    assertNotNull(config);
  }

  @Test
  void testBuildConfigWithInnerCertificateProvider() {
    RSANotificationConfig.Builder builder =
        new RSANotificationConfig.Builder()
            .privateKeyFromStr(MERCHANT_PRIVATE_KEY_STRING)
            .merchantId(MERCHANT_ID)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY);
    assertThrows(ServiceException.class, builder::build);
  }

  @Test
  void testBuildConfigWithInnerCertificateProvider2() {
    RSANotificationConfig.Builder builder =
        new RSANotificationConfig.Builder()
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantId(MERCHANT_ID)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .apiV3Key(API_V3_KEY);
    assertThrows(ServiceException.class, builder::build);
  }

  @Test
  void testBuildConfigWithCertificateProvider2() {
    CertificateProvider certificateProvider =
        new CertificateProvider() {
          @Override
          public X509Certificate getCertificate(String serialNumber) {
            return null;
          }

          @Override
          public X509Certificate getAvailableCertificate() {
            return null;
          }
        };
    NotificationConfig config =
        new RSANotificationConfig.Builder()
            .certificateProvider(certificateProvider)
            .apiV3Key(API_V3_KEY)
            .build();
    assertNotNull(config);
  }

  @Override
  public NotificationConfig buildNotificationConfig() {
    return new RSANotificationConfig.Builder()
        .certificates(WECHAT_PAY_CERTIFICATE)
        .apiV3Key(API_V3_KEY)
        .build();
  }
}
