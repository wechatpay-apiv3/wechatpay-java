package com.wechat.pay.java.service.payments;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;

/** PaymentNotificationParseService使用示例 */
public class PaymentNotificationParseServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";

  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static PaymentNotificationParseService service;

  public static void main(String[] args) {
    // 初始化商户配置
    NotificationConfig config =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .apiV3Key(apiV3Key)
            .build();

    // 初始化服务
    service = new PaymentNotificationParseService.Builder().config(config).build();
    // ... 调用接口
    try {
      RequestParam requestParam =
          new RequestParam.Builder()
              .serialNumber("5157F09EFDC096DE15EBE81A47057A72********")
              .signature("mm/2CMGxo5qDKNk1i7Szn0IiwAUPlfrp********")
              .timestamp("1723789635")
              .nonce("614275b63d789bd3a7a472c63d809552")
              .body("")
              .signType("WECHATPAY2-SHA256-RSA2048")
              .build();
      Transaction transaction = payment(requestParam);
    } catch (MalformedMessageException e) { // 回调通知参数不正确、解析通知数据失败
      // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
    } catch (ValidationException e) { // 签名验证失败
      // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见ValidationException定义
    }
  }

  /** 支付回调 */
  public static Transaction payment(RequestParam requestParam) {
    return service.payment(requestParam);
  }
}
