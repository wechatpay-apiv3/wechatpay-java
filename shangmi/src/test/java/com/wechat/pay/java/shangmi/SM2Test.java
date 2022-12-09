package com.wechat.pay.java.shangmi;

import static com.wechat.pay.java.core.cipher.Constant.HEX;

import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.shangmi.testing.BaseSM2Test;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Vector;

class SM2Test implements BaseSM2Test {

  @Override
  public Signer createSM2Signer(String serialNumber, String privateKeyString) {
    return new SM2Signer(serialNumber, SMPemUtil.loadPrivateKeyFromString(privateKeyString));
  }

  @Override
  public Verifier createSM2Verifier(String certificate) {
    List<X509Certificate> list = new Vector<>();
    list.add(SMPemUtil.loadX509FromString(certificate));
    return new SM2Verifier(new InMemoryCertificateProvider(list));
  }

  @Override
  public PrivacyEncryptor createPrivacyEncryptor(String certificate) {
    X509Certificate c = SMPemUtil.loadX509FromString(certificate);
    return new SM2PrivacyEncryptor(c.getPublicKey(), c.getSerialNumber().toString(HEX));
  }

  @Override
  public PrivacyDecryptor createPrivacyDecryptor(String privateKeyString) {
    return new SM2PrivacyDecryptor(SMPemUtil.loadPrivateKeyFromString(privateKeyString));
  }
}
