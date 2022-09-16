package com.wechat.pay.java.core.model;

import com.wechat.pay.java.core.annotation.Encryption;

public class Object2 {
  @Encryption("EM_APIV3")
  private String object2EncryptVal;

  private Object3 object3;

  public String getObject2EncryptVal() {
    return object2EncryptVal;
  }

  public void setObject2EncryptVal(String object2EncryptVal) {
    this.object2EncryptVal = object2EncryptVal;
  }

  public Object3 getObject3() {
    return object3;
  }

  public void setObject3(Object3 object3) {
    this.object3 = object3;
  }

  @Override
  public String toString() {
    return "Object2{"
        + "firstRequestValOne='"
        + object2EncryptVal
        + '\''
        + ", firstRequestValSecond="
        + object3
        + '}';
  }
}
