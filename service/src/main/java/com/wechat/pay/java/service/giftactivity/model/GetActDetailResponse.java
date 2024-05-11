// Copyright 2021 Tencent Inc. All rights reserved.
//
// 支付有礼活动对外API
//
// No description provided (generated by Openapi Generator
// https://github.com/openapitools/openapi-generator)
//
// API version: 0.1.2

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.giftactivity.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** GetActDetailResponse */
public class GetActDetailResponse {
  /** 活动Id 说明：活动Id */
  @SerializedName("activity_id")
  private String activityId;

  /** 活动类型 说明：活动类型 */
  @SerializedName("activity_type")
  private ActType activityType;

  /** 活动基本信息 说明：创建活动时录入的基本信息 */
  @SerializedName("activity_base_info")
  private ActBaseInfo activityBaseInfo;

  /** 活动奖品发放规则 说明：奖品派送规则，分别对应满送、阶梯送、满A送B中的一种 */
  @SerializedName("award_send_rule")
  private AwardSendRule awardSendRule;

  /** 活动高级设置 说明：创建时传入的高级设置信息 */
  @SerializedName("advanced_setting")
  private ActAdvancedSetting advancedSetting;

  /** 活动状态 说明：活动当前状态枚举值 */
  @SerializedName("activity_status")
  private ActStatus activityStatus;

  /** 创建商户号 说明：创建商户号 */
  @SerializedName("creator_merchant_id")
  private String creatorMerchantId;

  /** 所属商户号 说明：所属商户号 */
  @SerializedName("belong_merchant_id")
  private String belongMerchantId;

  /** 活动暂停时间 说明：活动暂停时间 */
  @SerializedName("pause_time")
  private String pauseTime;

  /** 活动恢复时间 说明：活动恢复时间 */
  @SerializedName("recovery_time")
  private String recoveryTime;

  /** 活动创建时间 说明：活动创建时间 */
  @SerializedName("create_time")
  private String createTime;

  /** 活动更新时间 说明：活动更新时间 */
  @SerializedName("update_time")
  private String updateTime;

  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  public ActType getActivityType() {
    return activityType;
  }

  public void setActivityType(ActType activityType) {
    this.activityType = activityType;
  }

  public ActBaseInfo getActivityBaseInfo() {
    return activityBaseInfo;
  }

  public void setActivityBaseInfo(ActBaseInfo activityBaseInfo) {
    this.activityBaseInfo = activityBaseInfo;
  }

  public AwardSendRule getAwardSendRule() {
    return awardSendRule;
  }

  public void setAwardSendRule(AwardSendRule awardSendRule) {
    this.awardSendRule = awardSendRule;
  }

  public ActAdvancedSetting getAdvancedSetting() {
    return advancedSetting;
  }

  public void setAdvancedSetting(ActAdvancedSetting advancedSetting) {
    this.advancedSetting = advancedSetting;
  }

  public ActStatus getActivityStatus() {
    return activityStatus;
  }

  public void setActivityStatus(ActStatus activityStatus) {
    this.activityStatus = activityStatus;
  }

  public String getCreatorMerchantId() {
    return creatorMerchantId;
  }

  public void setCreatorMerchantId(String creatorMerchantId) {
    this.creatorMerchantId = creatorMerchantId;
  }

  public String getBelongMerchantId() {
    return belongMerchantId;
  }

  public void setBelongMerchantId(String belongMerchantId) {
    this.belongMerchantId = belongMerchantId;
  }

  public String getPauseTime() {
    return pauseTime;
  }

  public void setPauseTime(String pauseTime) {
    this.pauseTime = pauseTime;
  }

  public String getRecoveryTime() {
    return recoveryTime;
  }

  public void setRecoveryTime(String recoveryTime) {
    this.recoveryTime = recoveryTime;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActDetailResponse {\n");
    sb.append("    activityId: ").append(toIndentedString(activityId)).append("\n");
    sb.append("    activityType: ").append(toIndentedString(activityType)).append("\n");
    sb.append("    activityBaseInfo: ").append(toIndentedString(activityBaseInfo)).append("\n");
    sb.append("    awardSendRule: ").append(toIndentedString(awardSendRule)).append("\n");
    sb.append("    advancedSetting: ").append(toIndentedString(advancedSetting)).append("\n");
    sb.append("    activityStatus: ").append(toIndentedString(activityStatus)).append("\n");
    sb.append("    creatorMerchantId: ").append(toIndentedString(creatorMerchantId)).append("\n");
    sb.append("    belongMerchantId: ").append(toIndentedString(belongMerchantId)).append("\n");
    sb.append("    pauseTime: ").append(toIndentedString(pauseTime)).append("\n");
    sb.append("    recoveryTime: ").append(toIndentedString(recoveryTime)).append("\n");
    sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
