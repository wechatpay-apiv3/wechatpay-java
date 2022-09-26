package com.wechat.pay.java.core.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/** Gson工具类 */
public class GsonUtil {

  private GsonUtil() {}

  private static final Gson gson;

  static {
    gson =
        new GsonBuilder()
            .disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .addSerializationExclusionStrategy(
                new ExclusionStrategy() {
                  @Override
                  public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                    return expose != null && !expose.serialize();
                  }

                  @Override
                  public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                  }
                })
            .addDeserializationExclusionStrategy(
                new ExclusionStrategy() {
                  @Override
                  public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                    return expose != null && !expose.deserialize();
                  }

                  @Override
                  public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                  }
                })
            .create();
  }

  /**
   * 获取自定义设置的Gson对象
   *
   * @return Gson对象
   */
  public static Gson getGson() {
    return gson;
  }

  /**
   * 转换对象为JSON格式字符串
   *
   * @param object 待转换对象
   * @return JSON格式字符串
   */
  public static String toJson(Object object) {
    return gson.toJson(object);
  }
}
