package com.wechat.pay.java.service.papay.notify;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.papay.notify.model.BeforePayNotifyRequest;

/**
 * 周期扣款 预扣款通知 使用示例
 */
public class BeforePayNotifyExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static BeforePayNotifyService service;

  public static void main(String[] args) {
    // 初始化商户配置
    RSAConfig config =
        new RSAConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();

    // 初始化服务
    service = new BeforePayNotifyService.Builder().config(config).build();
    // ... 调用接口
  }

  /**
   * 发送 预扣款通知
   */
  public static void sendNotify() {
    BeforePayNotifyRequest request = new BeforePayNotifyRequest();
    service.sendNotify(request);
  }
}
