package com.wechat.pay.java.service.mchtransfer;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.mchtransfer.model.*;

/** MchTransferService使用示例 */
public class MchTransferServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";

  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static MchTransferService service;

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
    service = new MchTransferService.Builder().config(config).build();
    // ... 调用接口
  }

  /** 微信单号查询转账单 */
  public static TransferBillEntity getTransferBillByNo() {
    GetTransferBillByNoRequest request = new GetTransferBillByNoRequest();
    return service.getTransferBillByNo(request);
  }

  /** 商户单号查询转账单 */
  public static TransferBillEntity getTransferBillByOutNo() {
    GetTransferBillByOutNoRequest request = new GetTransferBillByOutNoRequest();
    return service.getTransferBillByOutNo(request);
  }

  /** 发起商家转账 */
  public static InitiateMchTransferResponse initiateMchTransfer() {
    InitiateMchTransferRequest request = new InitiateMchTransferRequest();
    return service.initiateMchTransfer(request);
  }

  /** 撤销商家转账 */
  public static CancelMchTransferResponse cancelMchTransfer() {
    CancelMchTransferRequest request = new CancelMchTransferRequest();
    return service.cancelMchTransfer(request);
  }

  /** 微信单号申请电子回单 */
  public static AcceptElecsignResponse acceptElecsignByNo() {
    AcceptElecsignByNoRequest request = new AcceptElecsignByNoRequest();
    return service.acceptElecsignByNo(request);
  }

  /** 微信单号查询电子回单 */
  public static ElecsignEntity queryElecsignByNo() {
    QueryElecsignByNoRequest request = new QueryElecsignByNoRequest();
    return service.queryElecsignByNo(request);
  }

  /** 商户单号申请电子回单 */
  public static AcceptElecsignResponse acceptElecsignByOutNo() {
    AcceptElecsignByOutNoRequest request = new AcceptElecsignByOutNoRequest();
    return service.acceptElecsignByOutNo(request);
  }

  /** 商户单号查询电子回单 */
  public static ElecsignEntity queryElecsignByOutNo() {
    QueryElecsignByOutNoRequest request = new QueryElecsignByOutNoRequest();
    return service.queryElecsignByOutNo(request);
  }

  /** 下载电子回单 */
  public static DigestElecsignEntity downloadElecsign() {
    ElecsignEntity entity = queryElecsignByNo();
    return service.downloadElecsign(entity);
  }
}
