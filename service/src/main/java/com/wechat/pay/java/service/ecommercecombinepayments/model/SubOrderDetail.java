package com.wechat.pay.java.service.ecommercecombinepayments.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

public class SubOrderDetail {

  /** 子商户号 说明：子单发起方商户号，必须与发起方appid有绑定关系。服务商和电商模式下，传服务商商户号。 */
  @SerializedName("mchid")
  private String mchid;

  /** tradeType */
  public enum TradeTypeEnum {
    @SerializedName("JSAPI")
    JSAPI,

    @SerializedName("NATIVE")
    NATIVE,

    @SerializedName("APP")
    APP,

    @SerializedName("MICROPAY")
    MICROPAY,

    @SerializedName("MWEB")
    MWEB,

    @SerializedName("FACEPAY")
    FACEPAY
  }

  @SerializedName("trade_type")
  private TradeTypeEnum tradeType;

  /** tradeState */
  public enum TradeStateEnum {
    @SerializedName("SUCCESS")
    SUCCESS,

    @SerializedName("REFUND")
    REFUND,

    @SerializedName("NOTPAY")
    NOTPAY,

    @SerializedName("CLOSED")
    CLOSED,

    @SerializedName("REVOKED")
    REVOKED,

    @SerializedName("USERPAYING")
    USERPAYING,

    @SerializedName("PAYERROR")
    PAYERROR,

    @SerializedName("ACCEPT")
    ACCEPT
  }

  @SerializedName("trade_state")
  private TradeStateEnum tradeState;

  /** bankType */
  @SerializedName("bank_type")
  private String bankType;

  /** attach */
  @SerializedName("attach")
  private String attach;

  /** successTime */
  @SerializedName("success_time")
  private String successTime;

  /** transactionId */
  @SerializedName("transaction_id")
  private String transactionId;

  /** outTradeNo */
  @SerializedName("out_trade_no")
  private String outTradeNo;

  /** subMchid */
  @SerializedName("sub_mchid")
  private String subMchid;

  /** subAppid */
  @SerializedName("sub_appid")
  private String subAppid;

  /** subOpenid */
  @SerializedName("sub_openid")
  private String subOpenid;

  /** amount */
  @SerializedName("amount")
  private TransactionAmount amount;

  /** promotionDetail */
  @SerializedName("promotion_detail")
  private List<PromotionDetail> promotionDetail;

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  public TradeTypeEnum getTradeType() {
    return tradeType;
  }

  public void setTradeType(TradeTypeEnum tradeType) {
    this.tradeType = tradeType;
  }

  public TradeStateEnum getTradeState() {
    return tradeState;
  }

  public void setTradeState(TradeStateEnum tradeState) {
    this.tradeState = tradeState;
  }

  public String getBankType() {
    return bankType;
  }

  public void setBankType(String bankType) {
    this.bankType = bankType;
  }

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public String getSuccessTime() {
    return successTime;
  }

  public void setSuccessTime(String successTime) {
    this.successTime = successTime;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  public String getSubMchid() {
    return subMchid;
  }

  public void setSubMchid(String subMchid) {
    this.subMchid = subMchid;
  }

  public String getSubAppid() {
    return subAppid;
  }

  public void setSubAppid(String subAppid) {
    this.subAppid = subAppid;
  }

  public String getSubOpenid() {
    return subOpenid;
  }

  public void setSubOpenid(String subOpenid) {
    this.subOpenid = subOpenid;
  }

  public TransactionAmount getAmount() {
    return amount;
  }

  public void setAmount(TransactionAmount amount) {
    this.amount = amount;
  }

  public List<PromotionDetail> getPromotionDetail() {
    return promotionDetail;
  }

  public void setPromotionDetail(List<PromotionDetail> promotionDetail) {
    this.promotionDetail = promotionDetail;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubOrderDetail {\n");
    sb.append("    mchid: ").append(toIndentedString(mchid)).append("\n");
    sb.append("    tradeType: ").append(toIndentedString(tradeType)).append("\n");
    sb.append("    tradeState: ").append(toIndentedString(tradeState)).append("\n");
    sb.append("    bankType: ").append(toIndentedString(bankType)).append("\n");
    sb.append("    attach: ").append(toIndentedString(attach)).append("\n");
    sb.append("    successTime: ").append(toIndentedString(successTime)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    outTradeNo: ").append(toIndentedString(outTradeNo)).append("\n");
    sb.append("    subMchid: ").append(toIndentedString(subMchid)).append("\n");
    sb.append("    subAppid: ").append(toIndentedString(subAppid)).append("\n");
    sb.append("    subOpenid: ").append(toIndentedString(subOpenid)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    promotionDetail: ").append(toIndentedString(promotionDetail)).append("\n");
    sb.append("}");
    return sb.toString();
  }

}
