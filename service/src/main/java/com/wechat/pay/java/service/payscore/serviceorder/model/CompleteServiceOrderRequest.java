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

/** CompleteServiceOrderRequest */
public class CompleteServiceOrderRequest {
  /** 商户服务订单号 说明：商户系统内部服务订单号（不是交易单号），要求32个字符内，只能是数字、大小写字母_-&#124;* 且在同一个商户号下唯一。详见[商户订单号]( */
  @SerializedName("out_order_no")
  @Expose(serialize = false)
  private String outOrderNo;
  /** 公众账号ID 说明：数据 */
  @SerializedName("appid")
  private String appid;
  /** 服务订单号 说明：服务订单的主键，唯一定义此资源的标识 */
  @SerializedName("service_id")
  private String serviceId;
  /** 后付费项目 说明：付费项目列表，最少包含一条，最多包含100条付费项目 创建订单接口传入的【后付费】将失效，以本接口传入的【后付费】为准 未使用服务，取消订单时，不可填写该字段 */
  @SerializedName("post_payments")
  private List<Payment> postPayments = new ArrayList<Payment>();
  /** 商户优惠 说明：商户优惠列表，最多包含5条商户优惠 创建订单接口传入的【商户优惠】将失效，以本接口传入的【商户优惠】为准 未使用服务，取消订单时，不可填写该字段 */
  @SerializedName("post_discounts")
  private List<ServiceOrderCoupon> postDiscounts;
  /**
   * 总金额 说明：1.金额：数字，必须≥0（单位：分） 2.总金额 =（完结付费项目1…+完结付费项目n）-（完结商户优惠项目1…+完结商户优惠项目n） 3.总金额上限
   * 1）【评估不通过：交押金】模式：总金额≤创单时填写的“订单风险金额” 2）【评估不通过：拒绝】模式：总金额≤“服务风险金额”
   */
  @SerializedName("total_amount")
  private Long totalAmount;
  /** 微信支付服务分账标记 说明：完结订单分账接口标记 false-不分账，true-分账，默认：false */
  @SerializedName("profit_sharing")
  private Boolean profitSharing;
  /**
   * 完结服务时间 说明：时间使用ISO 8601所定义的格式。 示例： - YYYY-MM-DDTHH:mm:ss.SSSZ - YYYY-MM-DDTHH:mm:ssZ -
   * YYYY-MM-DDTHH:mm:ss.SSS+08:00 - YYYY-MM-DDTHH:mm:ss+08:00
   */
  @SerializedName("complete_time")
  private String completeTime;
  /** 商户号 说明：直连商户号：平台商必填 */
  @SerializedName("mchid")
  private String mchid;
  /** 订单优惠标记 说明：订单优惠标记，代金券或立减金优惠的参数，说明详见代金券或立减金优惠 */
  @SerializedName("goods_tag")
  private String goodsTag;
  /** 实际服务位置 说明： */
  @SerializedName("location")
  private Location location;
  /** 实际服务时间段 说明： */
  @SerializedName("time_range")
  private TimeRange timeRange;

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

  public Boolean getProfitSharing() {
    return profitSharing;
  }

  public void setProfitSharing(Boolean profitSharing) {
    this.profitSharing = profitSharing;
  }

  public String getCompleteTime() {
    return completeTime;
  }

  public void setCompleteTime(String completeTime) {
    this.completeTime = completeTime;
  }

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  public String getGoodsTag() {
    return goodsTag;
  }

  public void setGoodsTag(String goodsTag) {
    this.goodsTag = goodsTag;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public TimeRange getTimeRange() {
    return timeRange;
  }

  public void setTimeRange(TimeRange timeRange) {
    this.timeRange = timeRange;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
