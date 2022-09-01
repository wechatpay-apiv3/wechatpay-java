package com.wechat.pay.java.core.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/** PEM工具 */
public class PemUtil {

  private PemUtil() {}

  /**
   * 从私钥字符串中加载RSA私钥。
   *
   * @param keyString 私钥字符串
   * @return RSA私钥
   */
  public static PrivateKey loadPrivateKeyFromString(String keyString) {
    try {
      keyString =
              keyString
                      .replace("-----BEGIN PRIVATE KEY-----", "")
                      .replace("-----END PRIVATE KEY-----", "")
                      .replaceAll("\\s+", "");
      return KeyFactory.getInstance("RSA")
              .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyString)));
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException("The current Java environment does not support RSA");
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(
              "Get privateKey from privateKey str, keyString is illegal.", e);
    }
  }

  /**
   * @param keyString 私钥字符串
   * @param algorithm 私钥算法
   * @param provider the provider
   * @return 私钥
   */
  public static PrivateKey loadPrivateKeyFromString(
          String keyString, String algorithm, String provider) {
    try {
      keyString =
              keyString
                      .replace("-----BEGIN PRIVATE KEY-----", "")
                      .replace("-----END PRIVATE KEY-----", "")
                      .replaceAll("\\s+", "");
      return KeyFactory.getInstance(algorithm, provider)
              .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyString)));
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException(
              "The current Java environment does not support " + algorithm);
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(
              "Get privateKey from privateKey str, keyString is illegal.", e);
    } catch (NoSuchProviderException e) {
      throw new IllegalArgumentException("No such crypto provider " + provider, e);
    }
  }

  /**
   * 从私钥路径加载RSA私钥
   *
   * @param keyPath 私钥路径
   * @return RSA私钥
   */
  public static PrivateKey loadPrivateKeyFromPath(String keyPath) {
    return loadPrivateKeyFromString(readPrivateKeyStringFromPath(keyPath));
  }

  /**
   * @param keyPath 私钥路径
   * @param algorithm 私钥算法
   * @param provider the provider
   * @return 私钥
   */
  public static PrivateKey loadPrivateKeyFromPath(
          String keyPath, String algorithm, String provider) {
    return loadPrivateKeyFromString(readPrivateKeyStringFromPath(keyPath), algorithm, provider);
  }

  private static String readPrivateKeyStringFromPath(String keyPath) {
    try (FileInputStream inputStream = new FileInputStream(keyPath)) {
      return IOUtil.toString(inputStream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * 从输入流加载证书
   *
   * @param inputStream 私钥输入流
   * @return X.509证书
   */
  public static X509Certificate loadX509FromStream(InputStream inputStream) {
    try {
      return (X509Certificate)
              CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
    } catch (CertificateException e) {
      throw new IllegalArgumentException("parsing certificate failed", e);
    }
  }

  /**
   * 从输入流加载证书
   *
   * @param inputStream 私钥输入流
   * @param provider the provider
   * @return X.509证书
   */
  public static X509Certificate loadX509FromStream(InputStream inputStream, String provider) {
    try {
      return (X509Certificate)
              CertificateFactory.getInstance("X.509", provider)
                      .generateCertificate(inputStream);
    } catch (CertificateException e) {
      throw new IllegalArgumentException("parsing certificate failed", e);
    } catch (NoSuchProviderException e) {
      throw new IllegalArgumentException("No such provider " + provider, e);
    }
  }

  /**
   * 从证书文件路径加载证书
   *
   * @param certificatePath 证书文件路径
   * @return X.509证书
   */
  public static X509Certificate loadX509FromPath(String certificatePath) {
    try (FileInputStream inputStream = new FileInputStream(certificatePath)) {
      return loadX509FromStream(inputStream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * @param certificatePath 证书文件路径
   * @param provider the provider
   * @return X.509证书
   */
  public static X509Certificate loadX509FromPath(String certificatePath, String provider) {
    try (FileInputStream inputStream = new FileInputStream(certificatePath)) {
      return loadX509FromStream(inputStream, provider);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * 从证书字符串加载证书
   *
   * @param certificateString 证书字符串
   * @return X.509证书
   */
  public static X509Certificate loadX509FromString(String certificateString) {
    try (ByteArrayInputStream inputStream =
                 new ByteArrayInputStream(certificateString.getBytes(StandardCharsets.UTF_8))) {
      return loadX509FromStream(inputStream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * @param certificateString 证书字符串
   * @param provider the provider
   * @return X.509证书
   */
  public static X509Certificate loadX509FromString(String certificateString, String provider) {
    try (ByteArrayInputStream inputStream =
                 new ByteArrayInputStream(certificateString.getBytes(StandardCharsets.UTF_8))) {
      return loadX509FromStream(inputStream, provider);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
