package com.wechat.pay.java.shangmi.kona;

import static com.wechat.pay.java.core.cipher.Constant.HEX;
import static com.wechat.pay.java.shangmi.kona.TestConfig.MERCHANT_CERTIFICATE_PATH;
import static com.wechat.pay.java.shangmi.kona.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static org.junit.jupiter.api.Assertions.*;

import java.security.cert.X509Certificate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SM2PrivacyEncryptorTest {
  private static SM2PrivacyEncryptor sm2PrivacyEncryptor;
  private static SM2PrivacyDecryptor sm2PrivacyDecryptor;

  @BeforeAll
  public static void init() {
    X509Certificate certificate = SMPemUtil.loadCertificateFromPath(MERCHANT_CERTIFICATE_PATH);
    sm2PrivacyEncryptor =
        new SM2PrivacyEncryptor(
            certificate.getPublicKey(), certificate.getSerialNumber().toString(HEX));
    sm2PrivacyDecryptor =
        new SM2PrivacyDecryptor(SMPemUtil.loadPrivateKeyFromString(MERCHANT_PRIVATE_KEY_STRING));
  }

  @Test
  public void testEncrypt() {
    String plaintext = "plaintext";
    String encryptData = sm2PrivacyEncryptor.encrypt(plaintext);
    String decryptData = sm2PrivacyDecryptor.decrypt(encryptData);
    assertEquals(plaintext, decryptData);
  }
}
