package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/** HTTP请求头 */
public class HttpHeaders {

  private final Map<String, String> headers = new HashMap<>();

  public HttpHeaders() {}

  public HttpHeaders(Map<String, String> headers) {
    this.headers.putAll(headers);
  }

  /**
   * 增加请求头参数
   *
   * @param name 参数名称
   * @param value 参数值
   */
  public void addHeader(String name, String value) {
    headers.put(requireNonNull(name), requireNonNull(value));
  }

  /**
   * 获取请求头参数
   *
   * @param name 参数名称
   * @return 参数值
   */
  public String getHeader(String name) {
    return headers.get(name);
  }

  /**
   * 获取请求头
   *
   * @return 请求头
   */
  public Map<String, String> getHeaders() {
    return new HashMap<>(headers);
  }

  @Override
  public String toString() {
    if (headers.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (Entry<String, String> entry : headers.entrySet()) {
      sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
    }
    sb.delete(sb.length() - 1, sb.length()).append("\n");
    return sb.toString();
  }
}
