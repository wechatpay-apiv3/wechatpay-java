package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.util.GsonUtil;
import java.net.URLConnection;

/** 文件类型请求体 */
public final class FileRequestBody implements RequestBody {

  private final String meta;
  private final String fileName;
  private final byte[] file;

  private FileRequestBody(String meta, String fileName, byte[] file) {
    this.meta = meta;
    this.fileName = fileName;
    this.file = file;
  }

  /**
   * 获取媒体文件元信息
   *
   * @return 媒体文件元信息
   */
  public String getMeta() {
    return meta;
  }

  /**
   * 获取文件名
   *
   * @return 文件名
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * 获取文件
   *
   * @return 文件
   */
  public byte[] getFile() {
    return file;
  }

  @Override
  public String getContentType() {
    String contentTypeFromName = URLConnection.guessContentTypeFromName(fileName);
    if (contentTypeFromName == null) {
      // 表明是某种二进制数据
      return MediaType.APPLICATION_OCTET_STREAM.getValue();
    }
    return contentTypeFromName;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }

  public static class Builder {

    private String meta;
    private String fileName;
    private byte[] file;

    public Builder meta(String meta) {
      this.meta = meta;
      return this;
    }

    public Builder fileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder file(byte[] file) {
      this.file = file;
      return this;
    }

    public FileRequestBody build() {
      return new FileRequestBody(
          requireNonNull(meta), requireNonNull(fileName), requireNonNull(file));
    }
  }
}
