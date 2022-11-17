package com.wechat.pay.java.service.partnerpayments.jsapi.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

public class PrepayWithRequestPaymentResponse {

  @SerializedName("appId")
  private String appId;

  @SerializedName("timeStamp")
  private String timestamp;

  @SerializedName("nonceStr")
  private String nonceStr;

  @SerializedName("package")
  private String packageVal;

  @SerializedName("signType")
  private String signType;

  @SerializedName("paySign")
  private String paySign;

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getTimeStamp() {
    return timestamp;
  }

  public void setTimeStamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getNonceStr() {
    return nonceStr;
  }

  public void setNonceStr(String nonceStr) {
    this.nonceStr = nonceStr;
  }

  public String getPackageVal() {
    return packageVal;
  }

  public void setPackageVal(String packageVal) {
    this.packageVal = packageVal;
  }

  public String getSignType() {
    return signType;
  }

  public void setSignType(String signType) {
    this.signType = signType;
  }

  public String getPaySign() {
    return paySign;
  }

  public void setPaySign(String paySign) {
    this.paySign = paySign;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrepayWithRequestPaymentRequest {\n");
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    nonceStr: ").append(toIndentedString(nonceStr)).append("\n");
    sb.append("    packageVal: ").append(toIndentedString(packageVal)).append("\n");
    sb.append("    signType: ").append(toIndentedString(signType)).append("\n");
    sb.append("    paySign: ").append(toIndentedString(paySign)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
