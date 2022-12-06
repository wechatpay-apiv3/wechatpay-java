// Copyright 2021 Tencent Inc. All rights reserved.
//
// 爱心餐对外API
//
// 微信支付爱心餐公益计划旨在面向深圳市的市政一线环卫工人提供每周一餐的1分钱用餐公益服务。在受助端，微信支付联动上千家餐饮门店关爱特殊人群，通过微信支付数字化能力将人群身份认证与公益福利领用全流程线上化，实现公益福利精准到人。在捐赠端，微信支付发挥连接优势与平台能力，结合用户就餐场景通过爱心餐一块捐插件让用户可在点餐时顺手捐1元，带动更多社会力量致谢城市美容师。
//
// API version: 0.0.4

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.lovefeast.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/** GetByUserRequest */
public class GetByUserRequest {
  /** 用户标识 说明：用户在商户AppID下的唯一标识 */
  @SerializedName("openid")
  @Expose(serialize = false)
  private String openid;
  /** 商户订单号 说明：商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一 */
  @SerializedName("out_trade_no")
  @Expose(serialize = false)
  private String outTradeNo;

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetByUserRequest {\n");
    sb.append("    openid: ").append(toIndentedString(openid)).append("\n");
    sb.append("    outTradeNo: ").append(toIndentedString(outTradeNo)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
