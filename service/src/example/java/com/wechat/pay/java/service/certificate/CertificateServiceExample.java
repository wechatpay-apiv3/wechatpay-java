package com.wechat.pay.java.service.certificate;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;

/** 证书服务使用示例 */
public class CertificateServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static String apiV3Key = "";
  public static CertificateService service;

  public static void main(String[] args) {
    // 初始化商户配置
    RSAConfig rsaConfig =
        new RSAConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();

    // 初始化证书服务
    service = new CertificateService(rsaConfig);
    // 设置商户apiV3密钥，apiV3密钥用于解密下载证书
    AeadCipher aeadCipher = new AeadAesCipher(apiV3Key.getBytes(StandardCharsets.UTF_8));
    // ... 调用接口
  }

  /** 下载证书 */
  public static List<X509Certificate> downloadCertificate() {
    AeadCipher aeadCipher = new AeadAesCipher(apiV3Key.getBytes(StandardCharsets.UTF_8));
    return service.downloadCertificate(aeadCipher);
  }
}
