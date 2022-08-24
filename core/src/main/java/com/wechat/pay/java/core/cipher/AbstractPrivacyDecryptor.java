package com.wechat.pay.java.core.cipher;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.exception.DecryptionException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public abstract class AbstractPrivacyDecryptor implements PrivacyDecryptor {
  private final PrivateKey privateKey;
  private final Cipher cipher;

  /**
   * 构造敏感信息解密的抽象类
   *
   * @param transformation 加密使用的模式
   * @param privateKey 加密使用的私钥
   */
  protected AbstractPrivacyDecryptor(String transformation, PrivateKey privateKey) {
    this.privateKey = requireNonNull(privateKey);
    try {
      cipher = Cipher.getInstance(transformation);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new IllegalArgumentException(
          "The current Java environment does not support " + transformation, e);
    }
  }

  @Override
  public String decrypt(String ciphertext) {
    requireNonNull(ciphertext);
    try {
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      return new String(
          cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("The given private key is invalid for decryption", e);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new DecryptionException("Decryption failed", e);
    }
  }
}
