package com.wechat.pay.java.core.util;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** SHA工具 */
public class ShaUtil {

  private ShaUtil() {}

  public static final int BUFFER_SIZE = 1024;
  public static final String SHA1 = "SHA-1";
  public static final String SHA256 = "SHA-256";

  /**
   * 生成SHA1的HEX编码消息摘要字符串
   *
   * @param inputStream 消息输入流
   * @return HEX编码消息摘要字符串
   * @throws IOException 读取输入流失字节、关闭流失败等
   */
  public static String getSha1HexString(InputStream inputStream) throws IOException {
    try {
      return getShaHexString(inputStream, SHA1);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getSha256HexString(InputStream inputStream) throws IOException {
    try {
      return getShaHexString(inputStream, SHA256);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getSha256HexString(byte[] source) {
    try {
      return getShaHexString(source, SHA256);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private static String getShaHexString(InputStream inputStream, String algorithm)
      throws NoSuchAlgorithmException, IOException {
    MessageDigest digest = MessageDigest.getInstance(algorithm);
    byte[] data = new byte[BUFFER_SIZE];
    int nRead;
    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
      digest.update(data, 0, nRead);
    }
    return toHexString(digest.digest());
  }

  /**
   * 生成SHA算法的HEX编码消息摘要字符串
   *
   * @param source 消息字节数组
   * @param algorithm 具体的SHA算法，例如SHA-1、SHA-256
   * @return HEX编码消息摘要字符串
   * @throws NoSuchAlgorithmException 没有对应的SHA算法
   */
  private static String getShaHexString(byte[] source, String algorithm)
      throws NoSuchAlgorithmException {
    requireNonNull(source);
    MessageDigest digest = MessageDigest.getInstance(algorithm);
    digest.update(source);
    return toHexString(digest.digest());
  }

  /**
   * 转换字节数组为HEX编码字符串
   *
   * @param bytes 字节数组
   * @return HEX编码字符串
   */
  private static String toHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
