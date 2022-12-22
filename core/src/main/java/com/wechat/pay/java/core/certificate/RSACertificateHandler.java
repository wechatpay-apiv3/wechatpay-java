package com.wechat.pay.java.core.certificate;

import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.util.PemUtil;
import java.security.cert.X509Certificate;
import java.util.List;

class RSACertificateHandler implements CertificateHandler {

  @Override
  public X509Certificate generateCertificate(String certificate) {
    return PemUtil.loadX509FromString(certificate);
  }

  @Override
  public Verifier generateVerifier(List<X509Certificate> certificateList) {
    return new RSAVerifier(new InMemoryCertificateProvider(certificateList));
  }
}
