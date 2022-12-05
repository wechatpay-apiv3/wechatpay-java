package com.wechat.pay.java.service.lovefeast;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.lovefeast.model.BrandEntity;
import com.wechat.pay.java.service.lovefeast.model.GetBrandRequest;
import com.wechat.pay.java.service.lovefeast.model.GetByUserRequest;
import com.wechat.pay.java.service.lovefeast.model.ListByUserRequest;
import com.wechat.pay.java.service.lovefeast.model.OrdersEntity;
import com.wechat.pay.java.service.lovefeast.model.OrdersListByUserResponse;

/** LovefeastService使用示例 */
public class LovefeastServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static LovefeastService service;

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
