package com.wechat.pay.java.service.transferbatch;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.transferbatch.model.*;

/** TransferBatchService使用示例 */
public class TransferBatchServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static TransferBatchService service;

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
