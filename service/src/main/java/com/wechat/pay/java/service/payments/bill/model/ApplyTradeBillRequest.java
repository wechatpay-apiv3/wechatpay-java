package com.wechat.pay.java.service.payments.bill.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

public class ApplyTradeBillRequest {

  /** 账单日期 格式yyyy-MM-dd，仅支持三个月内的账单下载申请。 */
  @SerializedName("bill_date")
  private String billDate;
  /**
   * 账单类型 不填则默认是ALL 枚举值： ALL：返回当日所有订单信息（不含充值退款订单） SUCCESS：返回当日成功支付的订单（不含充值退款订单）
   * REFUND：返回当日退款订单（不含充值退款订单）
   */
  @SerializedName("bill_type")
  private BillType billType;

  /** 压缩类型 不填则默认是数据流 枚举值： GZIP：返回格式为.gzip的压缩包账单 */
  @SerializedName("tar_type")
  private TarType tarType;

  public String getBillDate() {
    return billDate;
  }

  public void setBillDate(String billDate) {
    this.billDate = billDate;
  }

  public BillType getBillType() {
    return billType;
  }

  public void setBillType(BillType billType) {
    this.billType = billType;
  }

  public TarType getTarType() {
    return tarType;
  }

  public void setTarType(TarType tarType) {
    this.tarType = tarType;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplyTradeBillRequest {\n");
    sb.append("    billDate: ").append(toIndentedString(billDate)).append("\n");
    sb.append("    billType: ").append(toIndentedString(billType)).append("\n");
    sb.append("    tarType: ").append(toIndentedString(tarType)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
