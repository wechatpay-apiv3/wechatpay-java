package com.wechat.pay.java.core.certificate;

import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Vector;
import org.junit.Test;

public class InMemoryCertificateProviderTest implements CertificateProviderTest {

  @Test
  public void getCertificate() {
    Vector<X509Certificate> listCertificates = new Vector<>();
    listCertificates.add(WECHAT_PAY_CERTIFICATE);
    InMemoryCertificateProvider provider = new InMemoryCertificateProvider(listCertificates);

    Certificate certificate = provider.getCertificate(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER);
    assertEquals(WECHAT_PAY_CERTIFICATE, certificate);

    Certificate noExistsCertificate = provider.getCertificate(MERCHANT_CERTIFICATE_SERIAL_NUMBER);
    assertNull(noExistsCertificate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailWithEmptyList() {
    Vector<X509Certificate> listCertificates = new Vector<>();
    new InMemoryCertificateProvider(listCertificates);
  }

  @Override
  public CertificateProvider createCertificateProvider() {
    Vector<X509Certificate> listCertificates = new Vector<>();
    listCertificates.add(WECHAT_PAY_CERTIFICATE);
    return new InMemoryCertificateProvider(listCertificates);
  }
}
