package com.wechat.pay.java.shangmi.bc;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.cipher.AbstractVerifier;
import java.security.Security;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SM2Verifier extends AbstractVerifier {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }
  /** @param certificateProvider 验签使用的微信支付平台证书管理器，非空 */
  protected SM2Verifier(CertificateProvider certificateProvider) {
    super(GMObjectIdentifiers.sm2sign_with_sm3.toString(), certificateProvider);
  }
}
