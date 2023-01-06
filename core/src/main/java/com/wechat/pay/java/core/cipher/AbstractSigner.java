package com.wechat.pay.java.core.cipher;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

public abstract class AbstractSigner implements Signer {

  private final String certificateSerialNumber;
  private final String algorithm;
  private final String algorithmName;
  private final PrivateKey privateKey;

  /**
   * AbstractSigner 构造函数
   *
   * @param algorithm 微信支付的签名算法，例如SHA256-RSA2048
   * @param algorithmName 获取Signature对象时指定的算法，例如SHA256withRSA
   * @param certificateSerialNumber 商户API证书序列号
   * @param privateKey 商户API私钥
   */
  protected AbstractSigner(
      String algorithm,
      String algorithmName,
      String certificateSerialNumber,
      PrivateKey privateKey) {
    this.algorithm = requireNonNull(algorithm);
    this.algorithmName = requireNonNull(algorithmName);
    this.certificateSerialNumber = requireNonNull(certificateSerialNumber);
    this.privateKey = requireNonNull(privateKey);
  }

  @Override
  public SignatureResult sign(String message) {
    requireNonNull(message);

    byte[] sign;
    try {
      Signature signature = Signature.getInstance(algorithmName);
      signature.initSign(privateKey);
      signature.update(message.getBytes(StandardCharsets.UTF_8));
      sign = signature.sign();
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException(
          "The current Java environment does not support " + algorithmName, e);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException(algorithm + " signature uses an illegal privateKey.", e);
    } catch (SignatureException e) {
      throw new RuntimeException("An error occurred during the sign process.", e);
    }
    return new SignatureResult(Base64.getEncoder().encodeToString(sign), certificateSerialNumber);
  }

  @Override
  public String getAlgorithm() {
    return algorithm;
  }
}
