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

/** OrderSceneInfo */
public class OrderSceneInfo {
  /** 订单展示金额 说明：订单展示金额, 单位分. 当不需要展示订单金额时, 可不填写 */
  @SerializedName("order_amount")
  private Long orderAmount;

  public Long getOrderAmount() {
    return orderAmount;
  }

  public void setOrderAmount(Long orderAmount) {
    this.orderAmount = orderAmount;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}