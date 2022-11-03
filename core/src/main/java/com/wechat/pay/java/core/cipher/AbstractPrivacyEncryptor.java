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

public abstract class AbstractPrivacyEncryptor implements PrivacyEncryptor {
  private final PublicKey publicKey;
  private final Cipher cipher;
  private final String wechatPaySerial;

  protected AbstractPrivacyEncryptor(
      String transformation, PublicKey publicKey, String wechatPaySerial) {
    this.publicKey = requireNonNull(publicKey);
    this.wechatPaySerial = requireNonNull(wechatPaySerial).toUpperCase();
    try {
      cipher = Cipher.getInstance(transformation);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new IllegalArgumentException(
          "The current Java environment does not support " + transformation, e);
    }
  }

  @Override
  public String encrypt(String plaintext) {
    requireNonNull(plaintext);
    try {
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      return Base64.getEncoder()
          .encodeToString(cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8)));
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("RSA encryption using an illegal publicKey", e);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new IllegalArgumentException("Plaintext is too long", e);
    }
  }

  @Override
  public String getWechatpaySerial() {
    return wechatPaySerial;
  }
}
