package com.wechat.pay.java.service.giftactivity;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.giftactivity.model.AddActivityMerchantRequest;
import com.wechat.pay.java.service.giftactivity.model.AddActivityMerchantResponse;
import com.wechat.pay.java.service.giftactivity.model.CreateFullSendActRequest;
import com.wechat.pay.java.service.giftactivity.model.CreateFullSendActResponse;
import com.wechat.pay.java.service.giftactivity.model.DeleteActivityMerchantRequest;
import com.wechat.pay.java.service.giftactivity.model.DeleteActivityMerchantResponse;
import com.wechat.pay.java.service.giftactivity.model.GetActDetailRequest;
import com.wechat.pay.java.service.giftactivity.model.GetActDetailResponse;
import com.wechat.pay.java.service.giftactivity.model.ListActMchResponse;
import com.wechat.pay.java.service.giftactivity.model.ListActSkuResponse;
import com.wechat.pay.java.service.giftactivity.model.ListActivitiesRequest;
import com.wechat.pay.java.service.giftactivity.model.ListActivitiesResponse;
import com.wechat.pay.java.service.giftactivity.model.ListActivityMerchantRequest;
import com.wechat.pay.java.service.giftactivity.model.ListActivitySkuRequest;
import com.wechat.pay.java.service.giftactivity.model.TerminateActResponse;
import com.wechat.pay.java.service.giftactivity.model.TerminateActivityRequest;

/** GiftActivityService使用示例 */
public class GiftActivityServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static GiftActivityService service;

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
    service = new GiftActivityService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 新增活动发券商户号 */
  public static AddActivityMerchantResponse addActivityMerchant() {

    AddActivityMerchantRequest request = new AddActivityMerchantRequest();
    return service.addActivityMerchant(request);
  }
  /** 创建全场满额送活动 */
  public static CreateFullSendActResponse createFullSendAct() {
    CreateFullSendActRequest request = new CreateFullSendActRequest();
    return service.createFullSendAct(request);
  }
  /** 删除活动发券商户号 */
  public static DeleteActivityMerchantResponse deleteActivityMerchant() {

    DeleteActivityMerchantRequest request = new DeleteActivityMerchantRequest();
    return service.deleteActivityMerchant(request);
  }
  /** 获取活动详情接口 */
  public static GetActDetailResponse getActDetail() {

    GetActDetailRequest request = new GetActDetailRequest();
    return service.getActDetail(request);
  }
  /** 获取支付有礼活动列表 */
  public static ListActivitiesResponse listActivities() {

    ListActivitiesRequest request = new ListActivitiesRequest();
    return service.listActivities(request);
  }
  /** 获取活动发券商户号 */
  public static ListActMchResponse listActivityMerchant() {

    ListActivityMerchantRequest request = new ListActivityMerchantRequest();
    return service.listActivityMerchant(request);
  }
  /** 获取活动指定商品列表 */
  public static ListActSkuResponse listActivitySku() {

    ListActivitySkuRequest request = new ListActivitySkuRequest();
    return service.listActivitySku(request);
  }
  /** 终止活动 */
  public static TerminateActResponse terminateActivity() {

    TerminateActivityRequest request = new TerminateActivityRequest();
    return service.terminateActivity(request);
  }
}
