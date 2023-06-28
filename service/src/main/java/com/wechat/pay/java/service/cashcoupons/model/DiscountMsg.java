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

/** DiscountMsg */
public class DiscountMsg {
  /** 最高折扣金额 说明：最高折扣金额，单位分 */
  @SerializedName("discount_amount_max")
  private Long discountAmountMax;
  /** 折扣百分比 说明：折扣百分比，例如88-八八折 */
  @SerializedName("discount_percent")
  private Integer discountPercent;
  /** 门槛 说明：使用券金额门槛，单位分 */
  @SerializedName("transaction_minimum")
  private Long transactionMinimum;

  public Long getDiscountAmountMax() {
    return discountAmountMax;
  }

  public void setDiscountAmountMax(Long discountAmountMax) {
    this.discountAmountMax = discountAmountMax;
  }

  public Integer getDiscountPercent() {
    return discountPercent;
  }

  public void setDiscountPercent(Integer discountPercent) {
    this.discountPercent = discountPercent;
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
    sb.append("class DiscountMsg {\n");
    sb.append("    discountAmountMax: ").append(toIndentedString(discountAmountMax)).append("\n");
    sb.append("    discountPercent: ").append(toIndentedString(discountPercent)).append("\n");
    sb.append("    transactionMinimum: ").append(toIndentedString(transactionMinimum)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
