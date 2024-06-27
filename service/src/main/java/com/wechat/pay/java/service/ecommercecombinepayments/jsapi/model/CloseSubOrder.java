package com.wechat.pay.java.service.ecommercecombinepayments.jsapi.model;

import com.google.gson.annotations.SerializedName;

public class CloseSubOrder {

  /**
   * 子单商户号
   */
  @SerializedName("mchid")
  private String mchid;

  /**
   * 子单商户订单号
   */
  @SerializedName("out_trade_no")
  private String outTradeNo;

  /**
   * 二级商户号
   */
  @SerializedName("sub_mchid")
  private String subMchid;

  /**
   * 子商户应用ID
   */
  @SerializedName("sub_appid")
  private String subAppid;

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  public String getSubMchid() {
    return subMchid;
  }

  public void setSubMchid(String subMchid) {
    this.subMchid = subMchid;
  }

  public String getSubAppid() {
    return subAppid;
  }

  public void setSubAppid(String subAppid) {
    this.subAppid = subAppid;
  }
}
