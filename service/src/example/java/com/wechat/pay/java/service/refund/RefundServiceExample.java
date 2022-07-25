package com.wechat.pay.java.service.refund;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.QueryByOutRefundNoRefundsRequest;
import com.wechat.pay.java.service.refund.model.Refund;

/** RefundService使用示例 */
public class RefundServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static RefundService service;

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
    service = new RefundService(config);
    // ... 调用接口
  }

  /** 退款申请 */
  public static Refund createRefunds() {
    CreateRequest request = new CreateRequest();
    return service.createRefunds(request);
  }

  /** 查询单笔退款（通过商户退款单号） */
  public static Refund queryByOutRefundNoRefunds() {

    QueryByOutRefundNoRefundsRequest request = new QueryByOutRefundNoRefundsRequest();
    return service.queryByOutRefundNoRefunds(request);
  }
}
