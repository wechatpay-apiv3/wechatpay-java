package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.util.GsonUtil;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/** HTTP请求 */
public final class HttpRequest {

  private final HttpMethod httpMethod;
  private final URL url;
  private final URI uri;
  private final HttpHeaders headers;
  private final RequestBody body;

  private HttpRequest(
      HttpMethod httpMethod, URL url, URI uri, HttpHeaders headers, RequestBody body) {
    this.httpMethod = httpMethod;
    this.url = url;
    this.uri = uri;
    this.headers = headers;
    this.body = body;
  }

  /**
   * 获取HttpMethod
   *
   * @return httpMethod
   */
  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  /**
   * 获取请求URL
   *
   * @return 请求URL
   */
  public URL getUrl() {
    return url;
  }

  /**
   * 获取请求URI
   *
   * @return 请求uri
   */
  public URI getUri() {
    return uri;
  }

  /**
   * 获取请求头
   *
   * @return 请求头
   */
  public HttpHeaders getHeaders() {
    return headers;
  }

  /**
   * 获取请求体
   *
   * @return 请求体
   */
  public RequestBody getBody() {
    return body;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }

  public static class Builder {

    HttpMethod httpMethod;
    private URL url;
    private HttpHeaders headers = new HttpHeaders();
    private RequestBody body;

    /**
     * 设置httpMethod
     *
     * @param httpMethod httpMethod
     * @return Builder
     */
    public Builder httpMethod(HttpMethod httpMethod) {
      this.httpMethod = httpMethod;
      return this;
    }

    /**
     * 设置url
     *
     * @param url url
     * @return Builder
     */
    public Builder url(URL url) {
      this.url = url;
      return this;
    }

    /**
     * 设置url
     *
     * @param urlString urlString
     * @return Builder
     */
    public Builder url(String urlString) {
      try {
        this.url = new URL(urlString);
        return this;
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException(
            "The urlString passed in when building httpRequest is invalid:" + urlString);
      }
    }

    /**
     * 重置headers
     *
     * @param headers headers
     * @return Builder
     */
    public Builder headers(HttpHeaders headers) {
      this.headers = headers;
      return this;
    }

    /**
     * 添加header
     *
     * @param name name
     * @param value value
     * @return Builder
     */
    public Builder addHeader(String name, String value) {
      headers.addHeader(name, value);
      return this;
    }

    /**
     * 设置body
     *
     * @param body body
     * @return Builder
     */
    public Builder body(RequestBody body) {
      this.body = body;
      return this;
    }

    /**
     * 构建HttpRequest
     *
     * @return HttpRequest
     * @throws HttpException url转uri失败
     */
    public HttpRequest build() {
      try {
        return new HttpRequest(
            requireNonNull(httpMethod),
            requireNonNull(url),
            url.toURI(),
            headers == null ? new HttpHeaders() : headers,
            body);
      } catch (URISyntaxException e) {
        throw new HttpException(String.format("Parse url[%s] to uri failed.", url), e);
      }
    }
  }
}
