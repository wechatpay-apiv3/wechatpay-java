// Copyright 2021 Tencent Inc. All rights reserved.
//
// Native支付
//
// Native支付API
//
// API version: 1.2.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.ecommercecombinepayments.nativepay.model;

import com.google.gson.annotations.SerializedName;
import com.wechat.pay.java.service.ecommercecombinepayments.model.PromotionDetail;
import com.wechat.pay.java.service.ecommercecombinepayments.model.TransactionAmount;
import com.wechat.pay.java.service.ecommercecombinepayments.model.TransactionPayer;

import java.util.List;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

/** */
public class Transaction {
  /** amount */
  @SerializedName("amount")
  private TransactionAmount amount;

  /** spAppid */
  @SerializedName("sp_appid")
  private String spAppid;

  /** subAppid */
  @SerializedName("sub_appid")
  private String subAppid;

  /** spMchid */
  @SerializedName("sp_mchid")
  private String spMchid;

  /** subMchid */
  @SerializedName("sub_mchid")
  private String subMchid;

  /** attach */
  @SerializedName("attach")
  private String attach;

  /** bankType */
  @SerializedName("bank_type")
  private String bankType;

  /** outTradeNo */
  @SerializedName("out_trade_no")
  private String outTradeNo;

  /** payer */
  @SerializedName("payer")
  private TransactionPayer payer;

  /** promotionDetail */
  @SerializedName("promotion_detail")
  private List<PromotionDetail> promotionDetail;

  /** successTime */
  @SerializedName("success_time")
  private String successTime;

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

  /** tradeStateDesc */
  @SerializedName("trade_state_desc")
  private String tradeStateDesc;

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

  /** transactionId */
  @SerializedName("transaction_id")
  private String transactionId;

  public TransactionAmount getAmount() {
    return amount;
  }

  public void setAmount(TransactionAmount amount) {
    this.amount = amount;
  }

  public String getSpAppid() {
    return spAppid;
  }

  public void setSpAppid(String spAppid) {
    this.spAppid = spAppid;
  }

  public String getSubAppid() {
    return subAppid;
  }

  public void setSubAppid(String subAppid) {
    this.subAppid = subAppid;
  }

  public String getSpMchid() {
    return spMchid;
  }

  public void setSpMchid(String spMchid) {
    this.spMchid = spMchid;
  }

  public String getSubMchid() {
    return subMchid;
  }

  public void setSubMchid(String subMchid) {
    this.subMchid = subMchid;
  }

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public String getBankType() {
    return bankType;
  }

  public void setBankType(String bankType) {
    this.bankType = bankType;
  }

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  public TransactionPayer getPayer() {
    return payer;
  }

  public void setPayer(TransactionPayer payer) {
    this.payer = payer;
  }

  public List<PromotionDetail> getPromotionDetail() {
    return promotionDetail;
  }

  public void setPromotionDetail(List<PromotionDetail> promotionDetail) {
    this.promotionDetail = promotionDetail;
  }

  public String getSuccessTime() {
    return successTime;
  }

  public void setSuccessTime(String successTime) {
    this.successTime = successTime;
  }

  public TradeStateEnum getTradeState() {
    return tradeState;
  }

  public void setTradeState(TradeStateEnum tradeState) {
    this.tradeState = tradeState;
  }

  public String getTradeStateDesc() {
    return tradeStateDesc;
  }

  public void setTradeStateDesc(String tradeStateDesc) {
    this.tradeStateDesc = tradeStateDesc;
  }

  public TradeTypeEnum getTradeType() {
    return tradeType;
  }

  public void setTradeType(TradeTypeEnum tradeType) {
    this.tradeType = tradeType;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transaction {\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    spAppid: ").append(toIndentedString(spAppid)).append("\n");
    sb.append("    subAppid: ").append(toIndentedString(subAppid)).append("\n");
    sb.append("    spMchid: ").append(toIndentedString(spMchid)).append("\n");
    sb.append("    subMchid: ").append(toIndentedString(subMchid)).append("\n");
    sb.append("    attach: ").append(toIndentedString(attach)).append("\n");
    sb.append("    bankType: ").append(toIndentedString(bankType)).append("\n");
    sb.append("    outTradeNo: ").append(toIndentedString(outTradeNo)).append("\n");
    sb.append("    payer: ").append(toIndentedString(payer)).append("\n");
    sb.append("    promotionDetail: ").append(toIndentedString(promotionDetail)).append("\n");
    sb.append("    successTime: ").append(toIndentedString(successTime)).append("\n");
    sb.append("    tradeState: ").append(toIndentedString(tradeState)).append("\n");
    sb.append("    tradeStateDesc: ").append(toIndentedString(tradeStateDesc)).append("\n");
    sb.append("    tradeType: ").append(toIndentedString(tradeType)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
