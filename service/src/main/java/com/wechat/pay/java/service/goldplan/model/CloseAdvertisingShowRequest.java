// Copyright 2021 Tencent Inc. All rights reserved.
//
// 点金计划对外API
//
// 特约商户点金计划管理API
//
// API version: 0.3.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.goldplan.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** CloseAdvertisingShowRequest */
public class CloseAdvertisingShowRequest {
  /** 特约商户号 说明：需要关闭广告展示的特约商户号，由微信支付生成并下发。 */
  @SerializedName("sub_mchid")
  private String subMchid;

  public String getSubMchid() {
    return subMchid;
  }

  public void setSubMchid(String subMchid) {
    this.subMchid = subMchid;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CloseAdvertisingShowRequest {\n");
    sb.append("    subMchid: ").append(toIndentedString(subMchid)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
