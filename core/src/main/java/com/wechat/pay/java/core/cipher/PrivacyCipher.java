package com.wechat.pay.java.core.cipher;

import com.wechat.pay.java.core.annotation.Encryption;
import java.lang.reflect.Field;

/** 敏感信息加解密器 */
public class PrivacyCipher {

  private PrivacyCipher() {}
  /**
   * 获取obj中需要加密的字段，加密后设置在encryptObj
   *
   * @param obj 原对象
   * @param encryptObj 原对象的深拷贝
   * @param encryptor 加密器
   */
  public static void encrypt(Object obj, Object encryptObj, PrivacyEncryptor encryptor) {

    for (Field field : obj.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      Object objFieldVal;
      Object objEncryptFieldVal;
      try {
        objFieldVal = field.get(obj);
        objEncryptFieldVal = field.get(encryptObj);
      } catch (IllegalAccessException e) {
        throw new IllegalStateException(e);
      }
      // 如果值为空，跳过
      if (objFieldVal == null || objEncryptFieldVal == null) {
        continue;
      }
      // 如果是非枚举的自定义类，递归方法
      if (!field.isEnumConstant() && field.getType().getName().startsWith("com.wechat")) {
        encrypt(objFieldVal, objEncryptFieldVal, encryptor);
      }
      // 如果不是string类型或者没有加解密注解，跳过
      if (field.getType() != String.class || !field.isAnnotationPresent(Encryption.class)) {
        continue;
      }
      try {
        // 如果是string类型，加密值并设置给encryptObj
        field.set(encryptObj, encryptor.encrypt((String) objFieldVal));
      } catch (IllegalAccessException e) {
        throw new IllegalStateException(e);
      }
    }
  }
  /**
   * 获取obj中需要解密的字段，加密后设置在decryptObj
   *
   * @param obj 原对象
   * @param decryptObj 原对象的深拷贝
   * @param decryptor 解密器
   */
  public static void decrypt(Object obj, Object decryptObj, PrivacyDecryptor decryptor) {
    for (Field field : obj.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      Object objFieldVal;
      Object objDecryptFieldVal;
      try {
        objFieldVal = field.get(obj);
        objDecryptFieldVal = field.get(decryptObj);
      } catch (IllegalAccessException e) {
        throw new IllegalStateException(e);
      }
      // 如果值为空，跳过
      if (objFieldVal == null || objDecryptFieldVal == null) {
        continue;
      }
      // 如果是非枚举的自定义类，递归方法
      if (!field.isEnumConstant() && field.getType().getName().startsWith("com.wechat")) {
        decrypt(objFieldVal, objDecryptFieldVal, decryptor);
      }
      // 如果不是string类型或者没有加解密注解，跳过
      if (field.getType() != String.class || !field.isAnnotationPresent(Encryption.class)) {
        continue;
      }
      try {
        // 如果是string类型，解密值并设置给encryptObj
        field.set(decryptObj, decryptor.decrypt((String) objFieldVal));
      } catch (IllegalAccessException e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
