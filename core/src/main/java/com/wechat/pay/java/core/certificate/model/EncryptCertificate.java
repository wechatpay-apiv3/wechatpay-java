package com.wechat.pay.java.core.certificate.model;

import com.wechat.pay.java.core.util.GsonUtil;

public class EncryptCertificate {

  private String algorithm;
  private String nonce;
  private String associatedData;
  private String ciphertext;

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

  public String getAssociatedData() {
    return associatedData;
  }

  public void setAssociatedData(String associatedData) {
    this.associatedData = associatedData;
  }

  public String getCiphertext() {
    return ciphertext;
  }

  public void setCiphertext(String ciphertext) {
    this.ciphertext = ciphertext;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }

  public String getNonce() {
    return nonce;
  }
}
