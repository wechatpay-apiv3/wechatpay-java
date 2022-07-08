package com.wechat.pay.java.core.util;

import org.junit.Assert;
import org.junit.Test;

public class NonceUtilTest {

  @Test
  public void testCreateNonce() {
    String nonce = NonceUtil.createNonce(32);
    Assert.assertEquals(nonce.length(), 32);
  }
}
