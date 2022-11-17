package com.wechat.pay.java.service.payments.jsapi;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.jsapi.model.CloseOrderRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.payments.model.Transaction;

public class JsapiServiceExtensionExample {
  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static JsapiServiceExtension serviceExtension;

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
    serviceExtension =
        new JsapiServiceExtension.Builder()
            .config(config)
            .signType("RSA") // 不填默认为RSA
            .build();
    try {
      // ... 调用接口
      PrepayWithRequestPaymentResponse response = prepayWithRequestPayment();
      System.out.println(response);
    } catch (HttpException e) { // 发送HTTP请求失败
      // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
    } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
      // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
    } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
      // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
    }
  }
  /** 关闭订单 */
  public static void closeOrder() {

    CloseOrderRequest request = new CloseOrderRequest();
    // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
    // 调用接口
    serviceExtension.closeOrder(request);
  }
  /** JSAPI支付下单，并返回JSAPI调起支付数据 */
  public static PrepayWithRequestPaymentResponse prepayWithRequestPayment() {
    PrepayRequest request = new PrepayRequest();
    // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
    // 调用接口
    return serviceExtension.prepayWithRequestPayment(request);
  }
  /** 微信支付订单号查询订单 */
  public static Transaction queryOrderById() {

    QueryOrderByIdRequest request = new QueryOrderByIdRequest();
    // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
    // 调用接口
    return serviceExtension.queryOrderById(request);
  }
  /** 商户订单号查询订单 */
  public static Transaction queryOrderByOutTradeNo() {

    QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
    // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
    // 调用接口
    return serviceExtension.queryOrderByOutTradeNo(request);
  }
}
