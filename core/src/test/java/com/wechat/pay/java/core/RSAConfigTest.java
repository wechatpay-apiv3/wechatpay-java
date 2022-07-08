package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_STRING;

import org.junit.Assert;
import org.junit.Test;

public class RSAConfigTest {

  @Test
  public void testBuildConfigFromString() {
    RSAConfig rsaConfig =
        new RSAConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .wechatPayCertificates(WECHAT_PAY_CERTIFICATE_STRING)
            .build();
    Assert.assertNotNull(rsaConfig);
    Assert.assertNotNull(rsaConfig.createCredential());
    Assert.assertNotNull(rsaConfig.createValidator());
    Assert.assertNotNull(rsaConfig.createDecryptor());
    Assert.assertNotNull(rsaConfig.createEncryptor());
  }

  @Test
  public void testBuildConfigFromPath() {
    RSAConfig rsaConfig =
        new RSAConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .wechatPayCertificatesFromPath(WECHAT_PAY_CERTIFICATE_PATH)
            .build();
    Assert.assertNotNull(rsaConfig);
    Assert.assertNotNull(rsaConfig.createCredential());
    Assert.assertNotNull(rsaConfig.createValidator());
    Assert.assertNotNull(rsaConfig.createDecryptor());
    Assert.assertNotNull(rsaConfig.createEncryptor());
  }
}
