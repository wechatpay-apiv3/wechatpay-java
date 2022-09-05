package com.wechat.pay.java.shangmi.bc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.util.Arrays;
import org.bouncycastle.asn1.*;
import org.bouncycastle.util.encoders.Hex;

public class Converter {
  /** SM2-WITH-SM3加密得到C1C3C2字符串中的C1部分长度 C1=x||y，为公钥x和y分量的顺序拼接串 长度=256+256=512bit，即64byte */
  private static final int C1_LEN_BYTE = 64;
  /** SM2WITHSM3加密得到C1C3C2字符串中的C3部分长度 C3=SM3HASH（x||m||y），为x和m和y的顺序拼接串 长度=32byte */
  private static final int C3_LEN_BYTE = 32;
  /** PC的HEX编码字符串 PC为公钥的压缩标志符 */
  private static final String PC_HEX = "04";

  private Converter() {}

  /**
   * 转换普通C1C3C2串为Boucy Castle的C1C3C2串
   *
   * @param c1c3c2 C1C3C2串字节数组
   * @return Boucy Castle的C1C3C2串
   */
  public static byte[] toBcC1C3C2(byte[] c1c3c2) {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      outputStream.write(Hex.decode(PC_HEX));
      outputStream.write(c1c3c2);
      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * 转换普通C1C3C2串为ASN.1_C1C3C2串
   *
   * @param c1c3c2 C1C3C2串字节数组 c1:x||y c3:SM3HASH(x||m||y) c2:加密明文
   * @return ASN.1_C1C3C2串字节数组
   */
  public static byte[] c1c3c2ToAsn1(byte[] c1c3c2) {
    byte[] c1 = Arrays.copyOfRange(c1c3c2, 0, C1_LEN_BYTE);
    byte[] c3 = Arrays.copyOfRange(c1c3c2, C1_LEN_BYTE, C1_LEN_BYTE + C3_LEN_BYTE);
    byte[] c2 = Arrays.copyOfRange(c1c3c2, C1_LEN_BYTE + C3_LEN_BYTE, c1c3c2.length);
    byte[] c1X = Arrays.copyOfRange(c1, 0, 32);
    byte[] c1Y = Arrays.copyOfRange(c1, 32, 64);
    ASN1Integer x = new ASN1Integer(c1X);
    ASN1Integer y = new ASN1Integer(c1Y);
    DEROctetString c3Str = new DEROctetString(c3);
    DEROctetString c2Str = new DEROctetString(c2);
    ASN1EncodableVector v = new ASN1EncodableVector();
    v.add(x);
    v.add(y);
    v.add(c3Str);
    v.add(c2Str);
    DERSequence seq = new DERSequence(v);
    try {
      return seq.getEncoded();
    } catch (IOException e) {
      throw new UncheckedIOException(
          "Failed to generate byte array of c1c3c2 of ASN.1 specification", e);
    }
  }

  /**
   * 转换ASN.1_C1C3C2串为普通C1C3C2串
   *
   * @param asn1 ASN.1_C1C3C2串字节数组
   * @return 普通C1C3C2串的字节数组
   */
  public static byte[] asn1Toc1c3c2(byte[] asn1) {
    ASN1Sequence seq;
    try (ASN1InputStream aIn = new ASN1InputStream(asn1)) {
      seq = (ASN1Sequence) aIn.readObject();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    BigInteger x = ASN1Integer.getInstance(seq.getObjectAt(0)).getValue();
    BigInteger y = ASN1Integer.getInstance(seq.getObjectAt(1)).getValue();
    byte[] c3 = ASN1OctetString.getInstance(seq.getObjectAt(2)).getOctets();
    byte[] c2 = ASN1OctetString.getInstance(seq.getObjectAt(3)).getOctets();
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      outputStream.write(x.toByteArray());
      outputStream.write(y.toByteArray());
      outputStream.write(c3);
      outputStream.write(c2);
      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
