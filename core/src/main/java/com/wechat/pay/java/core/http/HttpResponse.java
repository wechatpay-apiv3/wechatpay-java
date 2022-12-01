package com.wechat.pay.java.core.http;

import com.google.gson.JsonSyntaxException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.util.GsonUtil;
import java.util.Objects;

/**
 * HTTP返回
 *
 * @param <T> 业务返回体类型
 */
public final class HttpResponse<T> {

  private final HttpRequest request;
  private final HttpHeaders headers;
  private final ResponseBody body;
  private final T serviceResponse;

  private HttpResponse(
      HttpRequest request, HttpHeaders headers, ResponseBody body, T serviceResponse) {
    this.request = request;
    this.headers = headers;
    this.body = body;
    this.serviceResponse = serviceResponse;
  }

  /**
   * 获取返回头
   *
   * @return 返回头
   */
  public HttpHeaders getHeaders() {
    return headers;
  }

  /**
   * 获取返回对应的请求
   *
   * @return 请求
   */
  public HttpRequest getRequest() {
    return request;
  }

  /**
   * 获取返回体
   *
   * @return 返回体
   */
  public ResponseBody getBody() {
    return body;
  }

  /**
   * 获取业务返回
   *
   * @return 业务返回
   */
  public T getServiceResponse() {
    return serviceResponse;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }

  public static class Builder<T> {

    private OriginalResponse originalResponse;
    private Class<T> serviceResponseType;

    public Builder<T> originalResponse(OriginalResponse originalResponse) {
      this.originalResponse = originalResponse;
      return this;
    }

    public Builder<T> serviceResponseType(Class<T> serviceResponseType) {
      this.serviceResponseType = serviceResponseType;
      return this;
    }

    /**
     * 构造 HttpResponse
     *
     * @return httpResponse
     */
    public HttpResponse<T> build() {
      Objects.requireNonNull(originalResponse);
      if (originalResponse.getBody() == null
          || originalResponse.getBody().isEmpty()
          || serviceResponseType == null) {
        return new HttpResponse<>(
            originalResponse.getRequest(), originalResponse.getHeaders(), null, null);
      }
      ResponseBody body = new JsonResponseBody.Builder().body(originalResponse.getBody()).build();
      T serviceResponse;
      try {
        serviceResponse =
            GsonUtil.getGson().fromJson(originalResponse.getBody(), serviceResponseType);
      } catch (JsonSyntaxException e) {
        throw new MalformedMessageException(
            String.format("Invalid json response body[%s]", originalResponse.getBody()), e);
      }
      return new HttpResponse<>(
          originalResponse.getRequest(), originalResponse.getHeaders(), body, serviceResponse);
    }
  }
}
