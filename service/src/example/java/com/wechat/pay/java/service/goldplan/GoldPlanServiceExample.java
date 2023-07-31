package com.wechat.pay.java.service.goldplan;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.goldplan.model.ChangeCustomPageStatusRequest;
import com.wechat.pay.java.service.goldplan.model.ChangeCustomPageStatusResponse;
import com.wechat.pay.java.service.goldplan.model.ChangeGoldPlanStatusRequest;
import com.wechat.pay.java.service.goldplan.model.ChangeGoldPlanStatusResponse;
import com.wechat.pay.java.service.goldplan.model.CloseAdvertisingShowRequest;
import com.wechat.pay.java.service.goldplan.model.OpenAdvertisingShowRequest;
import com.wechat.pay.java.service.goldplan.model.SetAdvertisingIndustryFilterRequest;

/** GoldPlanService使用示例 */
public class GoldPlanServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String apiV3Key = "";
  public static GoldPlanService service;

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
    service = new GoldPlanService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 关闭广告展示 */
  public static void closeAdvertisingShow() {
    CloseAdvertisingShowRequest request = new CloseAdvertisingShowRequest();
    service.closeAdvertisingShow(request);
  }
  /** 开通广告展示 */
  public static void openAdvertisingShow() {
    OpenAdvertisingShowRequest request = new OpenAdvertisingShowRequest();
    service.openAdvertisingShow(request);
  }
  /** 同业过滤标签管理 */
  public static void setAdvertisingIndustryFilter() {
    SetAdvertisingIndustryFilterRequest request = new SetAdvertisingIndustryFilterRequest();
    service.setAdvertisingIndustryFilter(request);
  }
  /** 商家小票管理 */
  public static ChangeCustomPageStatusResponse changeCustomPageStatus() {
    ChangeCustomPageStatusRequest request = new ChangeCustomPageStatusRequest();
    return service.changeCustomPageStatus(request);
  }
  /** 点金计划管理 */
  public static ChangeGoldPlanStatusResponse changeGoldPlanStatus() {
    ChangeGoldPlanStatusRequest request = new ChangeGoldPlanStatusRequest();
    return service.changeGoldPlanStatus(request);
  }
}
