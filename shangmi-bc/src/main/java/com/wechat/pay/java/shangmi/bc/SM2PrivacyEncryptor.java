package com.wechat.pay.java.shangmi.bc;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;

public class SM2PrivacyEncryptor implements PrivacyEncryptor {

  private final BCECPublicKey publicKey;
  private final String wechatpaySerial;

  public SM2PrivacyEncryptor(BCECPublicKey publicKey, String wechatpaySerial) {
    this.publicKey = requireNonNull(publicKey);
    this.wechatpaySerial = requireNonNull(wechatpaySerial).toUpperCase();
  }

  /**
   * 加密并转换为字符串
   *
   * @param plaintext 明文
   * @return Base64编码的密文
   */
  @Override
  public String encrypt(String plaintext) {
    ECParameterSpec ecParameterSpec = publicKey.getParameters();
    ECDomainParameters ecDomainParameters =
        new ECDomainParameters(
            ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN());
    ECPublicKeyParameters ecPublicKeyParameters =
        new ECPublicKeyParameters(publicKey.getQ(), ecDomainParameters);
    SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
    sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
    byte[] c1c3c2;
    byte[] in = plaintext.getBytes(StandardCharsets.UTF_8);
    try {
      byte[] tmp = sm2Engine.processBlock(in, 0, in.length);
      // SM2Engine得到的加密串前面多了一个`04`，因此需要从1开始取
      c1c3c2 = Arrays.copyOfRange(tmp, 1, tmp.length);
    } catch (InvalidCipherTextException e) {
      throw new IllegalArgumentException("The parameter plaintext of SM2 encryption is invalid", e);
    }
    byte[] asn1 = Converter.c1c3c2ToAsn1(c1c3c2);
    return Base64.getEncoder().encodeToString(asn1);
  }

  /**
   * 获取加密使用公钥所属证书的证书序列号，可设置到请求的 HTTP 头部 Wechatpay-Serial
   *
   * @return 证书序列号
   */
  @Override
  public String getWechatpaySerial() {
    return wechatpaySerial;
  }
}
