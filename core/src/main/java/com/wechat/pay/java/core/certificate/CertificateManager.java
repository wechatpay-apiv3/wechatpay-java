package com.wechat.pay.java.core.certificate;

import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.http.HttpClient;
import java.security.cert.X509Certificate;

/** 平台证书管理器 */
public interface CertificateManager {

  /**
   * 放入需要定时更新平台证书的商户
   *
   * @param merchantId 商户号
   */
  void putMerchant(String merchantId, HttpClient httpClient, AeadCipher aeadCipher);

  /** 停止定时更新平台证书，停止后无法再重新开启 */
  void stopScheduleUpdate();
  /**
   * 获取特定商户号下的可用平台证书
   *
   * @param merchantId 商户号
   * @return 可用平台证书
   */
  X509Certificate getAvailableCertificate(String merchantId);

  /**
   * 获取特定商户号下，平台证书序列号对应的平台证书
   *
   * @param merchantId 商户号
   * @param wechatPayCertSerialNum 平台证书序列号
   * @return 平台证书
   */
  X509Certificate getCertificate(String merchantId, String wechatPayCertSerialNum);
}
