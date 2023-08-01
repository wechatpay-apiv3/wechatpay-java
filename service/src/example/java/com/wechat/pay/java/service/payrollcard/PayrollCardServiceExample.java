package com.wechat.pay.java.service.payrollcard;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payrollcard.model.AuthenticationEntity;
import com.wechat.pay.java.service.payrollcard.model.CreateTokenRequest;
import com.wechat.pay.java.service.payrollcard.model.CreateTransferBatchRequest;
import com.wechat.pay.java.service.payrollcard.model.GetAuthenticationRequest;
import com.wechat.pay.java.service.payrollcard.model.GetRelationRequest;
import com.wechat.pay.java.service.payrollcard.model.ListAuthenticationsRequest;
import com.wechat.pay.java.service.payrollcard.model.ListAuthenticationsResponse;
import com.wechat.pay.java.service.payrollcard.model.PreOrderAuthenticationRequest;
import com.wechat.pay.java.service.payrollcard.model.PreOrderAuthenticationResponse;
import com.wechat.pay.java.service.payrollcard.model.PreOrderAuthenticationWithAuthRequest;
import com.wechat.pay.java.service.payrollcard.model.PreOrderAuthenticationWithAuthResponse;
import com.wechat.pay.java.service.payrollcard.model.RelationEntity;
import com.wechat.pay.java.service.payrollcard.model.TokenEntity;
import com.wechat.pay.java.service.payrollcard.model.TransferBatchEntity;

/** PayrollCardService使用示例 */
public class PayrollCardServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static PayrollCardService service;

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
    service = new PayrollCardService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 获取核身结果 */
  public static AuthenticationEntity getAuthentication() {

    GetAuthenticationRequest request = new GetAuthenticationRequest();
    return service.getAuthentication(request);
  }
  /** 查询核身记录 */
  public static ListAuthenticationsResponse listAuthentications() {

    ListAuthenticationsRequest request = new ListAuthenticationsRequest();
    return service.listAuthentications(request);
  }
  /** 微工卡核身预下单 */
  public static PreOrderAuthenticationResponse preOrderAuthentication() {
    PreOrderAuthenticationRequest request = new PreOrderAuthenticationRequest();
    return service.preOrderAuthentication(request);
  }
  /** 微工卡核身预下单（流程中完成授权） */
  public static PreOrderAuthenticationWithAuthResponse preOrderAuthenticationWithAuth() {
    PreOrderAuthenticationWithAuthRequest request = new PreOrderAuthenticationWithAuthRequest();
    return service.preOrderAuthenticationWithAuth(request);
  }
  /** 查询微工卡授权关系 */
  public static RelationEntity getRelation() {

    GetRelationRequest request = new GetRelationRequest();
    return service.getRelation(request);
  }
  /** 生成授权token */
  public static TokenEntity createToken() {
    CreateTokenRequest request = new CreateTokenRequest();
    return service.createToken(request);
  }
  /** 发起批量转账 */
  public static TransferBatchEntity createTransferBatch() {
    CreateTransferBatchRequest request = new CreateTransferBatchRequest();
    return service.createTransferBatch(request);
  }
}
