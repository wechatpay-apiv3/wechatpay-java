package com.wechat.pay.java.core.http;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class HttpHeadersTest {

  private static HttpHeaders httpHeaders;
  private static final String HEADER_KEY_1 = "key1";
  private static final String HEADER_VALUE_1 = "value1";

  @BeforeClass
  public static void init() {
    Map<String, String> map = new HashMap<>();
    map.put(HEADER_KEY_1, HEADER_VALUE_1);
    httpHeaders = new HttpHeaders(map);
  }

  @Test
  public void testGetHeaders() {
    Map<String, String> headers = httpHeaders.getHeaders();
    Assert.assertEquals(headers.get(HEADER_KEY_1), HEADER_VALUE_1);
  }

  @Test
  public void testAddHttpHeader() {
    String key = "key2";
    String value = "value2";
    httpHeaders.addHeader(key, value);
    Assert.assertEquals(httpHeaders.getHeaders().size(), 2);
    Assert.assertEquals(httpHeaders.getHeader(key), value);
  }
}
