package com.wechat.pay.java.shangmi.testing;

import static com.wechat.pay.java.shangmi.testing.TestConfig.*;
import static org.junit.jupiter.api.Assertions.*;

import com.wechat.pay.java.core.cipher.*;
import com.wechat.pay.java.core.exception.DecryptionException;
import org.junit.jupiter.api.Test;

public interface BaseSM2Test {

  Signer createSM2Signer(String serialNumber, String privateKeyString);

  Verifier createSM2Verifier(String certificate);

  PrivacyEncryptor createPrivacyEncryptor(String certificateString);

  PrivacyDecryptor createPrivacyDecryptor(String privateKeyString);

  String CIPHERTEXT =
      "MHECIAGn23Ew8nF0zRfu3qgdD6VATSmGM5wrVFvNqg1ToXneAiBgfOJLVi1NtpRVH2"
          + "qanzooL4gOrDKKOloD7gS17g+iCQQgcTtJpGhytHZeV/HeTqYkg3YqOY+Vybx+OQx217Ktn0QECdMjiI7lpId5dg==";
  String PLAINTEXT = "plaintext";

  String MESSAGE = "message";
  String SIGN =
      "MEYCIQD35Rp3CKt7pLme/a0GaCVmwSeOl7X/12+ppRivcJuf6QIhAO5Vpy4tSk9SJUrgH5I5Qy0aYhFG9lN0aof1f5wJOI05";

  @Test
  default void testSign() {
    Signer s = createSM2Signer(MERCHANT_CERTIFICATE_SERIAL_NUMBER, MERCHANT_PRIVATE_KEY_STRING);
    Verifier v = createSM2Verifier(MERCHANT_CERTIFICATE_STRING);

    SignatureResult signatureResult = s.sign(MESSAGE);
    assertNotNull(signatureResult);
    assertEquals(signatureResult.getCertificateSerialNumber(), MERCHANT_CERTIFICATE_SERIAL_NUMBER);
    assertTrue(
        v.verify(signatureResult.getCertificateSerialNumber(), MESSAGE, signatureResult.getSign()));
  }

  @Test
  default void testVerify() {
    Verifier v = createSM2Verifier(MERCHANT_CERTIFICATE_STRING);
    assertTrue(v.verify(MERCHANT_CERTIFICATE_SERIAL_NUMBER, MESSAGE, SIGN));
  }

  @Test
  default void testVerifyFail() {
    Verifier v = createSM2Verifier(MERCHANT_CERTIFICATE_STRING);
    assertFalse(v.verify(MERCHANT_CERTIFICATE_SERIAL_NUMBER, "false-message", SIGN));
  }

  @Test
  default void testDecrypt() {
    PrivacyDecryptor d = createPrivacyDecryptor(MERCHANT_PRIVATE_KEY_STRING);
    assertEquals(PLAINTEXT, d.decrypt(CIPHERTEXT));
  }

  @Test
  default void testDecryptFail() {
    PrivacyDecryptor d = createPrivacyDecryptor(MERCHANT_PRIVATE_KEY_STRING);
    String ciphertext = CIPHERTEXT.replace("1", "2");
    assertNotEquals(CIPHERTEXT, ciphertext);
    assertThrows(DecryptionException.class, () -> d.decrypt(ciphertext));
  }

  @Test
  default void testEncrypt() {
    PrivacyEncryptor e = createPrivacyEncryptor(MERCHANT_CERTIFICATE_STRING);
    PrivacyDecryptor d = createPrivacyDecryptor(MERCHANT_PRIVATE_KEY_STRING);
    String plaintext = "plaintext";
    String ciphertext = e.encrypt(plaintext);
    assertEquals(plaintext, d.decrypt(ciphertext));
    assertEquals(MERCHANT_CERTIFICATE_SERIAL_NUMBER, e.getWechatpaySerial());
  }
}
