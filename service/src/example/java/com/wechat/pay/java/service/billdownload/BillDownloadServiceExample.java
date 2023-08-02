package com.wechat.pay.java.service.billdownload;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.billdownload.model.GetAllSubMchFundFlowBillRequest;
import com.wechat.pay.java.service.billdownload.model.GetFundFlowBillRequest;
import com.wechat.pay.java.service.billdownload.model.GetSingleSubMchFundFlowBillRequest;
import com.wechat.pay.java.service.billdownload.model.GetTradeBillRequest;
import com.wechat.pay.java.service.billdownload.model.QueryBillEntity;
import com.wechat.pay.java.service.billdownload.model.QueryEncryptBillEntity;

/** BillDownloadService使用示例 */
public class BillDownloadServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static BillDownloadService service;

  public static void main(String[] args) {
    // 初始化商户配置
    Config config =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .apiV3Key(apiV3Key)
            .build();

    // 初始化服务
    service = new BillDownloadService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 申请资金账单API */
  public static QueryBillEntity getFundFlowBill() {

    GetFundFlowBillRequest request = new GetFundFlowBillRequest();
    return service.getFundFlowBill(request);
  }
  /** 申请单个子商户资金账单API */
  public static QueryEncryptBillEntity getSingleSubMchFundFlowBill() {

    GetSingleSubMchFundFlowBillRequest request = new GetSingleSubMchFundFlowBillRequest();
    return service.getSingleSubMchFundFlowBill(request);
  }
  /** 申请二级商户资金账单API */
  public static QueryEncryptBillEntity getAllSubMchFundFlowBill() {

    GetAllSubMchFundFlowBillRequest request = new GetAllSubMchFundFlowBillRequest();
    return service.getAllSubMchFundFlowBill(request);
  }
  /** 申请交易账单API */
  public static QueryBillEntity getTradeBill() {

    GetTradeBillRequest request = new GetTradeBillRequest();
    return service.getTradeBill(request);
  }
}
