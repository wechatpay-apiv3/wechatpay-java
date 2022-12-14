package com.wechat.pay.java.service.payments.bill.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

public class ApplyFundFlowBillResponse {

  /** 哈希类型 枚举值： SHA1：SHA1值 */
  @SerializedName("hash_type")
  private HashType hashType;
  /** 哈希值 原始账单（gzip需要解压缩）的摘要值，用于校验文件的完整性。 */
  @SerializedName("hash_value")
  private String hashValue;
  /** 账单下载地址 供下一步请求账单文件的下载地址，该地址30s内有效。 */
  @SerializedName("download_url")
  private String downloadUrl;

  public HashType getHashType() {
    return hashType;
  }

  public void setHashType(HashType hashType) {
    this.hashType = hashType;
  }

  public String getHashValue() {
    return hashValue;
  }

  public void setHashValue(String hashValue) {
    this.hashValue = hashValue;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplyTradeBillResponse {\n");
    sb.append("    hashType: ").append(toIndentedString(hashType)).append("\n");
    sb.append("    hashValue: ").append(toIndentedString(hashValue)).append("\n");
    sb.append("    downloadUrl: ").append(toIndentedString(downloadUrl)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
