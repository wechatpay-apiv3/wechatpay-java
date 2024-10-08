package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.cipher.Constant.SHA256WITHRSA;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import java.security.PublicKey;

/** RSA验签器 */
public final class RSAVerifier extends AbstractVerifier {

  public RSAVerifier(CertificateProvider provider) {
    super(SHA256WITHRSA, provider);
  }

  public RSAVerifier(PublicKey publicKey, String publicKeyId) {
    super(SHA256WITHRSA, publicKey, publicKeyId);
  }

  public RSAVerifier(PublicKey publicKey, String publicKeyId, CertificateProvider provider) {
    super(SHA256WITHRSA, publicKey, publicKeyId, provider);
  }
}
