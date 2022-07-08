package com.wechat.pay.java.core.cipher;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/** RSA敏感信息加密器 */
public final class RSAPrivacyEncryptor implements PrivacyEncryptor {

  private static final String RSA_ECB_OAEPWITHSHA1ANDMGF1PADDING =
      "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
  private final PublicKey publicKey;
  private final Cipher cipher;
  private final String wechatPaySerial;

  public RSAPrivacyEncryptor(PublicKey publicKey, String wechatPaySerial) {
    this.wechatPaySerial = wechatPaySerial;
    this.publicKey = publicKey;
    try {
      this.cipher = Cipher.getInstance(RSA_ECB_OAEPWITHSHA1ANDMGF1PADDING);
      this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new IllegalStateException(
          "The current Java environment does not support RSA v1.5/OAEP.", e);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("RSA encryption using an illegal certificate.", e);
    }
  }

  @Override
  public String encrypt(String plaintext) {
    return encryptWithTransformation(RSA_ECB_OAEPWITHSHA1ANDMGF1PADDING, plaintext);
  }

  public String encryptWithTransformation(String transformation, String plaintext) {
    requireNonNull(plaintext);
    Cipher cipher = this.cipher;
    if (!RSA_ECB_OAEPWITHSHA1ANDMGF1PADDING.equals(transformation)) {
      try {
        cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        throw new IllegalStateException(
            "The current Java environment does not support RSA v1.5/OAEP.", e);
      } catch (InvalidKeyException e) {
        throw new IllegalArgumentException("RSA encryption using an illegal certificate.", e);
      }
    }
    try {
      return Base64.getEncoder()
          .encodeToString(cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8)));
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new IllegalArgumentException(
          "The length of the encrypted original string cannot exceed 214 bytes.");
    }
  }

  @Override
  public String getWechatpaySerial() {
    return wechatPaySerial;
  }
}
