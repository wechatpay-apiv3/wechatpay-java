package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.model.TestConfig.API_V3_KEY;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AeadAesCipherTest {

  private static final String MESSAGE = "message";
  private static final String ASSOCIATED_DATA = "associatedData";
  private static final String NONCE = "uluk4a9R25RW";
  private static final String CIPHERTEXT = "ulwSiIajGClcvcOYvOQ7+l+0PAbzzwI=";
  private static AeadAesCipher aeadAesCipher;

  @BeforeClass
  public static void init() {
    aeadAesCipher = new AeadAesCipher(API_V3_KEY.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testEncryptToString() {
    String ciphertext =
        aeadAesCipher.encryptToString(
            ASSOCIATED_DATA.getBytes(StandardCharsets.UTF_8),
            NONCE.getBytes(StandardCharsets.UTF_8),
            MESSAGE.getBytes(StandardCharsets.UTF_8));
    Assert.assertEquals(ciphertext, CIPHERTEXT);
  }

  @Test
  public void testDecryptToString() {
    String plaintext =
        aeadAesCipher.decryptToString(
            ASSOCIATED_DATA.getBytes(StandardCharsets.UTF_8),
            NONCE.getBytes(StandardCharsets.UTF_8),
            Base64.getDecoder().decode(CIPHERTEXT));
    Assert.assertEquals(plaintext, MESSAGE);
  }
}
