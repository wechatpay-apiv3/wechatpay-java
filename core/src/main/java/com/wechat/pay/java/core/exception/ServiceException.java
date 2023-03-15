package com.wechat.pay.java.core.exception;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.util.GsonUtil;

/** 发送HTTP请求成功，返回异常时抛出。例如返回状态码小于200或大于等于300、返回体参数不完整。 */
public class ServiceException extends WechatPayException {

  private static final long serialVersionUID = -7174975090366956652L;

  private final HttpRequest httpRequest;
  private final int httpStatusCode;
  private final String responseBody;
  private String errorCode;
  private String errorMessage;

  /**
   * 返回状态码小于200或大于300调用
   *
   * @param httpRequest http请求
   * @param httpStatusCode http状态码
   * @param responseBody http返回体
   */
  public ServiceException(HttpRequest httpRequest, int httpStatusCode, String responseBody) {
    super(
        String.format(
            "Wrong HttpStatusCode[%d]%nhttpResponseBody[%.1024s]\tHttpRequest[%s]",
            httpStatusCode, responseBody, httpRequest));
    this.httpRequest = httpRequest;
    this.httpStatusCode = httpStatusCode;
    this.responseBody = responseBody;
    if (responseBody != null && !responseBody.isEmpty()) {
      JsonObject jsonObject = GsonUtil.getGson().fromJson(responseBody, JsonObject.class);
      JsonElement code = jsonObject.get("code");
      JsonElement message = jsonObject.get("message");
      this.errorCode = code == null ? null : code.getAsString();
      this.errorMessage = message == null ? null : message.getAsString();
    }
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

  /**
   * 获取HTTP返回体
   *
   * @return HTTP返回体
   */
  public String getResponseBody() {
    return responseBody;
  }

  /**
   * 获取HTTP状态码
   *
   * @return HTTP状态码
   */
  public int getHttpStatusCode() {
    return httpStatusCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
