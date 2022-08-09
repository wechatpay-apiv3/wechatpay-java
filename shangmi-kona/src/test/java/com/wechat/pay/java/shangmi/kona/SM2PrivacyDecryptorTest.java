package com.wechat.pay.java.shangmi.kona;

import static com.wechat.pay.java.shangmi.kona.TestConfig.MERCHANT_PRIVATE_KEY_STRING;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SM2PrivacyDecryptorTest {
  private static SM2PrivacyDecryptor sm2PrivacyDecryptor;
  private static final String CIPHERTEXT =
      "MHECIAGn23Ew8nF0zRfu3qgdD6VATSmGM5wrVFvNqg1ToXneAiBgfOJLVi1NtpRVH2"
          + "qanzooL4gOrDKKOloD7gS17g+iCQQgcTtJpGhytHZeV/HeTqYkg3YqOY+Vybx+OQx217Ktn0QECdMjiI7lpId5dg==";

  @BeforeAll
  public static void init() {
    sm2PrivacyDecryptor =
        new SM2PrivacyDecryptor(SMPemUtil.loadPrivateKeyFromString(MERCHANT_PRIVATE_KEY_STRING));
  }

  @Test
  public void testDecrypt() {
    String plaintext = "plaintext";
    String decryptData = sm2PrivacyDecryptor.decrypt(CIPHERTEXT);
    assertEquals(decryptData, plaintext);
  }

  @Test
  public void testDecryptFail() {
    assertThrows(
        IllegalArgumentException.class, () -> sm2PrivacyDecryptor.decrypt(CIPHERTEXT + "xxoo"));
  }
}
