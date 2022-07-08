package com.wechat.pay.java.core.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** PEM工具 */
public class PemUtil {

  private static final Logger logger = LoggerFactory.getLogger(PemUtil.class);
  private static final String RSA_ALGORITHM = "RSA";
  private static final String X509_CERTIFICATE_TYPE = "X509";

  private PemUtil() {}

  /**
   * 从私钥字符串中加载私钥
   *
   * @param keyString 私钥字符串
   * @return 私钥
   */
  public static PrivateKey loadPrivateKeyFromString(String keyString) {
    try {
      keyString =
          keyString
              .replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll("\\s+", "");
      return KeyFactory.getInstance(RSA_ALGORITHM)
          .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyString)));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("The current Java environment does not support RSA.");
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(
          "Get privateKey from privateKey str, the passed parameter keyString is illegal.", e);
    }
  }

  /**
   * 从私钥路径加载私钥
   *
   * @param keyPath 私钥路径
   * @return 私钥
   */
  public static PrivateKey loadPrivateKeyFromPath(String keyPath) {
    try (FileInputStream inputStream = new FileInputStream(keyPath)) {
      return loadPrivateKeyFromString(IOUtil.toString(inputStream));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(
          "Get privateKey from keyPath, file not found in keyPath:" + keyPath, e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 从私钥输入流加载私钥
   *
   * @param inputStream 私钥输入流
   * @return 私钥
   */
  public static X509Certificate loadX509FromStream(InputStream inputStream) {
    try {
      return (X509Certificate)
          CertificateFactory.getInstance(X509_CERTIFICATE_TYPE).generateCertificate(inputStream);
    } catch (CertificateException e) {
      throw new IllegalArgumentException(
          "Get certificate from inputStream, the passed parameter inputStream is " + "illegal.", e);
    }
  }

  /**
   * 从证书文件路径加载证书
   *
   * @param certificatePath 证书文件路径
   * @return X509证书
   */
  public static X509Certificate loadX509FromPath(String certificatePath) {
    X509Certificate certificate = null;
    try (FileInputStream inputStream = new FileInputStream(certificatePath)) {
      certificate = loadX509FromStream(inputStream);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(
          "Get privateKey from certificatePath, file not found in certificatePath:"
              + certificatePath,
          e);
    } catch (IOException e) {
      logger.error("Get certificate from certificatePath,inputStream close failed.", e);
    }
    return certificate;
  }

  /**
   * 从证书字符串加载证书
   *
   * @param certificateString 证书字符串
   * @return X509证书
   */
  public static X509Certificate loadX509FromString(String certificateString) {
    X509Certificate certificate = null;
    try (ByteArrayInputStream inputStream =
        new ByteArrayInputStream(certificateString.getBytes())) {
      certificate = loadX509FromStream(inputStream);
    } catch (IOException e) {
      logger.warn("Get certificate from certificate string,inputStream close failed.", e);
    }
    return certificate;
  }
}
