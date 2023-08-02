package com.wechat.pay.java.service.lovefeast;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.lovefeast.model.BrandEntity;
import com.wechat.pay.java.service.lovefeast.model.GetBrandRequest;
import com.wechat.pay.java.service.lovefeast.model.GetByUserRequest;
import com.wechat.pay.java.service.lovefeast.model.ListByUserRequest;
import com.wechat.pay.java.service.lovefeast.model.OrdersEntity;
import com.wechat.pay.java.service.lovefeast.model.OrdersListByUserResponse;

/** LovefeastService使用示例 */
public class LovefeastServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static LovefeastService service;

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
    service = new LovefeastService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 查询爱心餐品牌信息 */
  public static BrandEntity getBrand() {

    GetBrandRequest request = new GetBrandRequest();
    return service.getBrand(request);
  }
  /** 查询用户捐赠单详情 */
  public static OrdersEntity getByUser() {

    GetByUserRequest request = new GetByUserRequest();
    return service.getByUser(request);
  }
  /** 查询用户捐赠单列表 */
  public static OrdersListByUserResponse listByUser() {

    ListByUserRequest request = new ListByUserRequest();
    return service.listByUser(request);
  }
}
