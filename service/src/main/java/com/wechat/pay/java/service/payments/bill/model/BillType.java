package com.wechat.pay.java.service.payments.bill.model;

import com.google.gson.annotations.SerializedName;

/** 账单类型 ALL：返回当日所有订单信息（不含充值退款订单） SUCCESS：返回当日成功支付的订单（不含充值退款订单） REFUND：返回当日退款订单（不含充值退款订单） */
public enum BillType {
  @SerializedName("ALL")
  ALL,
  @SerializedName("SUCCESS")
  SUCCESS,
  @SerializedName("REFUND")
  REFUND
}
