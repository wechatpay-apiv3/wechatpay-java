package com.wechat.pay.java.service.profitsharing;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.profitsharing.model.AddReceiverRequest;
import com.wechat.pay.java.service.profitsharing.model.AddReceiverResponse;
import com.wechat.pay.java.service.profitsharing.model.CreateOrderRequest;
import com.wechat.pay.java.service.profitsharing.model.CreateReturnOrderRequest;
import com.wechat.pay.java.service.profitsharing.model.DeleteReceiverRequest;
import com.wechat.pay.java.service.profitsharing.model.DeleteReceiverResponse;
import com.wechat.pay.java.service.profitsharing.model.OrdersEntity;
import com.wechat.pay.java.service.profitsharing.model.QueryMerchantRatioRequest;
import com.wechat.pay.java.service.profitsharing.model.QueryMerchantRatioResponse;
import com.wechat.pay.java.service.profitsharing.model.QueryOrderAmountRequest;
import com.wechat.pay.java.service.profitsharing.model.QueryOrderAmountResponse;
import com.wechat.pay.java.service.profitsharing.model.QueryOrderRequest;
import com.wechat.pay.java.service.profitsharing.model.QueryReturnOrderRequest;
import com.wechat.pay.java.service.profitsharing.model.ReturnOrdersEntity;
import com.wechat.pay.java.service.profitsharing.model.SplitBillRequest;
import com.wechat.pay.java.service.profitsharing.model.SplitBillResponse;
import com.wechat.pay.java.service.profitsharing.model.UnfreezeOrderRequest;

/** ProfitsharingService使用示例 */
public class ProfitsharingServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static ProfitsharingService service;

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
    service = new ProfitsharingService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 获取分账账单文件下载地址 */
  public static SplitBillResponse splitBill() {

    SplitBillRequest request = new SplitBillRequest();
    return service.splitBill(request);
  }
  /** 查询最大分账比例API */
  public static QueryMerchantRatioResponse queryMerchantRatio() {

    QueryMerchantRatioRequest request = new QueryMerchantRatioRequest();
    return service.queryMerchantRatio(request);
  }
  /** 请求分账API */
  public static OrdersEntity createOrder() {
    CreateOrderRequest request = new CreateOrderRequest();
    return service.createOrder(request);
  }
  /** 查询分账结果API */
  public static OrdersEntity queryOrder() {

    QueryOrderRequest request = new QueryOrderRequest();
    return service.queryOrder(request);
  }
  /** 解冻剩余资金API */
  public static OrdersEntity unfreezeOrder() {
    UnfreezeOrderRequest request = new UnfreezeOrderRequest();
    return service.unfreezeOrder(request);
  }
  /** 添加分账接收方API */
  public static AddReceiverResponse addReceiver() {
    AddReceiverRequest request = new AddReceiverRequest();
    return service.addReceiver(request);
  }
  /** 删除分账接收方API */
  public static DeleteReceiverResponse deleteReceiver() {
    DeleteReceiverRequest request = new DeleteReceiverRequest();
    return service.deleteReceiver(request);
  }
  /** 请求分账回退API */
  public static ReturnOrdersEntity createReturnOrder() {
    CreateReturnOrderRequest request = new CreateReturnOrderRequest();
    return service.createReturnOrder(request);
  }
  /** 查询分账回退结果API */
  public static ReturnOrdersEntity queryReturnOrder() {

    QueryReturnOrderRequest request = new QueryReturnOrderRequest();
    return service.queryReturnOrder(request);
  }
  /** 查询剩余待分金额API */
  public static QueryOrderAmountResponse queryOrderAmount() {

    QueryOrderAmountRequest request = new QueryOrderAmountRequest();
    return service.queryOrderAmount(request);
  }
}
