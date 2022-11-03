package com.wechat.pay.java.shangmi;

import com.tencent.kona.KonaProvider;
import com.wechat.pay.java.core.cipher.AbstractPrivacyEncryptor;
import java.security.PublicKey;
import java.security.Security;

/** 国密敏感信息加密器 */
public final class SM2PrivacyEncryptor extends AbstractPrivacyEncryptor {

  static {
    Security.addProvider(new KonaProvider());
  }

  /**
   * @param publicKey 请求的敏感信息加密时使用的微信支付国密公钥
   * @param wechatpaySerial 微信支付国密平台证书的证书序列号
   */
  public SM2PrivacyEncryptor(PublicKey publicKey, String wechatpaySerial) {
    super("SM2", publicKey, wechatpaySerial);
  }
}
