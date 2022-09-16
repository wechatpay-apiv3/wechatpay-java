package com.wechat.pay.java.core.util;

import static com.wechat.pay.java.core.util.ObjectUtil.deepCopy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.wechat.pay.java.core.model.Object1;
import com.wechat.pay.java.core.model.Object2;
import com.wechat.pay.java.core.model.Object3;
import org.junit.jupiter.api.Test;

class ObjectUtilTest {

  @Test
  void testDeepCopySucc() {
    Object1 object1 = new Object1();
    Object2 object2 = new Object2();
    Object3 object3 = new Object3();
    object1.setObject1EncryptVal("object1EncryptVal");
    object1.setObject2(object2);
    object2.setObject2EncryptVal("object2EncryptVal");
    object2.setObject3(object3);
    object3.setObject3NormalVal("object3NormalVal");
    object3.setObject3EncryptVal("object3EncryptVal");
    Object1 copy = deepCopy(object1, object1.getClass());
    assertEquals(object1.toString(), copy.toString());
    copy.setObject1EncryptVal("test");
    assertNotEquals(object1.toString(), copy.toString());
  }
}
