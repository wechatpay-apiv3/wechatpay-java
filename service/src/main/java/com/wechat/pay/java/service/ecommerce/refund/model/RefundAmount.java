// Copyright 2021 Tencent Inc. All rights reserved.
//
// 电商退款API
//
// 境内电商退款功能相关API文档
//
// API version: 1.1.8

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.ecommerce.refund.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** RefundAmount */
public class RefundAmount {
  /** 退款金额 说明：退款金额，单位为分，只能为整数，不能超 过原订单支付金额 */
  @SerializedName("refund")
  private Long refund;
  /** 退款出资账户及金额 说明：退款出资的账户类型及金额信息 */
  @SerializedName("from")
  private List<FundsFromItem> from;
  /** 用户退款金额 说明：退款给用户的金额，不包含所有优惠券金额 */
  @SerializedName("payer_refund")
  private Long payerRefund;
  /** 优惠退款金额 说明：优惠券的退款金额，原支付单的优惠按比例退款 */
  @SerializedName("discount_refund")
  private Long discountRefund;
  /** 货币类型 说明：符合ISO4217标准的三位字母代码，目前只支持人民币：CNY */
  @SerializedName("currency")
  private String currency;
  /** 垫付金额 说明：电商平台垫付的金额 */
  @SerializedName("advance")
  private Long advance;

  public Long getRefund() {
    return refund;
  }

  public void setRefund(Long refund) {
    this.refund = refund;
  }

  public List<FundsFromItem> getFrom() {
    return from;
  }

  public void setFrom(List<FundsFromItem> from) {
    this.from = from;
  }

  public Long getPayerRefund() {
    return payerRefund;
  }

  public void setPayerRefund(Long payerRefund) {
    this.payerRefund = payerRefund;
  }

  public Long getDiscountRefund() {
    return discountRefund;
  }

  public void setDiscountRefund(Long discountRefund) {
    this.discountRefund = discountRefund;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Long getAdvance() {
    return advance;
  }

  public void setAdvance(Long advance) {
    this.advance = advance;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RefundAmount {\n");
    sb.append("    refund: ").append(toIndentedString(refund)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    payerRefund: ").append(toIndentedString(payerRefund)).append("\n");
    sb.append("    discountRefund: ").append(toIndentedString(discountRefund)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    advance: ").append(toIndentedString(advance)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}