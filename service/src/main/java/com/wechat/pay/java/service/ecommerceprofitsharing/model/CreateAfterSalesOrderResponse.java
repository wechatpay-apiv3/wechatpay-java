// Copyright 2021 Tencent Inc. All rights reserved.
//
// 微信支付电商平台分账API
//
// 微信支付电商平台分账API
//
// API version: 1.0.21

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.ecommerceprofitsharing.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** CreateAfterSalesOrderResponse */
public class CreateAfterSalesOrderResponse {
  /** 子商户号 说明：分账出资的商户 */
  @SerializedName("sub_mchid")
  private String subMchid;

  /** 微信订单号 说明：微信支付订单号 */
  @SerializedName("transaction_id")
  private String transactionId;

  /** 分账金额 说明：分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额 */
  @SerializedName("amount")
  private Long amount;

  public String getSubMchid() {
    return subMchid;
  }

  public void setSubMchid(String subMchid) {
    this.subMchid = subMchid;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateAfterSalesOrderResponse {\n");
    sb.append("    subMchid: ").append(toIndentedString(subMchid)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
