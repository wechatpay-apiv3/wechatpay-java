package com.wechat.pay.java.service.partnerpayments.applyments;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.service.partnerpayments.app.AppService;
import com.wechat.pay.java.service.partnerpayments.app.model.*;
import com.wechat.pay.java.service.partnerpayments.applyments.model.ApplymentsRequest;
import com.wechat.pay.java.service.partnerpayments.applyments.model.ApplymentsResponse;

/** AppService使用示例 */
public class ApplymentsExample {

  /** 商户号 */
  public static String merchantId = "190000****";

  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  public static String publicKeyFormPath = "/pub_key.pem";
  /** 商户API证书序列号 */
  public static String publicKeyId = "PUB_KEY_ID_01XXX";

  /** 商户证书序列号 */
  public static String merchantSerialNumber = "XXX";

  /** 商户APIV3密钥 */
  public static String apiV3Key = "";


  public static ApplymentsService service;

  public static void main(String[] args) {
    try {
      // 初始化商户配置
      System.out.println("开始初始化商户配置...");
      Config config =
          new RSAPublicKeyConfig.Builder()
              .merchantId(merchantId)
              .publicKeyFromPath(publicKeyFormPath)
              .publicKeyId(publicKeyId)
              // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
              .privateKeyFromPath(privateKeyPath)
              .merchantSerialNumber(merchantSerialNumber)
              .apiV3Key(apiV3Key)
              .build();

      // 初始化服务
      System.out.println("开始初始化服务...");
      service = new ApplymentsService.Builder().config(config).build();

      // 调用接口
      System.out.println("开始调用接口...");
      ApplymentsResponse response = applyments();
      System.out.println("接口调用成功，返回结果：" + response);

    } catch (HttpException e) {
      System.err.println("发送HTTP请求失败：" + e.getMessage());
      System.err.println("请求信息：" + e.getHttpRequest());
      e.printStackTrace();
    } catch (ServiceException e) {
      System.err.println("服务返回异常：" + e.getMessage());
      System.err.println("返回体：" + e.getResponseBody());
      e.printStackTrace();
    } catch (MalformedMessageException e) {
      System.err.println("解析返回体失败：" + e.getMessage());
      e.printStackTrace();
    } catch (ValidationException e) {
      System.err.println("验证签名失败：" + e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("发生未知错误：" + e.getMessage());
      e.printStackTrace();
    }
  }


  /** APP支付下单 */
  public static ApplymentsResponse applyments() {
    ApplymentsRequest request = new ApplymentsRequest();


    // 调用接口
    return service.applyments(request, publicKeyId);
  }


}
