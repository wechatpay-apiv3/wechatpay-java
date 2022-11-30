package com.wechat.pay.java.core.certificate;

import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.util.PemUtil;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.Function;

/** RSA平台证书管理器 */
public class RSACertificateManager extends AbstractCertificateManager {

  private static final String REQUEST_URL =
      "https://api.mch.weixin.qq.com/v3/certificates"; // 下载证书url
  private static final String ALGORITHM_TYPE = "RSA"; // 算法类型
  private static final Function<String, X509Certificate> certificateGenerator =
      PemUtil::loadX509FromString; // 将平台证书从String转换为X509Certificate的方法

  /**
   * 生成Verifier
   *
   * @param certificateList 平台证书列表
   * @return verifier
   */
  private Verifier toVerifier(List<X509Certificate> certificateList) {
    return new RSAVerifier(new InMemoryCertificateProvider(certificateList));
  }

  @Override
  public void putMerchant(String merchantId, HttpClient httpClient, AeadCipher aeadCipher) {
    super.putMerchant(
        merchantId,
        httpClient,
        aeadCipher,
        REQUEST_URL,
        certificateGenerator,
        this::toVerifier,
        ALGORITHM_TYPE);
  }

  /** 平台证书管理器实例持有类 */
  private static final class InstanceHolder {

    static final CertificateManager instance = new RSACertificateManager();
  }

  /**
   * 获取证书管理器实例
   *
   * @return 证书管理器实例
   */
  public static CertificateManager getInstance() {
    return InstanceHolder.instance;
  }
}
