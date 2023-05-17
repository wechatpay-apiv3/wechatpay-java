package com.wechat.pay.java.service.ecommerce.refund;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.ecommerce.refund.model.CreateRefundRequest;
import com.wechat.pay.java.service.ecommerce.refund.model.CreateReturnAdvanceRequest;
import com.wechat.pay.java.service.ecommerce.refund.model.QueryRefundByOutRefundNoRequest;
import com.wechat.pay.java.service.ecommerce.refund.model.QueryRefundRequest;
import com.wechat.pay.java.service.ecommerce.refund.model.QueryReturnAdvanceRequest;
import com.wechat.pay.java.service.ecommerce.refund.model.Refund;
import com.wechat.pay.java.service.ecommerce.refund.model.Refund4Create;
import com.wechat.pay.java.service.ecommerce.refund.model.ReturnAdvance;

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
            // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();

    // 初始化服务
    service = new RefundService.Builder().config(config).build();
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
