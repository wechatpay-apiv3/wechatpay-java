package com.wechat.pay.java.shangmi.kona;

import com.tencent.crypto.provider.SMCSProvider;
import com.wechat.pay.java.core.cipher.AbstractPrivacyDecryptor;
import java.security.PrivateKey;
import java.security.Security;

public final class SM2PrivacyDecryptor extends AbstractPrivacyDecryptor {

  static {
    Security.addProvider(new SMCSProvider());
  }

  public SM2PrivacyDecryptor(PrivateKey privateKey) {
    super("SM2", privateKey);
  }
}
