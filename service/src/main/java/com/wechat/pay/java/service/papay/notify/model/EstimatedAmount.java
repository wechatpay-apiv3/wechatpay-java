package com.wechat.pay.java.service.papay.notify.model;

import com.google.gson.annotations.SerializedName;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

/**
 * 预计扣费金额
 */
public class EstimatedAmount {

  /**
   * 预计扣费金额
   */
  @SerializedName("amount")
  private Integer amount;

  /**
   * 预计扣费货币类型
   */
  @SerializedName("currency")
  private String currency;

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstimatedAmount {\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
