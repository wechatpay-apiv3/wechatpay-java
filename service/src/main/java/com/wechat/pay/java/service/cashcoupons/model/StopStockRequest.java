// Copyright 2021 Tencent Inc. All rights reserved.
//
// 微信支付营销系统开放API
//
// 新增立减金api
//
// API version: 3.4.0

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.cashcoupons.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/** StopStockRequest */
public class StopStockRequest {
  /** 创建批次的商户号 说明：批次创建方商户号 */
  @SerializedName("stock_creator_mchid")
  private String stockCreatorMchid;

  /** 批次号 说明：批次号 */
  @SerializedName("stock_id")
  @Expose(serialize = false)
  private String stockId;

  public String getStockCreatorMchid() {
    return stockCreatorMchid;
  }

  public void setStockCreatorMchid(String stockCreatorMchid) {
    this.stockCreatorMchid = stockCreatorMchid;
  }

  public String getStockId() {
    return stockId;
  }

  public void setStockId(String stockId) {
    this.stockId = stockId;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StopStockRequest {\n");
    sb.append("    stockCreatorMchid: ").append(toIndentedString(stockCreatorMchid)).append("\n");
    sb.append("    stockId: ").append(toIndentedString(stockId)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
