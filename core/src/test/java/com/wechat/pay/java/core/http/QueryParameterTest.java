package com.wechat.pay.java.core.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class QueryParameterTest {

  @Test
  void testGetQueryStr() {
    QueryParameter queryParameter = new QueryParameter();
    queryParameter.add("first", "v1");
    queryParameter.add("second", "v2");
    assertEquals("?first=v1&second=v2", queryParameter.getQueryStr());
  }
}
