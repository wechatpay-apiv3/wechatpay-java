// Copyright 2021 Tencent Inc. All rights reserved.
//
// 微信支付营销系统开放API
//
// 新增立减金api
//
// API version: 3.4.0

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.cashcoupons.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** StatisticalData */
public class StatisticalData {
  /** 已发券张数 说明：批次已发放券张数 */
  @SerializedName("send_coupons_count")
  private Long sendCouponsCount;
  /** 已核销券张数 说明：批次已核销券张数 */
  @SerializedName("consume_coupons_count")
  private Long consumeCouponsCount;

  public Long getSendCouponsCount() {
    return sendCouponsCount;
  }

  public void setSendCouponsCount(Long sendCouponsCount) {
    this.sendCouponsCount = sendCouponsCount;
  }

  public Long getConsumeCouponsCount() {
    return consumeCouponsCount;
  }

  public void setConsumeCouponsCount(Long consumeCouponsCount) {
    this.consumeCouponsCount = consumeCouponsCount;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticalData {\n");
    sb.append("    sendCouponsCount: ").append(toIndentedString(sendCouponsCount)).append("\n");
    sb.append("    consumeCouponsCount: ")
        .append(toIndentedString(consumeCouponsCount))
        .append("\n");
    sb.append("}");
    return sb.toString();
  }
}
