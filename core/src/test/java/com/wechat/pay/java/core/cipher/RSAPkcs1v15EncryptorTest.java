package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.model.TestConfig.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RSAPkcs1v15EncryptorTest {

  private static RSAPkcs1v15Encryptor rsaPkcs1v15Encryptor;
  private static RSAPkcs1v15Decryptor rsaPkcs1v15Decryptor;

  @BeforeAll
  public static void init() {
    rsaPkcs1v15Decryptor = new RSAPkcs1v15Decryptor(MERCHANT_PRIVATE_KEY);
    rsaPkcs1v15Encryptor =
        new RSAPkcs1v15Encryptor(
            MERCHANT_CERTIFICATE.getPublicKey(), MERCHANT_CERTIFICATE_SERIAL_NUMBER);
  }

  @Test
  void testEncrypt() {
    String ciphertext = rsaPkcs1v15Encryptor.encrypt("plaintext");
    assertEquals("plaintext", rsaPkcs1v15Decryptor.decrypt(ciphertext));
    assertEquals(MERCHANT_CERTIFICATE_SERIAL_NUMBER, rsaPkcs1v15Encryptor.getWechatpaySerial());
  }

  @Test
  void testEncryptFailTooLong() {
    int leastPaddingLen = 11; // PKCS#1 adds at least 11 bytes
    assertDoesNotThrow(
        () -> rsaPkcs1v15Encryptor.encrypt(new String(new byte[256 - leastPaddingLen])));
    assertThrowsExactly(
        IllegalArgumentException.class,
        () -> rsaPkcs1v15Encryptor.encrypt(new String(new byte[256 - leastPaddingLen + 1])));
  }
}
