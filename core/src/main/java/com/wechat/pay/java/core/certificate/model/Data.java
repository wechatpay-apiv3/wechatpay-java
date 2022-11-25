package com.wechat.pay.java.core.certificate.model;

import com.wechat.pay.java.core.util.GsonUtil;

/** 获取平台证书列表返回数据 */
public class Data {

  private String serialNo;
  private String effectiveTime;
  private String expireTime;
  private EncryptCertificate encryptCertificate;

  public String getSerialNo() {
    return serialNo;
  }

  public void setSerialNo(String serialNo) {
    this.serialNo = serialNo;
  }

  public EncryptCertificate getEncryptCertificate() {
    return encryptCertificate;
  }

  public void setEncryptCertificate(EncryptCertificate encryptCertificate) {
    this.encryptCertificate = encryptCertificate;
  }

  public String getEffectiveTime() {
    return effectiveTime;
  }

  public void setEffectiveTime(String effectiveTime) {
    this.effectiveTime = effectiveTime;
  }

  public String getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(String expireTime) {
    this.expireTime = expireTime;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
