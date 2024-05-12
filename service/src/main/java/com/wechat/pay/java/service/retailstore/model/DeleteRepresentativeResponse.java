// Copyright 2021 Tencent Inc. All rights reserved.
//
// 营销加价购对外API
//
// 指定服务商可通过该接口报名加价购活动、查询某个区域内的加价购活动列表、锁定加价活动购资格以及解锁加价购活动资格。
//
// API version: 1.4.0

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.retailstore.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** DeleteRepresentativeResponse */
public class DeleteRepresentativeResponse {
  /** 零售小店活动ID 说明：零售小店活动ID */
  @SerializedName("activity_id")
  private String activityId;

  /** 删除失败业务代理信息列表 说明：删除失败业务代理信息列表 */
  @SerializedName("failed_representative_info_list")
  private List<RepresentativeInfo> failedRepresentativeInfoList;

  /** 删除时间 说明：删除时间 */
  @SerializedName("delete_time")
  private String deleteTime;

  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  public List<RepresentativeInfo> getFailedRepresentativeInfoList() {
    return failedRepresentativeInfoList;
  }

  public void setFailedRepresentativeInfoList(
      List<RepresentativeInfo> failedRepresentativeInfoList) {
    this.failedRepresentativeInfoList = failedRepresentativeInfoList;
  }

  public String getDeleteTime() {
    return deleteTime;
  }

  public void setDeleteTime(String deleteTime) {
    this.deleteTime = deleteTime;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeleteRepresentativeResponse {\n");
    sb.append("    activityId: ").append(toIndentedString(activityId)).append("\n");
    sb.append("    failedRepresentativeInfoList: ")
        .append(toIndentedString(failedRepresentativeInfoList))
        .append("\n");
    sb.append("    deleteTime: ").append(toIndentedString(deleteTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
