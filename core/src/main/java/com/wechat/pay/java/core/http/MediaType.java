package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

/** HTTP媒体类型 */
public enum MediaType {
  APPLICATION_JSON("application/json"),
  MULTIPART_FORM_DATA("multipart/form-data"),
  APPLICATION_X_GZIP("application/x-gzip"),
  APPLICATION_OCTET_STREAM("application/octet-stream");

  private final String value;

  MediaType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public boolean equalsWith(String string) {
    requireNonNull(string);
    return string.startsWith(value);
  }
}
