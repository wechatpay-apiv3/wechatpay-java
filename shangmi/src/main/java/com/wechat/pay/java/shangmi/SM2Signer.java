package com.wechat.pay.java.shangmi;

import com.tencent.kona.KonaProvider;
import com.wechat.pay.java.core.cipher.AbstractSigner;
import java.security.PrivateKey;
import java.security.Security;

/** 国密签名器 */
public class SM2Signer extends AbstractSigner {
  static {
    Security.addProvider(new KonaProvider());
  }

  /**
   * SM2Signer 构造函数
   *
   * @param certificateSerialNumber 商户API证书序列号
   * @param privateKey 商户API私钥
   */
  public SM2Signer(String certificateSerialNumber, PrivateKey privateKey) {
    super("SM2-WITH-SM3", "SM2", certificateSerialNumber, privateKey);
  }
}
