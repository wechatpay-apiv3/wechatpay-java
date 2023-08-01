package com.wechat.pay.java.service.marketingbankpackages;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskRequest;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskResponse;

/** MarketingBankPackagesService使用示例 */
public class MarketingBankPackagesServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static MarketingBankPackagesService service;

  public static void main(String[] args) {
    // 初始化商户配置
    Config config =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .apiV3Key(apiV3Key)
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
