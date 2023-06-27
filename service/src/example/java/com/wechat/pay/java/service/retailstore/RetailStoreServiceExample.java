package com.wechat.pay.java.service.retailstore;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.retailstore.model.AddRepresentativeRequest;
import com.wechat.pay.java.service.retailstore.model.ApplyActivityRequest;
import com.wechat.pay.java.service.retailstore.model.ApplyActivityResponse;
import com.wechat.pay.java.service.retailstore.model.CreateMaterialsRequest;
import com.wechat.pay.java.service.retailstore.model.DeleteRepresentativeRequest;
import com.wechat.pay.java.service.retailstore.model.ListActsByAreaRequest;
import com.wechat.pay.java.service.retailstore.model.ListActsByAreaResponse;
import com.wechat.pay.java.service.retailstore.model.ListRepresentativeRequest;
import com.wechat.pay.java.service.retailstore.model.LockQualificationRequest;
import com.wechat.pay.java.service.retailstore.model.LockQualificationResponse;
import com.wechat.pay.java.service.retailstore.model.RetailStoreActAddRepresentativeResponse;
import com.wechat.pay.java.service.retailstore.model.RetailStoreActCreateMaterials;
import com.wechat.pay.java.service.retailstore.model.RetailStoreActDeleteRepresentativeResponse;
import com.wechat.pay.java.service.retailstore.model.RetailStoreActListRepresentativeResponse;
import com.wechat.pay.java.service.retailstore.model.UnlockQualificationRequest;
import com.wechat.pay.java.service.retailstore.model.UnlockQualificationResponse;

/** RetailStoreService使用示例 */
public class RetailStoreServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static RetailStoreService service;

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
    service = new RetailStoreService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 门店报名品牌加价购活动 */
  public static ApplyActivityResponse applyActivity() {

    ApplyActivityRequest request = new ApplyActivityRequest();
    return service.applyActivity(request);
  }
  /** 按区域查询品牌加价购活动 */
  public static ListActsByAreaResponse listActsByArea() {

    ListActsByAreaRequest request = new ListActsByAreaRequest();
    return service.listActsByArea(request);
  }
  /** 锁定品牌加价购活动资格 */
  public static LockQualificationResponse lockQualification() {
    LockQualificationRequest request = new LockQualificationRequest();
    return service.lockQualification(request);
  }
  /** 解锁品牌加价购活动资格 */
  public static UnlockQualificationResponse unlockQualification() {
    UnlockQualificationRequest request = new UnlockQualificationRequest();
    return service.unlockQualification(request);
  }
  /** 添加零售小店活动业务代理 */
  public static RetailStoreActAddRepresentativeResponse addRepresentative() {

    AddRepresentativeRequest request = new AddRepresentativeRequest();
    return service.addRepresentative(request);
  }
  /** 生成小店活动物料码 */
  public static RetailStoreActCreateMaterials createMaterials() {

    CreateMaterialsRequest request = new CreateMaterialsRequest();
    return service.createMaterials(request);
  }
  /** 删除零售小店活动业务代理 */
  public static RetailStoreActDeleteRepresentativeResponse deleteRepresentative() {

    DeleteRepresentativeRequest request = new DeleteRepresentativeRequest();
    return service.deleteRepresentative(request);
  }
  /** 查询零售小店活动业务代理 */
  public static RetailStoreActListRepresentativeResponse listRepresentative() {

    ListRepresentativeRequest request = new ListRepresentativeRequest();
    return service.listRepresentative(request);
  }
}
