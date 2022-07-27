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
    Config config =
        new RSAConfig.Builder()
            .merchantId(merchantId)
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();
    CertificateService certificateService = new CertificateService.Builder().config(config).build();
    List<X509Certificate> certificates =
        certificateService.downloadCertificate(apiV3Key.getBytes(StandardCharsets.UTF_8));
  }
}
