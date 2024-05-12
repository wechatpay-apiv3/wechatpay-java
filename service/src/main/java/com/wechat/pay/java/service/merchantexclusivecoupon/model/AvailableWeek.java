// Copyright 2021 Tencent Inc. All rights reserved.
//
// 营销商家券对外API
//
// No description provided (generated by Openapi Generator
// https://github.com/openapitools/openapi-generator)
//
// API version: 0.0.11

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.merchantexclusivecoupon.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** AvailableWeek */
public class AvailableWeek {
  /** 可用星期数 说明：0代表周日，1代表周一，以此类推 当填写available_day_time时，week_day必填 */
  @SerializedName("week_day")
  private List<Integer> weekDay;

  /** 当天可用时间段 说明：可以填写多个时间段，最多不超过2个 */
  @SerializedName("available_day_time")
  private List<AvailableCurrentDayTime> availableDayTime;

  public List<Integer> getWeekDay() {
    return weekDay;
  }

  public void setWeekDay(List<Integer> weekDay) {
    this.weekDay = weekDay;
  }

  public List<AvailableCurrentDayTime> getAvailableDayTime() {
    return availableDayTime;
  }

  public void setAvailableDayTime(List<AvailableCurrentDayTime> availableDayTime) {
    this.availableDayTime = availableDayTime;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AvailableWeek {\n");
    sb.append("    weekDay: ").append(toIndentedString(weekDay)).append("\n");
    sb.append("    availableDayTime: ").append(toIndentedString(availableDayTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
