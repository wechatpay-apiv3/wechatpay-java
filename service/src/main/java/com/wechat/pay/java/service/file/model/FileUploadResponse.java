package com.wechat.pay.java.service.file.model;

import com.wechat.pay.java.core.util.GsonUtil;

public class FileUploadResponse {

  private String mediaId;

  public String getMediaId() {
    return mediaId;
  }

  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
