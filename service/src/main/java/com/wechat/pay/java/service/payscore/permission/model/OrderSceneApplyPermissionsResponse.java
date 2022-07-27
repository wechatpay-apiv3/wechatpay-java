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

/** OrderSceneApplyPermissionsResponse */
public class OrderSceneApplyPermissionsResponse {
  /** 申请许可token 说明：用于跳转到微信侧小程序授权数据, 跳转到微信侧小程序传入 */
  @SerializedName("apply_permissions_token")
  private String applyPermissionsToken;

  public String getApplyPermissionsToken() {
    return applyPermissionsToken;
  }

  public void setApplyPermissionsToken(String applyPermissionsToken) {
    this.applyPermissionsToken = applyPermissionsToken;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}