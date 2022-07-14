package com.wechat.pay.java.service.certificate.model;

import com.google.gson.annotations.SerializedName;
import com.wechat.pay.java.core.util.GsonUtil;
import java.util.List;

public class DownloadCertificateResponse {

  private static final long serialVersionUID = -4605481743126436851L;

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
