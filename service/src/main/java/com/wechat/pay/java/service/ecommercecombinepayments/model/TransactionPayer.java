// Copyright 2021 Tencent Inc. All rights reserved.
//
// APP支付
//
// APP支付API
//
// API version: 1.2.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.ecommercecombinepayments.model;

import com.google.gson.annotations.SerializedName;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

/** TransactionPayer */
public class TransactionPayer {
  /** spOpenid */
  @SerializedName("sp_openid")
  private String spOpenid;

  /** subOpenid */
  @SerializedName("sub_openid")
  private String subOpenid;

  public String getSpOpenid() {
    return spOpenid;
  }

  public void setSpOpenid(String spOpenid) {
    this.spOpenid = spOpenid;
  }

  public String getSubOpenid() {
    return subOpenid;
  }

  public void setSubOpenid(String subOpenid) {
    this.subOpenid = subOpenid;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionPayer {\n");
    sb.append("    spOpenid: ").append(toIndentedString(spOpenid)).append("\n");
    sb.append("    subOpenid: ").append(toIndentedString(subOpenid)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
