package com.wechat.pay.java.service.billdownload;

import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.util.ShaUtil;
import com.wechat.pay.java.service.billdownload.model.HashType;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestBillEntity {
  private final DigestInputStream digestInputStream;
  private final String hashValue;

  public DigestBillEntity(InputStream inputStream, String hashValue, HashType hashType) {
    MessageDigest md = getMessageDigestInstance(hashType);
    this.digestInputStream = new DigestInputStream(inputStream, md);
    this.hashValue = hashValue;
  }

  private MessageDigest getMessageDigestInstance(HashType hashType) {
    String algorithm;
    if (hashType == HashType.SHA1) {
      algorithm = "SHA-1";
    } else {
      algorithm = hashType.name();
    }

    try {
      return MessageDigest.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
      throw new MalformedMessageException("Unsupported hash_type: " + hashType.name(), e);
    }
  }

  private String digestAndEncodeHex() {
    MessageDigest md = digestInputStream.getMessageDigest();
    byte[] hash = md.digest();

    return ShaUtil.toHexString(hash);
  }

  /**
   * * 返回下载账单的输入流。当账单下载完成后，调用方应关闭该输入流。
   *
   * @return 下载账单的输入流。
   */
  public InputStream getInputStream() {
    return this.digestInputStream;
  }

  /**
   * * 通过计算摘要并同申请下载账单时获得的摘要对比，验证下载账单的完整性
   *
   * @return 如果验证结果为真，账单是完整准确，未经篡改的。如果为假，账单是不完整或被篡改，应清理之前保存的文件。
   */
  public final boolean verifyHash() {
    final String digest = digestAndEncodeHex();
    return digest.equalsIgnoreCase(hashValue);
  }
}
