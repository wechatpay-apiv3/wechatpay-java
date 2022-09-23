// Copyright 2021 Tencent Inc. All rights reserved.
//
// H5支付
//
// H5支付API
//
// API version: 1.2.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.payment.h5.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** PrepayResponse */
public class PrepayResponse {
  /** h5Url 说明：支付跳转链接 */
  @SerializedName("h5_url")
  private String h5Url;

  public String getH5Url() {
    return h5Url;
  }

  public void setH5Url(String h5Url) {
    this.h5Url = h5Url;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrepayResponse {\n");
    sb.append("    h5Url: ").append(toIndentedString(h5Url)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
