package com.wechat.pay.java.core.certificate;

import java.security.cert.X509Certificate;

/** 证书处理器 */
public interface CertificateHandler {

  /**
   * 将证书从String转为X509Certificate
   *
   * @param certificate 证书字符串
   * @return X509Certificate
   */
  X509Certificate generateCertificate(String certificate);

  /**
   * * 验证证书链
   *
   * @param certificate 微信支付平台证书
   * @throws com.wechat.pay.java.core.exception.ValidationException 证书验证失败
   */
  void validateCertPath(X509Certificate certificate);
}
