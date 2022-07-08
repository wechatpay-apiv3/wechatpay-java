package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY;

import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Vector;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RSASignerTest {

  private static Signer rsaSigner;
  private static Verifier rsaVerifier;

  @BeforeClass
  public static void init() {
    rsaSigner = new RSASigner(MERCHANT_CERTIFICATE_SERIAL_NUMBER, MERCHANT_PRIVATE_KEY);

    List<X509Certificate> list = new Vector<>();
    list.add(MERCHANT_CERTIFICATE);
    rsaVerifier = new RSAVerifier(new InMemoryCertificateProvider(list));
  }

  @Test
  public void testSign() {
    String message = "message";
    SignatureResult signatureResult = rsaSigner.sign(message);
    Assert.assertTrue(
        rsaVerifier.verify(
            signatureResult.getCertificateSerialNumber(), message, signatureResult.getSign()));
  }

  @Test
  public void testGetAlgorithm() {
    Assert.assertEquals("SHA256-RSA2048", rsaSigner.getAlgorithm());
  }
}
