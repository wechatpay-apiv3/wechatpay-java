package com.wechat.pay.java.service.ecommerceprofitsharing;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.AddReceiverRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.AddReceiverResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateAfterSalesOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateAfterSalesOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateReturnOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateReturnOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.DeleteReceiverRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.DeleteReceiverResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.FinishOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.FinishOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryAfterSalesOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryAfterSalesOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryOrderAmountRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryOrderAmountResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryReturnOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryReturnOrderResponse;

/** EcommerceProfitSharingService使用示例 */
public class EcommerceProfitSharingServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static EcommerceProfitSharingService service;

  public static void main(String[] args) {
    // 初始化商户配置
    Config config =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util
            // 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .apiV3Key(apiV3Key)
            .build();

    // 初始化服务
    service = new EcommerceProfitSharingService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 请求售后服务分账 */
  public static CreateAfterSalesOrderResponse createAfterSalesOrder() {
    CreateAfterSalesOrderRequest request = new CreateAfterSalesOrderRequest();
    return service.createAfterSalesOrder(request);
  }
  /** 请求分账 */
  public static CreateOrderResponse createOrder() {
    CreateOrderRequest request = new CreateOrderRequest();
    return service.createOrder(request);
  }
  /** 完结分账 */
  public static FinishOrderResponse finishOrder() {
    FinishOrderRequest request = new FinishOrderRequest();
    return service.finishOrder(request);
  }
  /** 查询售后服务分账结果 */
  public static QueryAfterSalesOrderResponse queryAfterSalesOrder() {

    QueryAfterSalesOrderRequest request = new QueryAfterSalesOrderRequest();
    return service.queryAfterSalesOrder(request);
  }
  /** 查询分账结果 */
  public static QueryOrderResponse queryOrder() {

    QueryOrderRequest request = new QueryOrderRequest();
    return service.queryOrder(request);
  }
  /** 查询订单剩余待分金额 */
  public static QueryOrderAmountResponse queryOrderAmount() {

    QueryOrderAmountRequest request = new QueryOrderAmountRequest();
    return service.queryOrderAmount(request);
  }
  /** 添加分账接收方 */
  public static AddReceiverResponse addReceiver() {
    AddReceiverRequest request = new AddReceiverRequest();
    return service.addReceiver(request);
  }
  /** 删除分账接收方 */
  public static DeleteReceiverResponse deleteReceiver() {
    DeleteReceiverRequest request = new DeleteReceiverRequest();
    return service.deleteReceiver(request);
  }
  /** 请求分账回退 */
  public static CreateReturnOrderResponse createReturnOrder() {
    CreateReturnOrderRequest request = new CreateReturnOrderRequest();
    return service.createReturnOrder(request);
  }
  /** 查询分账回退结果 */
  public static QueryReturnOrderResponse queryReturnOrder() {

    QueryReturnOrderRequest request = new QueryReturnOrderRequest();
    return service.queryReturnOrder(request);
  }
}
