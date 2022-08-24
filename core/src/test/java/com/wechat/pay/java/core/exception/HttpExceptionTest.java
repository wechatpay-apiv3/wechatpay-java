package com.wechat.pay.java.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class HttpExceptionTest {

  @Test
  void testConstruct() {
    String message = "message";
    HttpException exception = new HttpException(message, new IOException());
    assertEquals(message, exception.getMessage());
    assertEquals(IOException.class, exception.getCause().getClass());
  }
}
