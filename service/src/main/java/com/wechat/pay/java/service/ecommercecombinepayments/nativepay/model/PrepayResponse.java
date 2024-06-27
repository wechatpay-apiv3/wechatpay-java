// Copyright 2021 Tencent Inc. All rights reserved.
//
// Native支付
//
// Native支付API
//
// API version: 1.2.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.ecommercecombinepayments.nativepay.model;

import com.google.gson.annotations.SerializedName;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

/** PrepayResponse */
public class PrepayResponse {
  /** codeUrl 说明：二维码链接 */
  @SerializedName("code_url")
  private String codeUrl;

  public String getCodeUrl() {
    return codeUrl;
  }

  public void setCodeUrl(String codeUrl) {
    this.codeUrl = codeUrl;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrepayResponse {\n");
    sb.append("    codeUrl: ").append(toIndentedString(codeUrl)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
