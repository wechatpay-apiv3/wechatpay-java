package com.wechat.pay.java.core.util;

/** 字符串工具类 */
public class StringUtil {
  private StringUtil() {}

  /**
   * 将给定对象转换为字符串，每行缩进 4 个空格（除了首行）
   *
   * @param o 对象
   * @return 格式化字符串
   */
  public static String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
