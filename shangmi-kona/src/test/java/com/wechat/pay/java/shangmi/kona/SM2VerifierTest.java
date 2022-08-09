package com.wechat.pay.java.shangmi.kona;

import static com.wechat.pay.java.shangmi.kona.TestConfig.MERCHANT_CERTIFICATE;
import static com.wechat.pay.java.shangmi.kona.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static org.junit.jupiter.api.Assertions.*;

import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.Verifier;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Vector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SM2VerifierTest {
  private static Verifier sm2Verifier;
  private static final String MESSAGE = "message";
  private static final String SIGNATURE =
      "MEYCIQD35Rp3CKt7pLme/a0GaCVmwSeOl7X/12+ppRivcJuf6QIhAO5Vpy4tSk9SJUrgH5I5Qy0aYhFG9lN0aof1f5wJOI05";

  @BeforeAll
  public static void init() {
    List<X509Certificate> list = new Vector<>();
    list.add(MERCHANT_CERTIFICATE);
    sm2Verifier = new SM2Verifier(new InMemoryCertificateProvider(list));
  }

  @Test
  public void testVerify() {
    assertTrue(sm2Verifier.verify(MERCHANT_CERTIFICATE_SERIAL_NUMBER, MESSAGE, SIGNATURE));
  }

  @Test
  public void testVerifyFail() {
    assertFalse(sm2Verifier.verify(MERCHANT_CERTIFICATE_SERIAL_NUMBER, "false-message", SIGNATURE));
  }
}
