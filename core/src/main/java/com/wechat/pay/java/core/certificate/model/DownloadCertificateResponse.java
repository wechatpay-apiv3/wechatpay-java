package com.wechat.pay.java.core.certificate.model;

import com.google.gson.annotations.SerializedName;
import com.wechat.pay.java.core.util.GsonUtil;
import java.util.List;

public class DownloadCertificateResponse {

  @SerializedName("data")
  List<Data> dataList;

  public List<Data> getData() {
    return dataList;
  }

  public void setData(List<Data> dataList) {
    this.dataList = dataList;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
