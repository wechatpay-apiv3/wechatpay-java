package com.wechat.pay.java.service.partnerpayments.app.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

public class PrepayWithRequestPaymentResponse {

  @SerializedName("appid")
  private String appid;

  @SerializedName("partnerid")
  private String partnerId;

  @SerializedName("prepayid")
  private String prepayId;

  @SerializedName("package")
  private String packageVal;

  @SerializedName("noncestr")
  private String nonceStr;

  @SerializedName("timestamp")
  private String timestamp;

  @SerializedName("sign")
  private String sign;

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public String getPrepayId() {
    return prepayId;
  }

  public void setPrepayId(String prepayId) {
    this.prepayId = prepayId;
  }

  public String getPackageVal() {
    return packageVal;
  }

  public void setPackageVal(String packageVal) {
    this.packageVal = packageVal;
  }

  public String getNonceStr() {
    return nonceStr;
  }

  public void setNonceStr(String nonceStr) {
    this.nonceStr = nonceStr;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrepayWithRequestPaymentRequest {\n");
    sb.append("    appid: ").append(toIndentedString(appid)).append("\n");
    sb.append("    partnerId: ").append(toIndentedString(partnerId)).append("\n");
    sb.append("    prepayId: ").append(toIndentedString(prepayId)).append("\n");
    sb.append("    packageVal: ").append(toIndentedString(packageVal)).append("\n");
    sb.append("    nonceStr: ").append(toIndentedString(nonceStr)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    sign: ").append(toIndentedString(sign)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
