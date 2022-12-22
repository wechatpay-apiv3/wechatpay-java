package com.wechat.pay.java.core.http;

import java.io.InputStream;
import java.util.Map;

public class OriginFileResponse {

  private final HttpHeaders headers;
  private final HttpRequest request;
  private final int statusCode;
  private final InputStream bodyStream;

  private OriginFileResponse(
      HttpHeaders headers, HttpRequest request, int statusCode, InputStream bodyStream) {
    this.headers = headers;
    this.request = request;
    this.statusCode = statusCode;
    this.bodyStream = bodyStream;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public HttpRequest getRequest() {
    return request;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public InputStream getBodyStream() {
    return bodyStream;
  }

  public static class Builder {

    private HttpHeaders headers;

    private HttpRequest request;
    private int statusCode;
    private InputStream bodyStream;

    public Builder headers(Map<String, String> headers) {
      this.headers = new HttpHeaders(headers);
      return this;
    }

    public Builder request(HttpRequest request) {
      this.request = request;
      return this;
    }

    public Builder statusCode(int statusCode) {
      this.statusCode = statusCode;
      return this;
    }

    public Builder bodyStream(InputStream bodyStream) {
      this.bodyStream = bodyStream;
      return this;
    }

    public OriginFileResponse build() {
      return new OriginFileResponse(headers, request, statusCode, bodyStream);
    }
  }
}
