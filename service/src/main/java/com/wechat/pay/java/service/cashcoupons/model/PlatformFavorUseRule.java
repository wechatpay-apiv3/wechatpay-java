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

/** PlatformFavorUseRule */
public class PlatformFavorUseRule {
  /** 券可核销时间 说明：日期区间内可以使用优惠 */
  @SerializedName("available_time")
  private PlatformFavorAvailableTime availableTime;
  /** 面额 说明：券面额，单位分 */
  @SerializedName("coupon_amount")
  private Long couponAmount;
  /** 门槛 说明：使用券金额门槛，单位分 */
  @SerializedName("transaction_minimum")
  private Long transactionMinimum;

  public PlatformFavorAvailableTime getAvailableTime() {
    return availableTime;
  }

  public void setAvailableTime(PlatformFavorAvailableTime availableTime) {
    this.availableTime = availableTime;
  }

  public Long getCouponAmount() {
    return couponAmount;
  }

  public void setCouponAmount(Long couponAmount) {
    this.couponAmount = couponAmount;
  }

  public Long getTransactionMinimum() {
    return transactionMinimum;
  }

  public void setTransactionMinimum(Long transactionMinimum) {
    this.transactionMinimum = transactionMinimum;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlatformFavorUseRule {\n");
    sb.append("    availableTime: ").append(toIndentedString(availableTime)).append("\n");
    sb.append("    couponAmount: ").append(toIndentedString(couponAmount)).append("\n");
    sb.append("    transactionMinimum: ").append(toIndentedString(transactionMinimum)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
