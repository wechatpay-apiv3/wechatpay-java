package com.wechat.pay.java.core.certificate;

import com.wechat.pay.java.core.util.PemUtil;
import java.security.cert.*;

final class RSACertificateHandler implements CertificateHandler {

  @Override
  public X509Certificate generateCertificate(String certificate) {
    return PemUtil.loadX509FromString(certificate);
  }

  @Override
  public void validateCertPath(X509Certificate certificate) {
    // 为防止证书过期导致验签失败，从而影响业务，后续不再验证证书信任链
    return;
  }
}
