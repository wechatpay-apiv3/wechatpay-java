package com.wechat.pay.java.core.util;

import com.google.gson.Gson;

/** 对象操作工具类 */
public class ObjectUtil {
  private ObjectUtil() {}

  public static final Gson gson = new Gson();

  /**
   * 返回深拷贝
   *
   * @param object 拷贝目标
   * @param tClass 拷贝目标类型类
   * @return 深拷贝
   * @param <T> 拷贝目标对象类型
   */
  public static <T> T deepCopy(Object object, Class<T> tClass) {
    String json = gson.toJson(object);
    return gson.fromJson(json, tClass);
  }
}
