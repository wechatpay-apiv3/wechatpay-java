package com.wechat.pay.java.core.cipher;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.util.GsonUtil;

/** 签名结果 */
public class SignatureResult {

  private final String signature;

  private final String certificateSerialNumber;

  public SignatureResult(String signature, String certificateSerialNumber) {
    this.signature = requireNonNull(signature);
    this.certificateSerialNumber = requireNonNull(certificateSerialNumber);
  }

  /**
   * 获取签名
   *
   * @return 签名
   */
  public String getSign() {
    return signature;
  }

  /**
   * 获取签名对应的商户证书序列号
   *
   * @return 商户证书序列号
   */
  public String getCertificateSerialNumber() {
    return certificateSerialNumber;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
