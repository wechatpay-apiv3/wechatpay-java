package com.wechat.pay.java.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class MalformedMessageExceptionTest {

  @Test
  void construct() {
    String message = "message";
    MalformedMessageException exception = new MalformedMessageException(message, new IOException());
    assertEquals(message, exception.getMessage());
    assertEquals(IOException.class, exception.getCause().getClass());
  }
}
