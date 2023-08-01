package com.wechat.pay.java.service.transferbatch;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.transferbatch.model.GetTransferBatchByNoRequest;
import com.wechat.pay.java.service.transferbatch.model.GetTransferBatchByOutNoRequest;
import com.wechat.pay.java.service.transferbatch.model.GetTransferDetailByNoRequest;
import com.wechat.pay.java.service.transferbatch.model.GetTransferDetailByOutNoRequest;
import com.wechat.pay.java.service.transferbatch.model.InitiateBatchTransferRequest;
import com.wechat.pay.java.service.transferbatch.model.InitiateBatchTransferResponse;
import com.wechat.pay.java.service.transferbatch.model.TransferBatchEntity;
import com.wechat.pay.java.service.transferbatch.model.TransferDetailEntity;

/** TransferBatchService使用示例 */
public class TransferBatchServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static TransferBatchService service;

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
    service = new TransferBatchService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 通过微信批次单号查询批次单 */
  public static TransferBatchEntity getTransferBatchByNo() {

    GetTransferBatchByNoRequest request = new GetTransferBatchByNoRequest();
    return service.getTransferBatchByNo(request);
  }
  /** 通过商家批次单号查询批次单 */
  public static TransferBatchEntity getTransferBatchByOutNo() {

    GetTransferBatchByOutNoRequest request = new GetTransferBatchByOutNoRequest();
    return service.getTransferBatchByOutNo(request);
  }
  /** 发起商家转账 */
  public static InitiateBatchTransferResponse initiateBatchTransfer() {
    InitiateBatchTransferRequest request = new InitiateBatchTransferRequest();
    return service.initiateBatchTransfer(request);
  }
  /** 通过微信明细单号查询明细单 */
  public static TransferDetailEntity getTransferDetailByNo() {

    GetTransferDetailByNoRequest request = new GetTransferDetailByNoRequest();
    return service.getTransferDetailByNo(request);
  }
  /** 通过商家明细单号查询明细单 */
  public static TransferDetailEntity getTransferDetailByOutNo() {

    GetTransferDetailByOutNoRequest request = new GetTransferDetailByOutNoRequest();
    return service.getTransferDetailByOutNo(request);
  }
}
