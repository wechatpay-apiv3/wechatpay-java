package com.wechat.pay.java.shangmi.kona;

import static com.wechat.pay.java.shangmi.kona.TestConfig.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tencent.kona.KonaProvider;
import java.security.Security;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SMConfigTest {

  @BeforeAll
  public static void init() {
    Security.addProvider(new KonaProvider());
  }

  @Test
  public void testBuildConfigFromString() {
    SMConfig smConfig =
        new SMConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .addWechatPayCertificate(WECHAT_PAY_CERTIFICATE_STRING)
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
            .addWechatPayCertificateFromPath(WECHAT_PAY_CERTIFICATE_PATH)
            .build();
    assertNotNull(smConfig);
    assertNotNull(smConfig.createCredential());
    assertNotNull(smConfig.createValidator());
    assertNotNull(smConfig.createDecryptor());
    assertNotNull(smConfig.createEncryptor());
  }
}
