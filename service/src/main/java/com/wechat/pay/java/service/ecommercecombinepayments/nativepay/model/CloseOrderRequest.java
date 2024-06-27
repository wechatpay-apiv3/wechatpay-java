// Copyright 2021 Tencent Inc. All rights reserved.
//
// APP支付
//
// APP支付API
//
// API version: 1.2.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.ecommercecombinepayments.nativepay.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

/** CloseOrderRequest */
public class CloseOrderRequest {
  /** combineOutTradeNo 说明：合单商户订单号 */
  @SerializedName("combine_out_trade_no")
  @Expose(serialize = false)
  private String combineOutTradeNo;

  /** 子单信息 最多支持子单条数：50 */
  @SerializedName("sub_orders")
  private List<CloseSubOrder> subOrders;

  /** 合单商户appid 说明：合单发起方的appid */
  @SerializedName("combine_appid")
  private String combineAppid;

  public String getCombineOutTradeNo() {
    return combineOutTradeNo;
  }

  public void setCombineOutTradeNo(String combineOutTradeNo) {
    this.combineOutTradeNo = combineOutTradeNo;
  }

  public List<CloseSubOrder> getSubOrders() {
    return subOrders;
  }

  public void setSubOrders(List<CloseSubOrder> subOrders) {
    this.subOrders = subOrders;
  }

  public String getCombineAppid() {
    return combineAppid;
  }

  public void setCombineAppid(String combineAppid) {
    this.combineAppid = combineAppid;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CloseOrderRequest {\n");
    sb.append("    combineOutTradeNo: ").append(toIndentedString(combineOutTradeNo)).append("\n");
    sb.append("    subOrders: ").append(toIndentedString(subOrders)).append("\n");
    sb.append("    subMchid: ").append(toIndentedString(combineAppid)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
