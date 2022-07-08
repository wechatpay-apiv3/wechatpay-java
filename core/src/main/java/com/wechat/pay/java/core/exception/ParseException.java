package com.wechat.pay.java.core.exception;

/** 当解析微信支付回调通知异常时抛出，例如回调通知参数不正确、解析通知数据失败。 */
public class ParseException extends WechatPayException {

  private static final long serialVersionUID = -1049702516796430238L;

  public ParseException(String format, Object... objects) {
    super(format, objects);
  }
}
