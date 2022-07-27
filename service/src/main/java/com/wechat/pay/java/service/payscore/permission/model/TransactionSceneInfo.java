// Copyright 2021 Tencent Inc. All rights reserved.
//
// 支付分授权对外API
//
// 支付分授权领域对外API
//
// API version: 1.4.0

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.payscore.permission.model;

import com.google.gson.annotations.SerializedName;
import com.wechat.pay.java.core.util.GsonUtil;

/** TransactionSceneInfo */
public class TransactionSceneInfo {
  /** 支付下单的商户号 说明：支付下单的商户号 */
  @SerializedName("mchid")
  private String mchid;

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}