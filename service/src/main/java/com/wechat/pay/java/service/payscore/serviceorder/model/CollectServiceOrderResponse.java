// Copyright 2021 Tencent Inc. All rights reserved.
//
// 支付分对外API
//
// 包含通用化api及先享后付api
//
// API version: 1.6.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.payscore.serviceorder.model;

import com.google.gson.annotations.SerializedName;
import com.wechat.pay.java.core.util.GsonUtil;

/** CollectServiceOrderResponse */
public class CollectServiceOrderResponse {
  /** 公众账号ID */
  @SerializedName("appid")
  private String appid;
  /** 商户号 说明：调用接口提交的商户号 */
  @SerializedName("mchid")
  private String mchid;
  /** 商户订单号 说明：调用接口提交的商户服务订单号 */
  @SerializedName("out_order_no")
  private String outOrderNo;
  /** 服务ID 说明：调用该接口提交的服务ID */
  @SerializedName("service_id")
  private String serviceId;
  /** 微信支付服务订单号 说明：每个微信支付服务订单号与商户号下对应的商户服务订单号一一对应 */
  @SerializedName("order_id")
  private String orderId;

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  public String getOutOrderNo() {
    return outOrderNo;
  }

  public void setOutOrderNo(String outOrderNo) {
    this.outOrderNo = outOrderNo;
  }

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}