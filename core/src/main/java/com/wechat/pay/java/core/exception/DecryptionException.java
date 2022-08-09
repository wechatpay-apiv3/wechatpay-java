package com.wechat.pay.java.core.exception;

import java.io.Serial;

public class DecryptionException extends WechatPayException {
  @Serial private static final long serialVersionUID = 1L;

  public DecryptionException(String message) {
    super(message);
  }

  public DecryptionException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
