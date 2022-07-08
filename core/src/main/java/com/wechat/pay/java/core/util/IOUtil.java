package com.wechat.pay.java.core.util;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** I/O工具 */
public class IOUtil {

  private static final int DEFAULT_BUFFER_SIZE = 8192;
  private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

  private IOUtil() {}

  /**
   * 转换输入流为字节数组
   *
   * @param inputStream 输入流
   * @return 字节数组
   * @throws IOException 读取字节失败、关闭流失败等
   */
  public static byte[] toByteArray(InputStream inputStream) throws IOException {
    requireNonNull(inputStream);
    List<byte[]> bufs = null;
    byte[] result = null;
    int total = 0;
    int remaining = Integer.MAX_VALUE;
    int n;
    do {
      byte[] buf = new byte[Math.min(remaining, DEFAULT_BUFFER_SIZE)];
      int nread = 0;

      // read to EOF which may read more or less than buffer size
      while ((n = inputStream.read(buf, nread, Math.min(buf.length - nread, remaining))) > 0) {
        nread += n;
        remaining -= n;
      }

      if (nread > 0) {
        if (MAX_BUFFER_SIZE - total < nread) {
          throw new OutOfMemoryError("Required array size too large");
        }
        total += nread;
        if (result == null) {
          result = buf;
        } else {
          if (bufs == null) {
            bufs = new ArrayList<>();
            bufs.add(result);
          }
          bufs.add(buf);
        }
      }
      // if the last call to read returned -1 or the number of bytes
      // requested have been read then break
    } while (n >= 0 && remaining > 0);

    if (bufs == null) {
      if (result == null) {
        return new byte[0];
      }
      return result.length == total ? result : Arrays.copyOf(result, total);
    }

    result = new byte[total];
    int offset = 0;
    remaining = total;
    for (byte[] b : bufs) {
      int count = Math.min(b.length, remaining);
      System.arraycopy(b, 0, result, offset, count);
      offset += count;
      remaining -= count;
    }

    return result;
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
    return toString(Files.newInputStream(Paths.get(path)));
  }
}
