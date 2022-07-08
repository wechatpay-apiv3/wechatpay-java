package com.wechat.pay.java.core.exception;

/** 验证签名失败时抛出 */
public class ValidationException extends WechatPayException {

  private static final long serialVersionUID = -5439484392712167452L;

  public ValidationException(String format, Object... objects) {
    super(format, objects);
  }

  public ValidationException(String message) {
    super(message);
  }
}
