package com.wechat.pay.java.shangmi.kona;

import com.tencent.crypto.provider.SMCSProvider;
import com.wechat.pay.java.core.cipher.AbstractAeadCipher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;

/** 国密SM4Gcm加解密器 */
public final class AeadSM4Cipher extends AbstractAeadCipher {

  static {
    Security.addProvider(new SMCSProvider());
  }

  private static final String TRANSFORMATION = "SM4/GCM/NoPadding";
  private static final int TAG_LENGTH_BIT = 128;
  private static final String ALGORITHM = "SM4";

  /**
   *
   * @param apiV3Key APIv3密钥
   */
  public AeadSM4Cipher(byte[] apiV3Key) {
    super(ALGORITHM, TRANSFORMATION, TAG_LENGTH_BIT, covertSM4Key(apiV3Key));
  }

  /**
   * 取SM3摘要的前128位，将APIv3Key转化成SM4使用的密钥
   *
   * @param apiV3Key APIv3Key
   * @return SM4Gcm的密钥
   */
  private static byte[] covertSM4Key(byte[] apiV3Key) {
    try {
    MessageDigest md = MessageDigest.getInstance("SM3");
    return Arrays.copyOf(md.digest(apiV3Key), 16);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
