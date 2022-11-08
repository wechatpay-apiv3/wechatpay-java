package com.wechat.pay.java.shangmi;

import com.tencent.kona.KonaProvider;
import com.wechat.pay.java.core.util.PemUtil;
import java.io.*;
import java.security.*;
import java.security.cert.X509Certificate;

public class SMPemUtil {

  static {
    Security.addProvider(new KonaProvider());
  }

  private SMPemUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * 从私钥字符串中加载EC(椭圆曲线)私钥。
   *
   * @param keyString 私钥字符串
   * @return EC(椭圆曲线)私钥。例如：国密私钥
   */
  public static PrivateKey loadPrivateKeyFromString(String keyString) {
    return PemUtil.loadPrivateKeyFromString(keyString, "EC", KonaProvider.NAME);
  }

  /**
   * 从私钥文件路径加载私钥
   *
   * @param keyPath 私钥文件路径
   * @return 私钥
   */
  public static PrivateKey loadPrivateKeyFromPath(String keyPath) {
    return PemUtil.loadPrivateKeyFromPath(keyPath, "EC", KonaProvider.NAME);
  }

  /**
   * 从证书输入流加载证书
   *
   * @param inputStream 证书输入流
   * @return X509证书
   */
  public static X509Certificate loadX509FromStream(InputStream inputStream) {
    return PemUtil.loadX509FromStream(inputStream, KonaProvider.NAME);
  }

  /**
   * 从证书文件路径加载证书
   *
   * @param certificatePath 证书文件绝对路径
   * @return X509证书
   */
  public static X509Certificate loadX509FromPath(String certificatePath) {
    return PemUtil.loadX509FromPath(certificatePath, KonaProvider.NAME);
  }

  /**
   * 从证书字符串加载证书
   *
   * @param certificateString 证书字符串
   * @return X509证书
   */
  public static X509Certificate loadX509FromString(String certificateString) {
    return PemUtil.loadX509FromString(certificateString, KonaProvider.NAME);
  }
}
