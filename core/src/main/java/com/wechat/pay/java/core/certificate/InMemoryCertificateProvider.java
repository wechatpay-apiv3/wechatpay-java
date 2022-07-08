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

  public InMemoryCertificateProvider(List<X509Certificate> listCertificate) {
    if (listCertificate.isEmpty()) {
      throw new IllegalArgumentException("The parameter list of constructor is empty.");
    }
    for (X509Certificate item : listCertificate) {
      certificates.put(item.getSerialNumber(), item);
    }
  }

  @Override
  public X509Certificate getCertificate(String serialNumber) {
    BigInteger key = new BigInteger(serialNumber, Constant.HEX);
    return certificates.get(key);
  }
}
