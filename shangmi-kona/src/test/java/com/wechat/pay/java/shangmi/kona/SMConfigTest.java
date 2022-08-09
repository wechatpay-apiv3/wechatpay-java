package com.wechat.pay.java.shangmi.kona;

import static com.wechat.pay.java.shangmi.kona.TestConfig.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SMConfigTest {
  @Test
  public void testBuildConfigFromString() {
    SMConfig smConfig =
        new SMConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .wechatPayCertificates(WECHAT_PAY_CERTIFICATE_STRING)
            .build();
    assertNotNull(smConfig);
    assertNotNull(smConfig.createCredential());
    assertNotNull(smConfig.createValidator());
    assertNotNull(smConfig.createDecryptor());
    assertNotNull(smConfig.createEncryptor());
  }

  @Test
  public void testBuildConfigFromPath() {
    SMConfig smConfig =
        new SMConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .wechatPayCertificatesFromPath(WECHAT_PAY_CERTIFICATE_PATH)
            .build();
    assertNotNull(smConfig);
    assertNotNull(smConfig.createCredential());
    assertNotNull(smConfig.createValidator());
    assertNotNull(smConfig.createDecryptor());
    assertNotNull(smConfig.createEncryptor());
  }
}
