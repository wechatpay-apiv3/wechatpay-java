package com.wechat.pay.java.service.wexinpayscoreparking;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.wexinpayscoreparking.model.CreateParkingRequest;
import com.wechat.pay.java.service.wexinpayscoreparking.model.CreateTransactionRequest;
import com.wechat.pay.java.service.wexinpayscoreparking.model.Parking;
import com.wechat.pay.java.service.wexinpayscoreparking.model.PlateService;
import com.wechat.pay.java.service.wexinpayscoreparking.model.QueryPlateServiceRequest;
import com.wechat.pay.java.service.wexinpayscoreparking.model.QueryTransactionRequest;
import com.wechat.pay.java.service.wexinpayscoreparking.model.Transaction;

/** WexinPayScoreParkingService使用示例 */
public class WexinPayScoreParkingServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static WexinPayScoreParkingService service;

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
    service = new WexinPayScoreParkingService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 创建停车入场 */
  public static Parking createParking() {
    CreateParkingRequest request = new CreateParkingRequest();
    return service.createParking(request);
  }
  /** 查询车牌服务开通信息 */
  public static PlateService queryPlateService() {

    QueryPlateServiceRequest request = new QueryPlateServiceRequest();
    return service.queryPlateService(request);
  }
  /** 扣费受理 */
  public static Transaction createTransaction() {
    CreateTransactionRequest request = new CreateTransactionRequest();
    return service.createTransaction(request);
  }
  /** 查询订单 */
  public static Transaction queryTransaction() {

    QueryTransactionRequest request = new QueryTransactionRequest();
    return service.queryTransaction(request);
  }
}
