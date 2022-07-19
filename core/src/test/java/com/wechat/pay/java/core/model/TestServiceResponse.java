package com.wechat.pay.java.core.model;

import com.wechat.pay.java.core.util.GsonUtil;

public class TestServiceResponse {

  private String requestId;

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
