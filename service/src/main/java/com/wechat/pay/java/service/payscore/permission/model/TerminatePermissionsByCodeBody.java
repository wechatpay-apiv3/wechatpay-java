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

/** TerminatePermissionsByCodeBody */
public class TerminatePermissionsByCodeBody {
  /** 服务ID 说明：该服务ID有本接口对应产品的权限。 */
  @SerializedName("service_id")
  private String serviceId;
  /** 撤销原因 说明：解除授权原因 */
  @SerializedName("reason")
  private String reason;

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
