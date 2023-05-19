package com.wechat.pay.java.service.papay.notify.model;

import com.google.gson.annotations.SerializedName;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

/**
 * 预扣款通知 请求消息
 */
public class BeforePayNotifyRequest {

  /**
   * 委托代扣协议id
   */
  @SerializedName("contract_id")
  private String contractId;

  /**
   * 直连商户号
   */
  @SerializedName("mchid")
  private String mchid;

  /**
   * 应用ID
   */
  @SerializedName("appid")
  private String appid;

  /**
   * -预计扣费金额信息
   */
  @SerializedName("estimated_amount")
  private EstimatedAmount estimatedAmount;

  public String getContractId() {
    return contractId;
  }

  public void setContractId(String contractId) {
    this.contractId = contractId;
  }

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public EstimatedAmount getEstimatedAmount() {
    return estimatedAmount;
  }

  public void setEstimatedAmount(EstimatedAmount estimatedAmount) {
    this.estimatedAmount = estimatedAmount;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeforePayNotifyRequest {\n");
    sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
    sb.append("    mchid: ").append(toIndentedString(mchid)).append("\n");
    sb.append("    appid: ").append(toIndentedString(appid)).append("\n");
    sb.append("    estimatedAmount: ").append(toIndentedString(estimatedAmount)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
