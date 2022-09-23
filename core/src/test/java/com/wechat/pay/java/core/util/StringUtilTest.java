package com.wechat.pay.java.core.util;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.wechat.pay.java.core.model.TestServiceRequest;
import org.junit.jupiter.api.Test;

class StringUtilTest {

  @Test
  void testNullObject() {
    assertEquals("null", toIndentedString(null));
  }

  @Test
  void testToIndentedStringSucc() {
    TestServiceRequest request = new TestServiceRequest();
    request.setAppid("appid");
    request.setMchid("mchid");
    request.setOutTradeNo("outorderno");
    assertNotNull(toIndentedString(request));
  }
}
