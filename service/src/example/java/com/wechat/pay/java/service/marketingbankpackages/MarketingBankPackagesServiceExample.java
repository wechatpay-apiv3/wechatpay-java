package com.wechat.pay.java.service.marketingbankpackages;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskRequest;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskResponse;

/** MarketingBankPackagesService使用示例 */
public class MarketingBankPackagesServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static MarketingBankPackagesService service;

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
    service = new MarketingBankPackagesService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 查询上传任务列表 */
  public static ListTaskResponse listTask() {

    ListTaskRequest request = new ListTaskRequest();
    return service.listTask(request);
  }
}
