package com.wechat.pay.java.shangmi.kona;

import static com.wechat.pay.java.shangmi.kona.TestConfig.API_V3_KEY;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AeadSM4CipherTest {
  public static final String plaintext = "plaintext";
  public static final String associatedData = "associatedData";
  public static final String nonce = "uluk4a9R25RW";
  public static final String ciphertext = "+lcLNfkZQQx+iQm20Apa3x9Mb/5L7PgZ7w==";
  public static AeadSM4Cipher sm4Cipher;

  @BeforeAll
  public static void init() {
    sm4Cipher = new AeadSM4Cipher(API_V3_KEY.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testEncryptToString() {
    String encryptData =
        sm4Cipher.encrypt(
            associatedData.getBytes(StandardCharsets.UTF_8),
            nonce.getBytes(StandardCharsets.UTF_8),
            plaintext.getBytes(StandardCharsets.UTF_8));
    String decryptData =
        sm4Cipher.decrypt(
            associatedData.getBytes(StandardCharsets.UTF_8),
            nonce.getBytes(StandardCharsets.UTF_8),
            Base64.getDecoder().decode(encryptData));
    Assertions.assertEquals(decryptData, plaintext);
  }

  @Test
  public void testDecryptToString() {
    String decryptData =
        sm4Cipher.decrypt(
            associatedData.getBytes(StandardCharsets.UTF_8),
            nonce.getBytes(StandardCharsets.UTF_8),
            Base64.getDecoder().decode(ciphertext));
    Assertions.assertEquals(decryptData, plaintext);
  }
}
