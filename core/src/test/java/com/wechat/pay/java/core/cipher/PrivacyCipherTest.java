package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.util.ObjectUtil.deepCopy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.wechat.pay.java.core.model.Object1;
import com.wechat.pay.java.core.model.Object2;
import com.wechat.pay.java.core.model.Object3;
import com.wechat.pay.java.core.model.ObjectType;
import com.wechat.pay.java.core.model.TestConfig;
import com.wechat.pay.java.core.util.ObjectUtil;
import org.junit.jupiter.api.Test;

class PrivacyCipherTest {
  @Test
  void testEncrypt() {

    PrivacyEncryptor encryptor =
        new PrivacyEncryptor() {
          @Override
          public String encrypt(String plaintext) {
            return "fake_" + plaintext;
          }

          @Override
          public String getWechatpaySerial() {
            return "fake-serial";
          }
        };
    Object1 object1 = new Object1();
    object1.setObject1EncryptVal("encrypt");
    Object1 needEncrypt = ObjectUtil.deepCopy(object1, object1.getClass());
    PrivacyCipher.encrypt(object1, needEncrypt, encryptor);
    assertNotEquals(object1.getObject1EncryptVal(), needEncrypt.getObject1EncryptVal());
  }

  @Test
  void testDecrypt() {
    PrivacyDecryptor decryptor = ciphertext -> "fake_" + ciphertext;
    Object1 object1 = new Object1();
    object1.setObject1EncryptVal("need_decrypt");
    Object1 needDecrypt = deepCopy(object1, object1.getClass());
    PrivacyCipher.decrypt(object1, needDecrypt, decryptor);
    assertNotEquals(object1.getObject1EncryptVal(), needDecrypt.getObject1EncryptVal());
  }

  @Test
  void testEncryptAndDecrypt() {
    Object1 object1 = new Object1();
    Object2 object2 = new Object2();
    Object3 object3 = new Object3();
    object1.setObject1EncryptVal("object1EncryptVal");
    object1.setObject2(object2);
    object1.setObjectType(ObjectType.OBJCET1);
    object2.setObject2EncryptVal("object2EncryptVal");
    object2.setObject3(object3);
    object3.setObject3NormalVal("object3NormalVal");
    object3.setObject3EncryptVal("object3EncryptVal");
    Object1 needEncryptObj = deepCopy(object1, object1.getClass());
    PrivacyEncryptor encryptor =
        new RSAPrivacyEncryptor(
            TestConfig.MERCHANT_CERTIFICATE.getPublicKey(),
            TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER);

    PrivacyCipher.encrypt(object1, needEncryptObj, encryptor);

    assertEquals(
        object3.getObject3NormalVal(),
        needEncryptObj.getObject2().getObject3().getObject3NormalVal());
    assertNotEquals(object1.getObject1EncryptVal(), needEncryptObj.getObject1EncryptVal());
    assertNotEquals(
        object2.getObject2EncryptVal(), needEncryptObj.getObject2().getObject2EncryptVal());
    assertNotEquals(
        object3.getObject3EncryptVal(),
        needEncryptObj.getObject2().getObject3().getObject3EncryptVal());

    PrivacyDecryptor decryptor = new RSAPrivacyDecryptor(TestConfig.MERCHANT_PRIVATE_KEY);
    Object1 needDecryptObj = ObjectUtil.deepCopy(needEncryptObj, needEncryptObj.getClass());

    PrivacyCipher.decrypt(needEncryptObj, needDecryptObj, decryptor);
    assertEquals(
        object3.getObject3NormalVal(),
        needDecryptObj.getObject2().getObject3().getObject3NormalVal());
    assertEquals(object1.getObject1EncryptVal(), needDecryptObj.getObject1EncryptVal());

    assertEquals(
        object2.getObject2EncryptVal(), needDecryptObj.getObject2().getObject2EncryptVal());
    assertEquals(
        object3.getObject3EncryptVal(),
        needDecryptObj.getObject2().getObject3().getObject3EncryptVal());
  }
}
