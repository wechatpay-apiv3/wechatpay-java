package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.cipher.Constant.SHA256WITHRSA;

import com.wechat.pay.java.core.certificate.CertificateProvider;

/** RSA验签器 */
public final class RSAVerifier extends AbstractVerifier {

  public RSAVerifier(CertificateProvider provider) {
    super(SHA256WITHRSA, provider);
  }
}
