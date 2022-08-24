package com.wechat.pay.java.core.cipher;

import com.wechat.pay.java.core.exception.DecryptionException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/** 带有关联数据的认证加密器 */
public abstract class AbstractAeadCipher implements AeadCipher {

  private final String algorithm;
  private final String transformation;
  private final int tagLengthBit;
  private final byte[] key;

  protected AbstractAeadCipher(
      String algorithm, String transformation, int tagLengthBit, byte[] key) {
    this.algorithm = algorithm;
    this.transformation = transformation;
    this.tagLengthBit = tagLengthBit;
    this.key = key;
  }

  /**
   * 加密并转换为字符串
   *
   * @param associatedData AAD，额外的认证加密数据，可以为空
   * @param nonce IV，随机字符串初始化向量
   * @param plaintext 明文
   * @return Base64编码的密文
   */
  public String encrypt(byte[] associatedData, byte[] nonce, byte[] plaintext) {
    try {
      javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(transformation);
      cipher.init(
          javax.crypto.Cipher.ENCRYPT_MODE,
          new SecretKeySpec(key, algorithm),
          new GCMParameterSpec(tagLengthBit, nonce));
      if (associatedData != null) {
        cipher.updateAAD(associatedData);
      }
      return Base64.getEncoder().encodeToString(cipher.doFinal(plaintext));
    } catch (InvalidKeyException
        | InvalidAlgorithmParameterException
        | BadPaddingException
        | IllegalBlockSizeException
        | NoSuchAlgorithmException
        | NoSuchPaddingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 解密并转换为字符串
   *
   * @param associatedData AAD，额外的认证加密数据，可以为空
   * @param nonce IV，随机字符串初始化向量
   * @param ciphertext 密文
   * @return UTF-8编码的明文
   */
  public String decrypt(byte[] associatedData, byte[] nonce, byte[] ciphertext) {
    try {
      javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(transformation);
      cipher.init(
          javax.crypto.Cipher.DECRYPT_MODE,
          new SecretKeySpec(key, algorithm),
          new GCMParameterSpec(tagLengthBit, nonce));
      if (associatedData != null) {
        cipher.updateAAD(associatedData);
      }
      return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
    } catch (InvalidKeyException
        | InvalidAlgorithmParameterException
        | NoSuchAlgorithmException
        | NoSuchPaddingException e) {
      throw new IllegalArgumentException(e);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new DecryptionException("Decryption failed", e);
    }
  }
}
