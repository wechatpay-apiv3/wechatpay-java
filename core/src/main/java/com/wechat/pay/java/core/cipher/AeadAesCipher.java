package com.wechat.pay.java.core.cipher;

/** 带有关联数据的AES认证加解密器 */
public final class AeadAesCipher extends AbstractAeadCipher {

  private static final String TRANSFORMATION = "AES/GCM/NoPadding";

  private static final int TAG_LENGTH_BIT = 128;
  private static final String ALGORITHM = "AES";

  public AeadAesCipher(byte[] key) {
    super(ALGORITHM, TRANSFORMATION, TAG_LENGTH_BIT, key);
  }
}
