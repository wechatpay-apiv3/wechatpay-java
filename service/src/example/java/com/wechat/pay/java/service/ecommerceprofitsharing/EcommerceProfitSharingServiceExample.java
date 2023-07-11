package com.wechat.pay.java.service.ecommerceprofitsharing;

import com.wechat.pay.java.core.RSAConfig;
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

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static EcommerceProfitSharingService service;

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
