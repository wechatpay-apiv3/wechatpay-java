package com.wechat.pay.java.core.cipher;

import java.security.PrivateKey;

/** RSA签名器 */
public final class RSASigner extends AbstractSigner {

  public RSASigner(String certificateSerialNumber, PrivateKey privateKey) {
    super("SHA256-RSA2048", Constant.SHA256WITHRSA, certificateSerialNumber, privateKey);
  }
}
