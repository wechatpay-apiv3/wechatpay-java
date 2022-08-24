package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.model.TestConfig.*;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RSAPrivacyEncryptorTest {

  private static RSAPrivacyEncryptor rsaPrivacyEncryptor;
  private static RSAPrivacyDecryptor rsaPrivacyDecryptor;
  private static final String PLAINTEXT = "plaintext";

  @BeforeClass
  public static void init() {
    rsaPrivacyEncryptor =
        new RSAPrivacyEncryptor(
            MERCHANT_CERTIFICATE.getPublicKey(), MERCHANT_CERTIFICATE_SERIAL_NUMBER);
    rsaPrivacyDecryptor = new RSAPrivacyDecryptor(MERCHANT_PRIVATE_KEY);
  }

  @Test
  public void testEncryptWithOAEP() {
    String ciphertext = rsaPrivacyEncryptor.encrypt(PLAINTEXT);
    String decryptMessage = rsaPrivacyDecryptor.decrypt(ciphertext);
    Assert.assertEquals(PLAINTEXT, decryptMessage);
    Assert.assertEquals(
        MERCHANT_CERTIFICATE_SERIAL_NUMBER, rsaPrivacyEncryptor.getWechatpaySerial());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEncryptTooLargePlaintext() {
    int paddingLen = 2 * 20 + 2; // OAEP adds 2 * sha1's length + 2 padding
    rsaPrivacyEncryptor.encrypt(new String(new char[256 - paddingLen + 1]));
  }
}
