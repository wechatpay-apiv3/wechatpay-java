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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wechat.pay.java.core.util.GsonUtil;
import java.util.ArrayList;
import java.util.List;

/** ModifyServiceOrderRequest */
public class ModifyServiceOrderRequest {
  /** 商户服务订单号 说明：商户系统内部订单号（不是交易单号），要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号 需要和创建订单的商户服务订单号一致 */
  @SerializedName("out_order_no")
  @Expose(serialize = false)
  private String outOrderNo;
  /** 公众账号ID 说明：微信公众平台分配的与传入的商户号建立了支付绑定关系的appid，可在公众平台查看绑定关系 需要在本系统先进行配置. 需要与创建订单时保持一致. */
  @SerializedName("appid")
  private String appid;
  /** 服务ID 说明：该服务ID有本接口对应产品的权限 需要与创建订单时保持一致 */
  @SerializedName("service_id")
  private String serviceId;
  /** 后付费项目 */
  @SerializedName("post_payments")
  private List<Payment> postPayments = new ArrayList<Payment>();
  /** 后付费商户优惠 */
  @SerializedName("post_discounts")
  private List<ServiceOrderCoupon> postDiscounts;
  /** 总金额 说明：总金额,单位:分 不能超过完结订单时候的总金额 */
  @SerializedName("total_amount")
  private Long totalAmount;
  /** 修改原因 说明：支持50个字符，按照字符计算 超过长度报错处理 */
  @SerializedName("reason")
  private String reason;
  /** 商户号 说明：直连商户号：平台商必填 */
  @SerializedName("mchid")
  private String mchid;

  public String getOutOrderNo() {
    return outOrderNo;
  }

  public void setOutOrderNo(String outOrderNo) {
    this.outOrderNo = outOrderNo;
  }

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

  public List<Payment> getPostPayments() {
    return postPayments;
  }

  public void setPostPayments(List<Payment> postPayments) {
    this.postPayments = postPayments;
  }

  public List<ServiceOrderCoupon> getPostDiscounts() {
    return postDiscounts;
  }

  public void setPostDiscounts(List<ServiceOrderCoupon> postDiscounts) {
    this.postDiscounts = postDiscounts;
  }

  public Long getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Long totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

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
