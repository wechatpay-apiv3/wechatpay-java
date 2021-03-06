// Copyright 2021 Tencent Inc. All rights reserved.
//
// Native支付
//
// Native支付API
//
// API version: 1.2.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.payment.nativepay.model;

import com.google.gson.annotations.SerializedName;
import com.wechat.pay.java.core.util.GsonUtil;

/** SettleInfo */
public class SettleInfo {
  /** profitSharing 说明：是否指定分账 */
  @SerializedName("profit_sharing")
  private Boolean profitSharing;

  public Boolean getProfitSharing() {
    return profitSharing;
  }

  public void setProfitSharing(Boolean profitSharing) {
    this.profitSharing = profitSharing;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
