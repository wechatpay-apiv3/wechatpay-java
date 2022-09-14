package com.wechat.pay.java.core.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class QueryParameter {

  private final Map<String, Object> params = new HashMap<>();

  public void add(String key, Object value) {
    params.put(key, value);
  }

  public String getQueryStr() {
    boolean isFirstQueryParam = true;
    StringBuilder queryStrBuilder = new StringBuilder();
    for (Entry<String, Object> entry : params.entrySet()) {
      if (isFirstQueryParam) {
        queryStrBuilder.append("?").append(entry.getKey()).append("=").append(entry.getValue());
        isFirstQueryParam = false;
        continue;
      }
      queryStrBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
    }
    return queryStrBuilder.toString();
  }
}
