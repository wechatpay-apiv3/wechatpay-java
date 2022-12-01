package com.wechat.pay.java.core.certificate;

import java.security.cert.X509Certificate;

/** 微信支付平台证书提供器 */
public interface CertificateProvider {

  /**
   * 根据证书序列号获取证书
   *
   * @param serialNumber 微信支付平台证书序列号
   * @return X.509证书实例
   */
  X509Certificate getCertificate(String serialNumber);

  /**
   * 获取最新可用的微信支付平台证书
   *
   * @return X.509证书实例
   */
  X509Certificate getAvailableCertificate();
}
