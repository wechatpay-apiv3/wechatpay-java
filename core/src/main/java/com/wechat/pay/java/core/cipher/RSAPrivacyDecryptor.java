package com.wechat.pay.java.core.cipher;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/** RSA敏感信息解密器 */
public final class RSAPrivacyDecryptor implements PrivacyDecryptor {

  private static final String RSA_ECB_OAEPWITHSHA1ANDMGF1PADDING =
      "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
  private final PrivateKey privateKey;
  private final Cipher cipher;

  public RSAPrivacyDecryptor(PrivateKey privateKey) {
    this.privateKey = requireNonNull(privateKey);
    try {
      this.cipher = Cipher.getInstance(RSA_ECB_OAEPWITHSHA1ANDMGF1PADDING);
      this.cipher.init(Cipher.DECRYPT_MODE, privateKey);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new IllegalStateException(
          "The current Java environment does not support RSA v1.5/OAEP.", e);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("RSA encryption using an illegal certificate.", e);
    }
  }

  @Override
  public String decrypt(String ciphertext) {
    return decryptWithTransformation(RSA_ECB_OAEPWITHSHA1ANDMGF1PADDING, ciphertext);
  }

  public String decryptWithTransformation(String transformation, String ciphertext) {
    requireNonNull(ciphertext);
    Cipher cipher = this.cipher;
    if (!RSA_ECB_OAEPWITHSHA1ANDMGF1PADDING.equals(transformation)) {
      try {
        cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        throw new IllegalStateException(
            "The current Java environment does not support " + transformation, e);
      } catch (InvalidKeyException e) {
        throw new IllegalArgumentException("RSA encryption using an illegal certificate.", e);
      }
    }
    try {
      return new String(
          cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new RuntimeException("RSA Decryption failed.", e);
    }
  }
}
