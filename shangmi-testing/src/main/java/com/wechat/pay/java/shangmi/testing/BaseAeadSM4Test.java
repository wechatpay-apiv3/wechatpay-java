package com.wechat.pay.java.shangmi.testing;

import static com.wechat.pay.java.shangmi.testing.TestConfig.API_V3_KEY;
import static org.junit.jupiter.api.Assertions.*;

import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.exception.DecryptionException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Test;

public interface BaseAeadSM4Test {

  AeadCipher createAeadSMCipher(byte[] apiV3Key);

  String plaintext = "plaintext";
  String associatedData = "associatedData";
  String nonce = "uluk4a9R25RW";
  String ciphertext = "+lcLNfkZQQx+iQm20Apa3x9Mb/5L7PgZ7w==";

  @Test
  default void testEncrypt() {
    AeadCipher sm4cipher = createAeadSMCipher(API_V3_KEY.getBytes(StandardCharsets.UTF_8));
    String encryptData =
        sm4cipher.encrypt(
            associatedData.getBytes(StandardCharsets.UTF_8),
            nonce.getBytes(StandardCharsets.UTF_8),
            plaintext.getBytes(StandardCharsets.UTF_8));
    String decryptData =
        sm4cipher.decrypt(
            associatedData.getBytes(StandardCharsets.UTF_8),
            nonce.getBytes(StandardCharsets.UTF_8),
            Base64.getDecoder().decode(encryptData));
    assertEquals(plaintext, decryptData);
  }

  @Test
  default void testDecrypt() {
    AeadCipher sm4Cipher = createAeadSMCipher(API_V3_KEY.getBytes(StandardCharsets.UTF_8));
    String result =
        sm4Cipher.decrypt(
            associatedData.getBytes(StandardCharsets.UTF_8),
            nonce.getBytes(StandardCharsets.UTF_8),
            Base64.getDecoder().decode(ciphertext));
    assertEquals(plaintext, result);
  }

  @Test
  default void testDecryptFail() {
    AeadCipher sm4Cipher = createAeadSMCipher(API_V3_KEY.getBytes(StandardCharsets.UTF_8));
    assertThrows(
        DecryptionException.class,
        () ->
            sm4Cipher.decrypt(
                associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                Base64.getDecoder().decode("+lcLNfkZQQx+iQm20Apa3x9Mb/5L7PgZ8w==")));
  }
}
