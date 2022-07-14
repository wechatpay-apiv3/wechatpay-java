package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.util.GsonUtil;

/** HTTP JSON类型请求体 */
public final class JsonRequestBody implements RequestBody {

  private final String body;

  private JsonRequestBody(String body) {
    this.body = body;
  }

  /**
   * 获取请求体
   *
   * @return 请求体
   */
  public String getBody() {
    return body;
  }

  @Override
  public String getContentType() {
    return MediaType.APPLICATION_JSON.getValue();
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }

  public static class Builder {

    private String body;

    public Builder body(String body) {
      this.body = body;
      return this;
    }

    public JsonRequestBody build() {
      return new JsonRequestBody(requireNonNull(body));
    }
  }
}
