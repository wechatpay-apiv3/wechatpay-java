package com.wechat.pay.java.core.certificate;

import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public interface CertificateProviderTest {

  CertificateProvider createCertificateProvider();

  @Test
  default void testGetAvailableCertificate() {
    assertNotNull(createCertificateProvider().getAvailableCertificate());
  }

  @Test
  default void testGetCertificates() {
    assertNotNull(createCertificateProvider().getCertificate(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER));
  }
}
