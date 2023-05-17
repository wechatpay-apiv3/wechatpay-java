package com.wechat.pay.java.service.refunddomestic;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.refunddomestic.model.CreateAbnormalRefundRequest;
import com.wechat.pay.java.service.refunddomestic.model.CreateRequest;
import com.wechat.pay.java.service.refunddomestic.model.QueryByOutRefundNoRequest;
import com.wechat.pay.java.service.refunddomestic.model.Refund;

/** RefunddomesticService使用示例 */
public class RefunddomesticServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static RefunddomesticService service;

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
    service = new RefunddomesticService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 退款申请 */
  public static Refund create() {
    CreateRequest request = new CreateRequest();
    return service.create(request);
  }
  /** 发起异常退款 */
  public static Refund createAbnormalRefund() {

    CreateAbnormalRefundRequest request = new CreateAbnormalRefundRequest();
    return service.createAbnormalRefund(request);
  }
  /** 查询单笔退款（通过商户退款单号） */
  public static Refund queryByOutRefundNo() {

    QueryByOutRefundNoRequest request = new QueryByOutRefundNoRequest();
    return service.queryByOutRefundNo(request);
  }
}
