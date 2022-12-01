package com.wechat.pay.java.core.cipher;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractVerifier implements Verifier {

  protected static final Logger logger = LoggerFactory.getLogger(AbstractVerifier.class);
  protected final CertificateProvider certificateProvider;

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

  @Override
  public boolean verify(String serialNumber, String message, String signature) {
    X509Certificate certificate = certificateProvider.getCertificate(serialNumber);
    if (certificate == null) {
      logger.error(
          "Verify the signature and get the WechatPay certificate corresponding to "
              + "serialNumber[{}] is empty.",
          serialNumber);
      return false;
    }
    return verify(certificate, message, signature);
  }
}
