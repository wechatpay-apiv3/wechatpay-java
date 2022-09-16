package com.wechat.pay.java.core.model;

import com.wechat.pay.java.core.annotation.Encryption;

public class Object3 {
  private String object3NormalVal;

  @Encryption("EM_APIV3")
  private String object3EncryptVal;

  public String getObject3NormalVal() {
    return object3NormalVal;
  }

  public void setObject3NormalVal(String object3NormalVal) {
    this.object3NormalVal = object3NormalVal;
  }

  public String getObject3EncryptVal() {
    return object3EncryptVal;
  }

  public void setObject3EncryptVal(String object3EncryptVal) {
    this.object3EncryptVal = object3EncryptVal;
  }

  @Override
  public String toString() {
    return "Object3{"
        + "object3NormalVal='"
        + object3NormalVal
        + '\''
        + ", object3EncryptVal='"
        + object3EncryptVal
        + '\''
        + '}';
  }
}
