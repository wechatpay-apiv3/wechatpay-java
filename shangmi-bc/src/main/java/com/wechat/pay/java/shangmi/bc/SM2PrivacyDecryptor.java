package com.wechat.pay.java.shangmi.bc;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.exception.DecryptionException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.spec.ECParameterSpec;

public class SM2PrivacyDecryptor implements PrivacyDecryptor {

  private final BCECPrivateKey privateKey;

  public SM2PrivacyDecryptor(BCECPrivateKey privateKey) {
    this.privateKey = requireNonNull(privateKey);
  }

  /**
   * 解密并转换为字符串
   *
   * @param ciphertext 密文
   * @return UTF-8编码的明文
   */
  @Override
  public String decrypt(String ciphertext) {
    // 密文为ASN.1格式的C1C3C2串，需要将其转换为SMEngine需要的C1C3C2格式
    byte[] ciphertextBytes = Converter.asn1Toc1c3c2(Base64.getDecoder().decode(ciphertext));
    ciphertextBytes = Converter.toBcC1C3C2(ciphertextBytes);
    ECParameterSpec ecParameterSpec = privateKey.getParameters();
    ECDomainParameters ecDomainParameters =
        new ECDomainParameters(
            ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN());
    ECPrivateKeyParameters ecPrivateKeyParameters =
        new ECPrivateKeyParameters(privateKey.getD(), ecDomainParameters);
    SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
    sm2Engine.init(false, ecPrivateKeyParameters);
    byte[] message;
    try {
      message = sm2Engine.processBlock(ciphertextBytes, 0, ciphertextBytes.length);
    } catch (Exception e) {
      throw new DecryptionException("decryption failed", e);
    }
    return new String(message, StandardCharsets.UTF_8);
  }
}
