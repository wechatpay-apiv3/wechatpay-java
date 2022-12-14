package com.wechat.pay.java.service.payments.bill.model;

import com.google.gson.annotations.SerializedName;

/** 资金账户类型 不填则默认是BASIC 枚举值： BASIC：基本账户 OPERATION：运营账户 FEES：手续费账户 */
public enum AccountType {
  /** */
  @SerializedName("BASIC")
  BASIC,
  @SerializedName("OPERATION")
  OPERATION,
  @SerializedName("FEES")
  FEES
}
