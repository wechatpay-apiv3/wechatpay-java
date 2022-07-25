package com.wechat.pay.java.service;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.certificate.CertificateService;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;

/** 下载微信支付平台证书为例 */
public class QuickStart {

  /** 商户号 */
  public static String merchantId = "";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "";
  /** 微信支付平台证书路径 */
  public static String wechatPayCertificatePath = "";
  /** 微信支付 APIv3 密钥 */
  public static String apiV3Key = "";

  public static void main(String[] args) {
    downloadCertificate();
  }
  /** 下载证书 */
  public static void downloadCertificate() {
    CertificateService certificateService = buildCertificateService();
    List<X509Certificate> certificates =
        certificateService.downloadCertificate(apiV3Key.getBytes(StandardCharsets.UTF_8));
  }
  /**
   * 构建证书下载服务
   *
   * @return 证书下载服务
   */
  public static CertificateService buildCertificateService() {
    return new CertificateService.Builder().config(buildConfig()).build();
  }
  /**
   * 构建请求配置
   *
   * @return 配置
   */
  public static Config buildConfig() {
    return new RSAConfig.Builder()
        .merchantId(merchantId)
        // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
        // 也可以使用其他方式加载商户私钥
        .privateKeyFromPath(privateKeyPath)
        .merchantSerialNumber(merchantSerialNumber)
        .wechatPayCertificatesFromPath(wechatPayCertificatePath)
        .build();
  }
}
