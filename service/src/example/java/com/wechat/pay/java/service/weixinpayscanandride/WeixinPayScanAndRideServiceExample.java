package com.wechat.pay.java.service.weixinpayscanandride;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.weixinpayscanandride.model.CreateTransactionRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.QueryTransactionRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.QueryUserServiceRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.TransactionsEntity;
import com.wechat.pay.java.service.weixinpayscanandride.model.UserServiceEntity;

/** WeixinPayScanAndRideService使用示例 */
public class WeixinPayScanAndRideServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static WeixinPayScanAndRideService service;

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
    service = new WeixinPayScanAndRideService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 扣费受理 */
  public static TransactionsEntity createTransaction() {
    CreateTransactionRequest request = new CreateTransactionRequest();
    return service.createTransaction(request);
  }
  /** 查询订单 */
  public static TransactionsEntity queryTransaction() {

    QueryTransactionRequest request = new QueryTransactionRequest();
    return service.queryTransaction(request);
  }
  /** 查询用户服务可用信息 */
  public static UserServiceEntity queryUserService() {

    QueryUserServiceRequest request = new QueryUserServiceRequest();
    return service.queryUserService(request);
  }
}
