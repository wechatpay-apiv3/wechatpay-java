package com.wechat.pay.java.service.certificate;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;

/** 证书服务使用示例 */
public class CertificateServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String apiV3Key = "";
  public static CertificateService service;

  public static void main(String[] args) {
    // 初始化商户配置
    Config config =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util
            // 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .apiV3Key(apiV3Key)
            .build();

    // 初始化证书服务
    service = new CertificateService.Builder().config(config).build();
    // 设置商户apiV3密钥，apiV3密钥用于解密下载证书
    // ... 调用接口
  }

  /** 下载证书 */
  public static List<X509Certificate> downloadCertificate() {
    return service.downloadCertificate(apiV3Key.getBytes(StandardCharsets.UTF_8));
  }
}
