package com.wechat.pay.java.shangmi.bc;

import com.wechat.pay.java.core.cipher.AbstractAeadCipher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/** SM4认证加解密器 */
public class AeadSM4Cipher extends AbstractAeadCipher {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  protected AeadSM4Cipher(byte[] key) {
    super("SM4", "SM4/GCM/NoPadding", 128, covertSM4Key(key));
  }

  /**
   * 取SM3摘要的前128位，将APIv3Key转化成SM4使用的密钥
   *
   * @param apiV3Key APIv3Key
   * @return SM4Gcm的密钥
   */
  private static byte[] covertSM4Key(byte[] apiV3Key) {
    try {
      MessageDigest md = MessageDigest.getInstance("SM3", BouncyCastleProvider.PROVIDER_NAME);
      return Arrays.copyOf(md.digest(apiV3Key), 16);
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new RuntimeException(e);
    }
  }
}
