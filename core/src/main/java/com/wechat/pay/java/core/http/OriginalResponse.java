package com.wechat.pay.java.core.http;

import java.util.Map;
import java.util.Objects;

public class OriginalResponse {

  private final HttpHeaders headers;
  private final HttpRequest request;
  private final String contentType;
  private final int statusCode;
  private final String body;

  private OriginalResponse(
      HttpRequest request, int statusCode, HttpHeaders headers, String contentType, String body) {
    this.request = request;
    this.contentType = contentType;
    this.headers = headers;
    this.statusCode = statusCode;
    this.body = body;
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
  public String getBody() {
    return body;
  }

  public String getContentType() {
    return contentType;
  }

  public int getStatusCode() {
    return statusCode;
  }

  /** OriginalResponse Builder */
  public static class Builder {

    private HttpRequest request;
    private int statusCode;
    private HttpHeaders headers;
    private String contentType;

    private String jsonBody;

    /**
     * 设置返回contentType
     *
     * @param contentType contentType
     * @return OriginalResponse Builder
     */
    public Builder contentType(String contentType) {
      this.contentType = contentType;
      return this;
    }

    /**
     * 设置返回headers
     *
     * @param headers contentType
     * @return OriginalResponse Builder
     */
    public Builder headers(Map<String, String> headers) {
      this.headers = new HttpHeaders(headers);
      return this;
    }

    /**
     * 设置request
     *
     * @param request request
     * @return OriginalResponse Builder
     */
    public Builder request(HttpRequest request) {
      this.request = request;
      return this;
    }

    /**
     * 设置返回statusCode
     *
     * @param statusCode statusCode
     * @return OriginalResponse Builder
     */
    public Builder statusCode(int statusCode) {
      this.statusCode = statusCode;
      return this;
    }

    /**
     * 设置返回body
     *
     * @param jsonBody jsonBody
     * @return OriginalResponse Builder
     */
    public Builder body(String jsonBody) {
      this.jsonBody = jsonBody;
      return this;
    }

    /**
     * 构建 OriginalResponse
     *
     * @return OriginalResponse
     */
    public OriginalResponse build() {
      Objects.requireNonNull(request);
      return new OriginalResponse(request, statusCode, headers, contentType, jsonBody);
    }
  }
}
