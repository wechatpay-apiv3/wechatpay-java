package com.wechat.pay.java.core.cipher;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractVerifier implements Verifier {

  protected static final Logger logger = LoggerFactory.getLogger(AbstractVerifier.class);
  protected final CertificateProvider certificateProvider;
  protected final PublicKey publicKey;
  protected final String publicKeyId;
  protected final String algorithmName;

  /**
   * AbstractVerifier 构造函数
   *
   * @param algorithmName 获取Signature对象时指定的算法，例如SHA256withRSA
   * @param certificateProvider 验签使用的微信支付平台证书管理器，非空
   */
  protected AbstractVerifier(String algorithmName, CertificateProvider certificateProvider) {
    this.certificateProvider = requireNonNull(certificateProvider);
    this.algorithmName = requireNonNull(algorithmName);
    this.publicKey = null;
    this.publicKeyId = null;
  }

  /**
   * AbstractVerifier 构造函数
   *
   * @param algorithmName 获取Signature对象时指定的算法，例如SHA256withRSA
   * @param publicKey 验签使用的微信支付平台公钥，非空
   * @param publicKeyId 验签使用的微信支付平台公钥id
   */
  protected AbstractVerifier(String algorithmName, PublicKey publicKey, String publicKeyId) {
    this.publicKey = requireNonNull(publicKey);
    this.publicKeyId = publicKeyId;
    this.algorithmName = requireNonNull(algorithmName);
    this.certificateProvider = null;
  }

  /**
   * AbstractVerifier 构造函数，仅在平台证书和平台公钥灰度切换阶段使用
   *
   * @param algorithmName 获取Signature对象时指定的算法，例如SHA256withRSA
   * @param publicKey 验签使用的微信支付平台公钥，非空
   * @param publicKeyId 验签使用的微信支付平台公钥id
   * @param certificateProvider 验签使用的微信支付平台证书管理器，非空
   */
  protected AbstractVerifier(
      String algorithmName,
      PublicKey publicKey,
      String publicKeyId,
      CertificateProvider certificateProvider) {
    this.publicKey = requireNonNull(publicKey);
    this.publicKeyId = publicKeyId;
    this.algorithmName = requireNonNull(algorithmName);
    this.certificateProvider = requireNonNull(certificateProvider);
  }

  protected boolean verify(X509Certificate certificate, String message, String signature) {
    try {
      Signature sign = Signature.getInstance(algorithmName);
      sign.initVerify(certificate);
      sign.update(message.getBytes(StandardCharsets.UTF_8));
      return sign.verify(Base64.getDecoder().decode(signature));
    } catch (SignatureException e) {
      return false;
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("verify uses an illegal certificate.", e);
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException(
          "The current Java environment does not support " + algorithmName, e);
    }
  }

  private boolean verify(String message, String signature) {
    try {
      Signature sign = Signature.getInstance(algorithmName);
      sign.initVerify(publicKey);
      sign.update(message.getBytes(StandardCharsets.UTF_8));
      return sign.verify(Base64.getDecoder().decode(signature));
    } catch (SignatureException e) {
      return false;
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("verify uses an illegal publickey.", e);
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException(
          "The current Java environment does not support " + algorithmName, e);
    }
  }

  @Override
  public boolean verify(String serialNumber, String message, String signature) {
    // 如果公钥不为空，使用公钥验签
    if (publicKey != null) {
      if (serialNumber.equals(publicKeyId)) {
        return verify(message, signature);
      }
      // 如果证书为空，则说明是传入的publicKeyId错误，如果不为空，则继续使用证书验签
      if (certificateProvider == null) {
        logger.error(
            "publicKeyId[{}] and serialNumber[{}] are not equal", publicKeyId, serialNumber);
        return false;
      }
    }
    // 使用证书验签
    requireNonNull(certificateProvider);
    X509Certificate certificate = certificateProvider.getCertificate(serialNumber);
    if (certificate == null) {
      logger.error(
          "Verify the signature and get the WechatPay certificate or publicKey corresponding to "
              + "serialNumber[{}] is empty.",
          serialNumber);
      return false;
    }
    return verify(certificate, message, signature);
  }

  @Override
  public String getSerialNumber() {
    if (publicKey != null) {
      return publicKeyId;
    }

    requireNonNull(certificateProvider);
    return certificateProvider.getAvailableCertificate().getSerialNumber().toString();
  }
}
