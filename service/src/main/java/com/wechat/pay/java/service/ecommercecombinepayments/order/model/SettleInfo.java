// Copyright 2021 Tencent Inc. All rights reserved.
//
// APP支付
//
// APP支付API
//
// API version: 1.2.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.ecommercecombinepayments.order.model;


import com.google.gson.annotations.SerializedName;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

/** SettleInfo */
public class SettleInfo {
  /** profitSharing 说明：是否指定分账 */
  @SerializedName("profit_sharing")
  private Boolean profitSharing;

  /**
   * 补差金额
   * SettleInfo.profit_sharing为true时，该金额才生效。
   注意：单笔订单最高补差金额为5000元
   */
  @SerializedName("subsidy_amount")
  private Integer subsidyAmount;



  public Boolean getProfitSharing() {
    return profitSharing;
  }

  public void setProfitSharing(Boolean profitSharing) {
    this.profitSharing = profitSharing;
  }

  public Integer getSubsidyAmount() {
    return subsidyAmount;
  }

  public void setSubsidyAmount(Integer subsidyAmount) {
    this.subsidyAmount = subsidyAmount;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SettleInfo {\n");
    sb.append("    profitSharing: ").append(toIndentedString(profitSharing)).append("\n");
    sb.append("    subsidyAmount: ").append(toIndentedString(subsidyAmount)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
