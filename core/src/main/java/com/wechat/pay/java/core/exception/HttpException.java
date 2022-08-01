package com.wechat.pay.java.core.exception;

import com.wechat.pay.java.core.http.HttpRequest;

/** 发送HTTP请求失败时抛出。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。 */
public class HttpException extends WechatPayException {

  private static final long serialVersionUID = 8583990125724273072L;
  private HttpRequest httpRequest;

  /**
   * 构造请求参数失败时调用
   *
   * @param message 错误信息
   * @param cause 引起失败的原始异常
   */
  public HttpException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * 发送请求失败时调用
   *
   * @param httpRequest http请求
   * @param cause 引起失败的原始异常
   */
  public HttpException(HttpRequest httpRequest, Throwable cause) {
    super(String.format("Send Http Request failed,httpRequest[%s]", httpRequest), cause);
    this.httpRequest = httpRequest;
  }

  /**
   * 获取序列化版本UID
   *
   * @return UID
   */
  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  /**
   * 获取HTTP请求
   *
   * @return HTTP请求
   */
  public HttpRequest getHttpRequest() {
    return httpRequest;
  }
}
