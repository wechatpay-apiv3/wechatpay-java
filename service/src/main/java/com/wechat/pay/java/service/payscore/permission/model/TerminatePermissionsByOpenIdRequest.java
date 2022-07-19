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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wechat.pay.java.core.util.GsonUtil;

/** TerminatePermissionsByOpenIdRequest */
public class TerminatePermissionsByOpenIdRequest {
  /** 服务ID 说明：该服务ID有本接口对应产品的权限. */
  @SerializedName("service_id")
  private String serviceId;
  /** 公众号ID 说明：微信公众平台分配的与传入的商户号建立了支付绑定关系的AppID，可在公众平台查看绑定关系，此参数需在本系统先进行配置。 */
  @SerializedName("appid")
  private String appid;
  /** 用户标识 说明：AppID下的用户标识 */
  @SerializedName("openid")
  @Expose(serialize = false)
  private String openid;
  /** 撤销原因 说明：解除授权原因 */
  @SerializedName("reason")
  private String reason;

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
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
