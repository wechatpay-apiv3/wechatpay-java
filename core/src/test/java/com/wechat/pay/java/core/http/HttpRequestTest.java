package com.wechat.pay.java.core.http;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.http.HttpRequest.Builder;
import org.junit.jupiter.api.Test;

class HttpRequestTest {

  @Test
  void throwHttpException() {
    Builder builder = new Builder().httpMethod(HttpMethod.GET).url("https://invalid.uri?`");
    assertThrows(HttpException.class, builder::build);
  }
}
