package com.wechat.pay.java.shangmi.kona;

import static com.wechat.pay.java.shangmi.kona.TestConfig.*;
import static org.junit.jupiter.api.Assertions.*;

import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.SignatureResult;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.cipher.Verifier;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Vector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SM2SignerTest {

  private static Signer smSigner;
  private static Verifier sm2Verifier;

  @BeforeAll
  public static void init() {
    PrivateKey privateKey = SMPemUtil.loadPrivateKeyFromString(MERCHANT_PRIVATE_KEY_STRING);
    smSigner = new SM2Signer(MERCHANT_CERTIFICATE_SERIAL_NUMBER, privateKey);
    List<X509Certificate> list = new Vector<>();
    list.add(MERCHANT_CERTIFICATE);
    sm2Verifier = new SM2Verifier(new InMemoryCertificateProvider(list));
  }

  @Test
  public void testSign() {
    String message = "message";
    SignatureResult signatureResult = smSigner.sign(message);
    assertNotNull(signatureResult);
    assertEquals(signatureResult.getCertificateSerialNumber(), MERCHANT_CERTIFICATE_SERIAL_NUMBER);
    assertTrue(
        sm2Verifier.verify(
            signatureResult.getCertificateSerialNumber(), message, signatureResult.getSign()));
  }
}
