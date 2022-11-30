package com.wechat.pay.java.core.certificate;

import com.wechat.pay.java.core.cipher.Constant;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** 证书提供器的简单实现，证书存储在内存ConcurrentHashMap中 */
public final class InMemoryCertificateProvider implements CertificateProvider {

  private final ConcurrentHashMap<BigInteger, X509Certificate> certificates =
      new ConcurrentHashMap<>();
  private final X509Certificate availableCertificate;

  public InMemoryCertificateProvider(List<X509Certificate> certificates) {
    if (certificates.isEmpty()) {
      throw new IllegalArgumentException("The parameter list of constructor is empty.");
    }
    // 假设拿到的都是可用的，选取一个能用最久的
    X509Certificate longest = null;
    for (X509Certificate item : certificates) {
      this.certificates.put(item.getSerialNumber(), item);
      if (longest == null || item.getNotAfter().after(longest.getNotAfter())) {
        longest = item;
      }
    }
    availableCertificate = longest;
  }

  /**
   * 根据证书序列号获取证书
   *
   * @param serialNumber 微信支付平台证书序列号
   * @return X.509证书实例
   */
  @Override
  public X509Certificate getCertificate(String serialNumber) {
    BigInteger key = new BigInteger(serialNumber, Constant.HEX);
    return certificates.get(key);
  }

  /**
   * 获取最新可用的微信支付平台证书
   *
   * @return X.509证书实例
   */
  @Override
  public X509Certificate getAvailableCertificate() {
    return availableCertificate;
  }
}
