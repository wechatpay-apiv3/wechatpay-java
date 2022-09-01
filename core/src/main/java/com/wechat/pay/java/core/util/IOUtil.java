package com.wechat.pay.java.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/** I/O工具 */
public class IOUtil {

  private static final int DEFAULT_BUFFER_SIZE = 8192;

  private IOUtil() {}

  /**
   * 转换输入流为字节数组
   *
   * @param inputStream 输入流
   * @return 字节数组
   * @throws IOException 读取字节失败、关闭流失败等
   */
  public static byte[] toByteArray(InputStream inputStream) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    int nRead;
    byte[] data = new byte[DEFAULT_BUFFER_SIZE];
    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }

    return buffer.toByteArray();
  }

  /**
   * 转换输入流为字符串
   *
   * @param inputStream 输入流
   * @return UTF-8编码的字符串
   * @throws IOException 读取字节失败、关闭流失败等
   */
  public static String toString(InputStream inputStream) throws IOException {
    return new String(toByteArray(inputStream), StandardCharsets.UTF_8);
  }

  /**
   * 从文件路径中读取字符串
   *
   * @param path 文件路径
   * @return UTF-8编码的字符串
   * @throws IOException 读取字节失败、关闭流失败等
   */
  public static String loadStringFromPath(String path) throws IOException {
    try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
      return toString(inputStream);
    }
  }
}
