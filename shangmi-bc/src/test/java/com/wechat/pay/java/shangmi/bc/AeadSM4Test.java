package com.wechat.pay.java.shangmi.bc;

import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.shangmi.testing.BaseAeadSM4Test;

public class AeadSM4Test implements BaseAeadSM4Test {

  @Override
  public AeadCipher createAeadSMCipher(byte[] apiV3Key) {
    return new AeadSM4Cipher(apiV3Key);
  }
}
