package com.wechat.pay.java.service.ecommercecombinepayments.jsapi.model;

import com.google.gson.annotations.SerializedName;

public class CombinePayerInfo {

  @SerializedName("openid")
  private String openid;

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  @Override
  public String toString() {
    return "CombinePayerInfo{" +
        "openid='" + openid + '\'' +
        '}';
  }
}
