package com.wechat.pay.java.service.payments.bill.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

public class ApplyFundFlowBillRequest {

  /** 账单日期 格式yyyy-MM-dd，仅支持三个月内的账单下载申请。 */
  @SerializedName("bill_date")
  private String billDate;
  /** 资金账户类型 不填则默认是BASIC 枚举值： BASIC：基本账户 OPERATION：运营账户 FEES：手续费账户 */
  @SerializedName("account_type")
  private AccountType accountType;

  /** 压缩类型 不填则默认是数据流 枚举值： GZIP：返回格式为.gzip的压缩包账单 */
  @SerializedName("tar_type")
  private TarType tarType;

  public String getBillDate() {
    return billDate;
  }

  public void setBillDate(String billDate) {
    this.billDate = billDate;
  }

  public TarType getTarType() {
    return tarType;
  }

  public void setTarType(TarType tarType) {
    this.tarType = tarType;
  }

  public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplyTradeBillRequest {\n");
    sb.append("    billDate: ").append(toIndentedString(billDate)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    tarType: ").append(toIndentedString(tarType)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
