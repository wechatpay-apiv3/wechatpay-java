package com.wechat.pay.java.core.certificate;

import com.wechat.pay.java.core.util.PemUtil;
import java.security.cert.*;

final class RSACertificateHandler implements CertificateHandler {

  @Override
  public X509Certificate generateCertificate(String certificate) {
    return PemUtil.loadX509FromString(certificate);
  }
}
