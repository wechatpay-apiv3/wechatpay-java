package com.wechat.pay.java.service.ecommercesubsidy;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCancelEntity;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCancelRequest;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCreateEntity;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCreateRequest;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesReturnEntity;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesReturnRequest;

/** EcommerceSubsidyService使用示例 */
public class EcommerceSubsidyServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static EcommerceSubsidyService service;

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
    service = new EcommerceSubsidyService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 取消补差 */
  public static SubsidiesCancelEntity cancelSubsidy() {
    SubsidiesCancelRequest request = new SubsidiesCancelRequest();
    return service.cancelSubsidy(request);
  }
  /** 请求补差 */
  public static SubsidiesCreateEntity createSubsidy() {
    SubsidiesCreateRequest request = new SubsidiesCreateRequest();
    return service.createSubsidy(request);
  }
  /** 请求补差回退 */
  public static SubsidiesReturnEntity returnSubsidy() {
    SubsidiesReturnRequest request = new SubsidiesReturnRequest();
    return service.returnSubsidy(request);
  }
}
