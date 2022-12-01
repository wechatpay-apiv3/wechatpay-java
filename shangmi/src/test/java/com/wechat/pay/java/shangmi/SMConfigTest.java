package com.wechat.pay.java.shangmi;

import static com.wechat.pay.java.shangmi.testing.TestConfig.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class SMConfigTest {

  @ParameterizedTest
  @MethodSource("BuilderProvider")
  void testConfigWithBuilderProvider(SMConfig.Builder builder) {
    SMConfig c = builder.build();

    assertNotNull(c);
    assertNotNull(c.createCredential());
    assertNotNull(c.createValidator());
    assertNotNull(c.createDecryptor());
    assertNotNull(c.createEncryptor());
    assertNotNull(c.createSigner());
  }

  static Stream<SMConfig.Builder> BuilderProvider() {
    List<X509Certificate> certificates = new ArrayList<>();
    certificates.add(SMPemUtil.loadX509FromString(WECHAT_PAY_CERTIFICATE_STRING));
    certificates.add(SMPemUtil.loadX509FromString(MERCHANT_CERTIFICATE_STRING));

    return Stream.of(
        // from string
        new SMConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .addWechatPayCertificate(WECHAT_PAY_CERTIFICATE_STRING),

        // from path
        new SMConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .addWechatPayCertificateFromPath(WECHAT_PAY_CERTIFICATE_PATH),

        // certificate list
        new SMConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .wechatPayCertificates(certificates),

        // certificate provider
        new SMConfig.Builder()
            .merchantId(MERCHANT_ID)
            .privateKey(MERCHANT_PRIVATE_KEY_STRING)
            .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
            .wechatPayCertificateProvider(new InMemoryCertificateProvider(certificates)));
  }
}
