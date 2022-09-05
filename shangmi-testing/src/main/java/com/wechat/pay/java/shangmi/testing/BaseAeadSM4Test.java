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

  String PLAINTEXT = "plaintext";
  String ASSOCIATED_DATA = "associatedData";
  String NONCE = "uluk4a9R25RW";
  String CIPHERTEXT = "+lcLNfkZQQx+iQm20Apa3x9Mb/5L7PgZ7w==";

  @Test
  default void testEncrypt() {
    AeadCipher sm4Cipher = createAeadSMCipher(API_V3_KEY.getBytes(StandardCharsets.UTF_8));
    String encryptData =
        sm4Cipher.encrypt(
            ASSOCIATED_DATA.getBytes(StandardCharsets.UTF_8),
            NONCE.getBytes(StandardCharsets.UTF_8),
            PLAINTEXT.getBytes(StandardCharsets.UTF_8));
    String decryptData =
        sm4Cipher.decrypt(
            ASSOCIATED_DATA.getBytes(StandardCharsets.UTF_8),
            NONCE.getBytes(StandardCharsets.UTF_8),
            Base64.getDecoder().decode(encryptData));
    assertEquals(PLAINTEXT, decryptData);
  }

  @Test
  default void testDecrypt() {
    AeadCipher sm4Cipher = createAeadSMCipher(API_V3_KEY.getBytes(StandardCharsets.UTF_8));
    String result =
        sm4Cipher.decrypt(
            ASSOCIATED_DATA.getBytes(StandardCharsets.UTF_8),
            NONCE.getBytes(StandardCharsets.UTF_8),
            Base64.getDecoder().decode(CIPHERTEXT));
    assertEquals(PLAINTEXT, result);
  }

  @Test
  default void testDecryptFail() {
    AeadCipher sm4Cipher = createAeadSMCipher(API_V3_KEY.getBytes(StandardCharsets.UTF_8));
    assertThrows(
        DecryptionException.class,
        () ->
            sm4Cipher.decrypt(
                ASSOCIATED_DATA.getBytes(StandardCharsets.UTF_8),
                NONCE.getBytes(StandardCharsets.UTF_8),
                Base64.getDecoder().decode("+lcLNfkZQQx+iQm20Apa3x9Mb/5L7PgZ8w==")));
  }
}
