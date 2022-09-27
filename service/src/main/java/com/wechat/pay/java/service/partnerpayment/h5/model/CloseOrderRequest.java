// Copyright 2021 Tencent Inc. All rights reserved.
//
// H5支付
//
// H5支付API
//
// API version: 1.2.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.partnerpayment.h5.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/** CloseOrderRequest */
public class CloseOrderRequest {
  /** outTradeNo 说明：商户订单号 */
  @SerializedName("out_trade_no")
  @Expose(serialize = false)
  private String outTradeNo;
  /** 服务商户号 说明：服务商户号，由微信支付生成并下发 */
  @SerializedName("sp_mchid")
  private String spMchid;
  /** 子商户号 说明：子商户的商户号，由微信支付生成并下发 */
  @SerializedName("sub_mchid")
  private String subMchid;

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  public String getSpMchid() {
    return spMchid;
  }

  public void setSpMchid(String spMchid) {
    this.spMchid = spMchid;
  }

  public String getSubMchid() {
    return subMchid;
  }

  public void setSubMchid(String subMchid) {
    this.subMchid = subMchid;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CloseOrderRequest {\n");
    sb.append("    outTradeNo: ").append(toIndentedString(outTradeNo)).append("\n");
    sb.append("    spMchid: ").append(toIndentedString(spMchid)).append("\n");
    sb.append("    subMchid: ").append(toIndentedString(subMchid)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
