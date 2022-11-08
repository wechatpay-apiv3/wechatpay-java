package com.wechat.pay.java.shangmi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SMNotificationConfigTest {

  private final String certificatePath =
      System.getProperty("user.dir")
          + "/../shangmi-testing/src/main/resources/wechat_pay_certificate.pem";
  private final String certificateStr =
      "-----BEGIN CERTIFICATE-----\n"
          + "MIICyTCCAmygAwIBAgIUVOf1ntjfRXDPsUeA6oFS1FpnXuYwDAYIKoEcz1UBg3UF\n"
          + "ADCBjTFHMEUGA1UEAww+U2hlbiBaaGVuIGlUcnVzQ2hpbmEgQ2xhc3MgRW50ZXJw\n"
          + "cmlzZSBTdWJzY3JpYmVyIENBIFNNMiAtIFRlc3QxGDAWBgNVBAsMD+a1i+ivlemD\n"
          + "qOivleeUqDEbMBkGA1UECgwS5aSp6K+a5a6J5L+h6K+V55SoMQswCQYDVQQGEwJD\n"
          + "TjAeFw0yMjA4MDQxMjE0MTlaFw0yMzA5MDMxMjE0MTlaMGsxCzAJBgNVBAYTAkNO\n"
          + "MRswGQYDVQQKDBLlvq7kv6HllYbmiLfns7vnu58xKjAoBgNVBAsMIea3seWcs+W4\n"
          + "guS8n+iNo+enkeaKgOaciemZkOWFrOWPuDETMBEGA1UEAwwKMjQ4MzI4MTc2MTBZ\n"
          + "MBMGByqGSM49AgEGCCqBHM9VAYItA0IABHvNR/deoZ0EwKcrTacPV90bP5M3zF3R\n"
          + "JBiBuRNDGTm/aaVe9rRnEMfhckUj0PZG3WlNJIOXb26FDUZjePqLL36jgcgwgcUw\n"
          + "DAYDVR0TAQH/BAIwADAOBgNVHQ8BAf8EBAMCBsAwHwYDVR0jBBgwFoAUK0Y6T9Ge\n"
          + "LM7UH4bC1j2avycoZPcwHQYDVR0OBBYEFBmE5STz2WNsXVrgclXrxsl6SCihMGUG\n"
          + "A1UdHwReMFwwWqBYoFaGVGh0dHA6Ly9ldmNhLml0cnVzLmNvbS5jbi9wdWJsaWMv\n"
          + "aXRydXNjcmw/Q0E9NzMzNUExQUYzNzRBMUU4QjQwM0FCMUFDMkQwNjVDQUU3NUNB\n"
          + "QjIzNjAMBggqgRzPVQGDdQUAA0kAMEYCIQDK0r6D8VyiUVMfRnAfz40ZtiG8DJEF\n"
          + "6Rn41oZ3qPW1aQIhAKtn5sKME+thLQFeyV70VSsraZ7h9Fccal2WzI2oCdtY\n"
          + "-----END CERTIFICATE-----";

  @Test
  void BuildTest() {
    String apiV3Key = "12345678901234567890123456789012";
    SMNotificationConfig c =
        new SMNotificationConfig.Builder()
            .apiV3Key(apiV3Key)
            .addWechatPayCertificate(SMPemUtil.loadX509FromPath(certificatePath))
            .addWechatPayCertificate(certificateStr)
            .build();
    assertNotNull(c);
    assertNotNull(c.createAeadCipher());
    assertNotNull(c.createVerifier());

    assertEquals("AEAD_SM4_GCM", c.getCipherType());
    assertEquals("WECHATPAY2-SM2-WITH-SM3", c.getSignType());
  }

  @Test
  void BuildWithWrongApiV3KeyTest() {
    String apiV3Key = "12345678901234567890123456789"; // less chars
    SMNotificationConfig.Builder builder = new SMNotificationConfig.Builder();
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          builder.apiV3Key(apiV3Key);
        });
  }

  @Test
  void BuildWithEmptyCertificateTest() {
    String apiV3Key = "12345678901234567890123456789012"; // less chars
    SMNotificationConfig.Builder builder = new SMNotificationConfig.Builder();
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              builder.apiV3Key(apiV3Key).build();
            });
    assertTrue(exception.getMessage().contains("must not be empty"));
  }
}
