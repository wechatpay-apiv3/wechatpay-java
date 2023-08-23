package com.wechat.pay.java.service.ecommercerefund;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.ecommercerefund.model.CreateRefundRequest;
import com.wechat.pay.java.service.ecommercerefund.model.CreateReturnAdvanceRequest;
import com.wechat.pay.java.service.ecommercerefund.model.QueryRefundByOutRefundNoRequest;
import com.wechat.pay.java.service.ecommercerefund.model.QueryRefundRequest;
import com.wechat.pay.java.service.ecommercerefund.model.QueryReturnAdvanceRequest;
import com.wechat.pay.java.service.ecommercerefund.model.Refund;
import com.wechat.pay.java.service.ecommercerefund.model.Refund4Create;
import com.wechat.pay.java.service.ecommercerefund.model.ReturnAdvance;

/** EcommerceRefundService使用示例 */
public class EcommerceRefundServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static EcommerceRefundService service;

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
    service = new EcommerceRefundService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 申请退款 */
  public static Refund4Create createRefund() {
    CreateRefundRequest request = new CreateRefundRequest();
    return service.createRefund(request);
  }
  /** 垫付退款回补 */
  public static ReturnAdvance createReturnAdvance() {

    CreateReturnAdvanceRequest request = new CreateReturnAdvanceRequest();
    return service.createReturnAdvance(request);
  }
  /** 查询单笔退款（按微信支付退款单号） */
  public static Refund queryRefund() {

    QueryRefundRequest request = new QueryRefundRequest();
    return service.queryRefund(request);
  }
  /** 查询单笔退款（按商户退款单号） */
  public static Refund queryRefundByOutRefundNo() {

    QueryRefundByOutRefundNoRequest request = new QueryRefundByOutRefundNoRequest();
    return service.queryRefundByOutRefundNo(request);
  }
  /** 查询垫付回补结果 */
  public static ReturnAdvance queryReturnAdvance() {

    QueryReturnAdvanceRequest request = new QueryReturnAdvanceRequest();
    return service.queryReturnAdvance(request);
  }
}
