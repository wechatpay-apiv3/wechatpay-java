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

/** SyncServiceOrderBody */
public class SyncServiceOrderBody {
  /** 公众账号ID 说明：微信公众平台分配的与传入的商户号建立了支付绑定关系的appid，可在公众平台查看绑定关系 需要在本系统先进行配置. 需要与创建订单时保持一致. */
  @SerializedName("appid")
  private String appid;
  /** 服务ID 说明：该服务ID有本接口对应产品的权限 需要与创建订单时保持一致 */
  @SerializedName("service_id")
  private String serviceId;
  /** 场景类型 说明：场景类型- \"Order_Paid\"：字符串表示“订单收款成功” 场景类型- \"Order_Paying\": 字符串表示“订单收款同步” */
  @SerializedName("type")
  private String type;
  /** 商户号 说明：直连商户号：平台商必填 */
  @SerializedName("mchid")
  private String mchid;
  /** 内容信息详情 说明：场景类型为：Order_Paid 时，为必填项； */
  @SerializedName("detail")
  private SyncDetail detail;

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  public SyncDetail getDetail() {
    return detail;
  }

  public void setDetail(SyncDetail detail) {
    this.detail = detail;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}