package com.wechat.pay.java.core.util;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StringUtilTest {

  @Test
  void testNullObject() {
    assertEquals("null", toIndentedString(null));
  }

  @Test
  void testToIndentedStringSucc() {
    assertEquals("test\n    ", toIndentedString("test\n"));
  }
}
