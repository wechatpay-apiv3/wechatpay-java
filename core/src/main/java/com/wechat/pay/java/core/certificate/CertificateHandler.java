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
}
