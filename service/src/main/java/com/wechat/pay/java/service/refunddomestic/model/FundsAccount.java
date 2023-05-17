// Copyright 2021 Tencent Inc. All rights reserved.
//
// 境内普通商户退款API
//
// 境内普通商户退款功能涉及的API文档
//
// API version: 1.2.2

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.refunddomestic.model;

import com.google.gson.annotations.SerializedName;

/** FundsAccount */
public enum FundsAccount {
  @SerializedName("UNSETTLED")
  UNSETTLED,

  @SerializedName("AVAILABLE")
  AVAILABLE,

  @SerializedName("UNAVAILABLE")
  UNAVAILABLE,

  @SerializedName("OPERATION")
  OPERATION,

  @SerializedName("BASIC")
  BASIC,

  @SerializedName("ECNY_BASIC")
  ECNY_BASIC
}