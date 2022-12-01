package com.wechat.pay.java.service.weixinpayscanandride;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.weixinpayscanandride.model.CreateTransactionRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.QueryTransactionRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.QueryUserServiceRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.TransactionsEntity;
import com.wechat.pay.java.service.weixinpayscanandride.model.UserServiceEntity;

/** WeixinPayScanAndRideService使用示例 */
public class WeixinPayScanAndRideServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static WeixinPayScanAndRideService service;

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
