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

/** AddStoresResponse */
public class AddStoresResponse {
  /**
   * 添加时间
   * 说明：添加时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日
   * 13点29分35秒。
   */
  @SerializedName("add_time")
  private String addTime;

  /** 添加失败的小店活动门店列表 说明：添加失败的小店活动门店列表 */
  @SerializedName("failed_stores")
  private List<RetailStoreInfo> failedStores;

  public String getAddTime() {
    return addTime;
  }

  public void setAddTime(String addTime) {
    this.addTime = addTime;
  }

  public List<RetailStoreInfo> getFailedStores() {
    return failedStores;
  }

  public void setFailedStores(List<RetailStoreInfo> failedStores) {
    this.failedStores = failedStores;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddStoresResponse {\n");
    sb.append("    addTime: ").append(toIndentedString(addTime)).append("\n");
    sb.append("    failedStores: ").append(toIndentedString(failedStores)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
