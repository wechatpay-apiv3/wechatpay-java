package com.wechat.pay.java.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class ValidationExceptionTest {

  @Test
  void construct() {
    String message = "message";
    ValidationException exception = new ValidationException(message, new IOException());
    assertEquals(message, exception.getMessage());
    assertEquals(IOException.class, exception.getCause().getClass());
  }
}
