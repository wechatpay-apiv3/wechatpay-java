package com.wechat.pay.java.core.model;

import com.google.gson.annotations.Expose;
import com.wechat.pay.java.core.util.GsonUtil;

public class TestServiceRequest {

  @Expose(serialize = false)
  private String mchid;

  private String appid;
  private String outTradeNo;

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
