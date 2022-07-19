package com.wechat.pay.java.core.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncoder {

  /**
   * 对参数进行url编码
   *
   * @param string 待编码的字符串
   * @return 编码后的字符串
   */
  public static String urlEncode(String string) {
    try {
      return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
