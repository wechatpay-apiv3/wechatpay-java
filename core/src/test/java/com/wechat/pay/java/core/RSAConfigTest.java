package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_PATH;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_STRING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.exception.ServiceException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RSAConfigTest implements ConfigTest {

  @Test
  public void testBuildConfigFromString() {
    Config rsaConfig =
        new RSAConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .wechatPayCertificates(WECHAT_PAY_CERTIFICATE_STRING)
            .build();
    assertNotNull(rsaConfig);
  }

  @Test
  public void testBuildConfigFromPath() {
    Config rsaConfig =
        new RSAConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantSerialNumberFromPath(MERCHANT_CERTIFICATE_PATH)
            .wechatPayCertificatesFromPath(WECHAT_PAY_CERTIFICATE_PATH)
            .build();
    assertNotNull(rsaConfig);
  }

  @Test
  public void testBuildConfigWithAutoUpdateCert() {
    assertThrows(
        ServiceException.class,
        () -> {
          Config rsaConfig =
              new RSAConfig.Builder()
                  .merchantId(MERCHANT_ID)
                  .privateKey(MERCHANT_PRIVATE_KEY)
                  .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
                  .apiV3Key(API_V3_KEY)
                  .build();
        });
  }

  @Test
  public void testBuildConfigWithProvider() {
    List<X509Certificate> certificatesList = new ArrayList<>();
    certificatesList.add(WECHAT_PAY_CERTIFICATE);
    Config rsaConfig =
        new RSAConfig.Builder()
            .privateKey(MERCHANT_PRIVATE_KEY)
            .merchantId(MERCHANT_ID)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .certificateProvider(new InMemoryCertificateProvider(certificatesList))
            .build();
    assertNotNull(rsaConfig);
  }

  @Override
  public Config createConfig() {
    return new RSAConfig.Builder()
        .merchantId(MERCHANT_ID)
        .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
        .merchantSerialNumberFromCertificate(MERCHANT_CERTIFICATE)
        .wechatPayCertificatesFromPath(WECHAT_PAY_CERTIFICATE_PATH)
        .build();
  }
}
