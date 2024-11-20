package com.wechat.pay.java.service.ecommercecombinepayments.jsapi.model;

import com.google.gson.annotations.SerializedName;

public class CombinePayerInfo {

  @SerializedName("openid")
  private String openid;

  @SerializedName("sub_openid")
  private String subOpenid;

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getSubOpenid() {
    return subOpenid;
  }

  public void setSubOpenid(String subOpenid) {
    this.subOpenid = subOpenid;
  }

  @Override
  public String toString() {
    return "CombinePayerInfo{" +
        "openid='" + openid + '\'' +
        "subOpenid='" + subOpenid + '\'' +
        '}';
  }
}
