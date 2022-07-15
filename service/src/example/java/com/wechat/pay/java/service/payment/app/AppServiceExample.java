package com.wechat.pay.java.service.payment.app;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.payment.app.model.CloseOrderRequest;
import com.wechat.pay.java.service.payment.app.model.PrepayRequest;
import com.wechat.pay.java.service.payment.app.model.PrepayResponse;
import com.wechat.pay.java.service.payment.app.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.payment.app.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.payment.model.Transaction;

/** AppService使用示例 */
public class AppServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static AppService service;

  public static void main(String[] args) {
    // 初始化商户配置
    RSAConfig config =
        new RSAConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util
            // 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();

    // 初始化服务
    service = new AppService(config);
    // ... 调用接口
  }
  /** 关闭订单 */
  public static void closeOrder() {

    CloseOrderRequest request = new CloseOrderRequest();
    service.closeOrder(request);
  }
  /** APP支付下单 */
  public static PrepayResponse prepay() {
    PrepayRequest request = new PrepayRequest();
    return service.prepay(request);
  }
  /** 微信支付订单号查询订单 */
  public static Transaction queryOrderById() {

    QueryOrderByIdRequest request = new QueryOrderByIdRequest();
    return service.queryOrderById(request);
  }
  /** 商户订单号查询订单 */
  public static Transaction queryOrderByOutTradeNo() {

    QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
    return service.queryOrderByOutTradeNo(request);
  }
}
