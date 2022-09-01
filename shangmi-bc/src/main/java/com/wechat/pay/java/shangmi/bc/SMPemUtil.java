package com.wechat.pay.java.shangmi.bc;

import com.wechat.pay.java.core.util.PemUtil;
import java.security.Security;
import java.security.cert.X509Certificate;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SMPemUtil {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private SMPemUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * 从私钥文件路径加载私钥
   *
   * @param keyPath 私钥文件路径
   * @return 私钥
   */
  public static BCECPrivateKey loadPrivateKeyFromPath(String keyPath) {
    return (BCECPrivateKey)
        PemUtil.loadPrivateKeyFromPath(keyPath, "EC", BouncyCastleProvider.PROVIDER_NAME);
  }

  /**
   * 从私钥字符串加载私钥
   *
   * @param keyString 私钥字符串
   * @return 私钥
   */
  public static BCECPrivateKey loadPrivateKeyFromString(String keyString) {
    return (BCECPrivateKey)
        PemUtil.loadPrivateKeyFromString(keyString, "EC", BouncyCastleProvider.PROVIDER_NAME);
  }

  /**
   * 从证书文件路径加载证书
   *
   * @param certificatePath 证书文件绝对路径
   * @return X509证书
   */
  public static X509Certificate loadX509FromPath(String certificatePath) {
    return PemUtil.loadX509FromPath(certificatePath, BouncyCastleProvider.PROVIDER_NAME);
  }

  /**
   * 从证书字符串加载证书
   *
   * @param certificateString 证书字符串
   * @return X509证书
   */
  public static X509Certificate loadX509FromString(String certificateString) {
    return PemUtil.loadX509FromString(certificateString, BouncyCastleProvider.PROVIDER_NAME);
  }
}
