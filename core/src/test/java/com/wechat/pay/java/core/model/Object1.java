package com.wechat.pay.java.core.model;

import com.wechat.pay.java.core.annotation.Encryption;

public class Object1 {
  @Encryption("EM_APIV3")
  private String object1EncryptVal;

  private Object2 object2;

  public ObjectType getObjectType() {
    return objectType;
  }

  public void setObjectType(ObjectType objectType) {
    this.objectType = objectType;
  }

  private ObjectType objectType;

  public String getObject1EncryptVal() {
    return object1EncryptVal;
  }

  public void setObject1EncryptVal(String object1EncryptVal) {
    this.object1EncryptVal = object1EncryptVal;
  }

  public Object2 getObject2() {
    return object2;
  }

  public void setObject2(Object2 object2) {
    this.object2 = object2;
  }

  @Override
  public String toString() {
    return "Object1{"
        + "object1EncryptVal='"
        + object1EncryptVal
        + '\''
        + ", object2="
        + object2
        + ", objectType="
        + objectType
        + '}';
  }
}
