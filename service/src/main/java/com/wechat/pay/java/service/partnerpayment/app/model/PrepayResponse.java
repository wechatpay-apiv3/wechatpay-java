// Copyright 2021 Tencent Inc. All rights reserved.
//
// APP支付
//
// APP支付API
//
// API version: 1.2.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.partnerpayment.app.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** PrepayResponse */
public class PrepayResponse {
  /** prepayId 说明：预支付交易会话标识 */
  @SerializedName("prepay_id")
  private String prepayId;

  public String getPrepayId() {
    return prepayId;
  }

  public void setPrepayId(String prepayId) {
    this.prepayId = prepayId;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrepayResponse {\n");
    sb.append("    prepayId: ").append(toIndentedString(prepayId)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
