package com.wechat.pay.java.service.payments.bill.model;

import com.google.gson.annotations.SerializedName;

/** 压缩类型 GZIP：返回格式为.gzip的压缩包账单 */
public enum TarType {
  @SerializedName("GZIP")
  GZIP
}
