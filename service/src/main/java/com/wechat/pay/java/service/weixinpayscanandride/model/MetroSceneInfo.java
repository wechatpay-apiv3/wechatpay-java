// Copyright 2021 Tencent Inc. All rights reserved.
//
// 公共出行平台代扣服务对外API
//
// 公共出行平台代扣服务对外API
//
// API version: 1.0.0

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.weixinpayscanandride.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** MetroSceneInfo */
public class MetroSceneInfo {
  /** 乘车时间 说明：用户乘车时间（上车），按照使用rfc3339所定义的格式，格式为yyyy-MM-DDThh:mm:ss+TIMEZONE */
  @SerializedName("start_time")
  private String startTime;
  /** 下车时间 说明：用户下车时间，按照使用rfc3339所定义的格式，格式为yyyy-MM-DDThh:mm:ss+TIMEZONE */
  @SerializedName("end_time")
  private String endTime;
  /** 起始站 说明：乘车起始站，该值催缴时会向微信用户进行展示 */
  @SerializedName("start_station")
  private String startStation;
  /** 终点站 说明：乘车终点站，该值催缴时会向微信用户进行展示 */
  @SerializedName("end_station")
  private String endStation;

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getStartStation() {
    return startStation;
  }

  public void setStartStation(String startStation) {
    this.startStation = startStation;
  }

  public String getEndStation() {
    return endStation;
  }

  public void setEndStation(String endStation) {
    this.endStation = endStation;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetroSceneInfo {\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    startStation: ").append(toIndentedString(startStation)).append("\n");
    sb.append("    endStation: ").append(toIndentedString(endStation)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
