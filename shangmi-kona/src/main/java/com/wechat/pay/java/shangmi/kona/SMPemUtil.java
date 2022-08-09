package com.wechat.pay.java.shangmi.kona;

import com.tencent.ssl.SMSSLProvider;
import com.wechat.pay.java.core.util.IOUtil;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMPemUtil {

  static {
    Security.addProvider(new SMSSLProvider());
  }

  private static final Logger logger = LoggerFactory.getLogger(SMPemUtil.class);

  /**
   * 从私钥文件路径加载私钥
   *
   * @param keyPath 私钥文件路径
   * @return 私钥
   */
  public static PrivateKey loadPrivateKeyFromPath(String keyPath) {
    if (keyPath == null || keyPath.isEmpty()) {
      throw new IllegalArgumentException(
          "Get privateKey from keyPath, the passed parameter keyPath is empty.");
    }

    String keyStr;
    try (FileInputStream inputStream = new FileInputStream(keyPath)) {
      keyStr = new String(IOUtil.toByteArray(inputStream), StandardCharsets.UTF_8);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(
          "Get privateKey from keyPath, file not found in keyPath:" + keyPath, e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return loadPrivateKeyFromString(keyStr);
  }

  /**
   * 从私钥字符串加载私钥
   *
   * @param keyString 私钥字符串
   * @return 私钥
   */
  public static PrivateKey loadPrivateKeyFromString(String keyString) {
    if (keyString == null || keyString.isEmpty()) {
      throw new IllegalArgumentException(
          "Get privateKey from private key string, the passed parameter privateKey is empty.");
    }
    try {
      String pkcs8KeyString =
          keyString
              .replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll("\\s+", "");

      return KeyFactory.getInstance("EC", "SMSSLProvider")
          .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(pkcs8KeyString)));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("The current Java environment does not support RSA.");
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(
          "Get privateKey from privateKey str, the passed parameter keyString is illegal.", e);
    } catch (NoSuchProviderException e) {
      throw new IllegalArgumentException("No SMSSLProvider", e);
    }
  }

  /**
   * 从证书输入流加载证书
   *
   * @param inputStream 证书输入流
   * @return X509证书
   */
  public static X509Certificate loadX509FromStream(InputStream inputStream) {
    if (inputStream == null) {
      throw new IllegalArgumentException(
          "Get certificate from inputStream, the passed parameter inputStream is null.");
    }
    X509Certificate cert;
    try {
      CertificateFactory factory = CertificateFactory.getInstance("X.509", "SMSSLProvider");
      cert = (X509Certificate) factory.generateCertificate(inputStream);
    } catch (CertificateException e) {
      throw new IllegalArgumentException(
          "Get certificate from inputStream, the passed parameter inputStream is " + "illegal.");
    } catch (NoSuchProviderException e) {
      throw new IllegalArgumentException("No SMSSLProvider", e);
    }
    return cert;
  }

  /**
   * 从证书文件路径加载证书
   *
   * @param certificatePath 证书文件绝对路径
   * @return X509证书
   */
  public static X509Certificate loadX509FromPath(String certificatePath) {
    if (certificatePath == null || certificatePath.isEmpty()) {
      throw new IllegalArgumentException(
          "Get certificate from certificate path, the passed parameter certPath is empty.");
    }
    X509Certificate x509Cert = null;
    try (FileInputStream inputStream = new FileInputStream(certificatePath)) {
      x509Cert = loadX509FromStream(inputStream);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(
          "Get certificate from certificate path, file not found in certPath:" + certificatePath,
          e);
    } catch (IOException e) {
      logger.warn("Get certificate from certificate path,inputStream close failed.", e);
    }
    return x509Cert;
  }

  /**
   * 从证书字符串加载证书
   *
   * @param certificateString 证书字符串
   * @return X509证书
   */
  public static X509Certificate loadX509FromString(String certificateString) {
    if (certificateString == null || certificateString.isEmpty()) {
      throw new IllegalArgumentException(
          "Get certificate from certificate string, the passed parameter cert is empty.");
    }
    X509Certificate x509Cert = null;
    try (ByteArrayInputStream inputStream =
        new ByteArrayInputStream(certificateString.getBytes(StandardCharsets.UTF_8))) {
      x509Cert = loadX509FromStream(inputStream);
    } catch (IOException e) {
      logger.warn("Get certificate from certificate string,inputStream close failed.", e);
    }
    return x509Cert;
  }
}
