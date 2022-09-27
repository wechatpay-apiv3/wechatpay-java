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

/** QueryOrderByIdRequest */
public class QueryOrderByIdRequest {
  /** transactionId 说明：微信支付订单号 */
  @SerializedName("transaction_id")
  @Expose(serialize = false)
  private String transactionId;
  /** spMchid 说明：服务商户号 */
  @SerializedName("sp_mchid")
  @Expose(serialize = false)
  private String spMchid;
  /** subMchid 说明：子商户号 */
  @SerializedName("sub_mchid")
  @Expose(serialize = false)
  private String subMchid;

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
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
    sb.append("class QueryOrderByIdRequest {\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    spMchid: ").append(toIndentedString(spMchid)).append("\n");
    sb.append("    subMchid: ").append(toIndentedString(subMchid)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
