package com.wechat.pay.java.shangmi.bc;

import com.wechat.pay.java.core.cipher.AbstractSigner;
import java.security.PrivateKey;
import java.security.Security;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SM2Signer extends AbstractSigner {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  /**
   * @param certificateSerialNumber 商户API证书序列号
   * @param privateKey 商户API私钥
   */
  protected SM2Signer(String certificateSerialNumber, PrivateKey privateKey) {
    super(
        "SM2-WITH-SM3",
        GMObjectIdentifiers.sm2sign_with_sm3.toString(),
        certificateSerialNumber,
        privateKey);
  }
}
