package com.wechat.pay.java.service.cashcoupons;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.cashcoupons.model.AvailableMerchantCollection;
import com.wechat.pay.java.service.cashcoupons.model.AvailableSingleitemCollection;
import com.wechat.pay.java.service.cashcoupons.model.Callback;
import com.wechat.pay.java.service.cashcoupons.model.Coupon;
import com.wechat.pay.java.service.cashcoupons.model.CouponCollection;
import com.wechat.pay.java.service.cashcoupons.model.CreateCouponStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.CreateCouponStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.ListAvailableMerchantsRequest;
import com.wechat.pay.java.service.cashcoupons.model.ListAvailableSingleitemsRequest;
import com.wechat.pay.java.service.cashcoupons.model.ListCouponsByFilterRequest;
import com.wechat.pay.java.service.cashcoupons.model.ListStocksRequest;
import com.wechat.pay.java.service.cashcoupons.model.PauseStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.PauseStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.QueryCallbackRequest;
import com.wechat.pay.java.service.cashcoupons.model.QueryCouponRequest;
import com.wechat.pay.java.service.cashcoupons.model.QueryStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.RefundFlowRequest;
import com.wechat.pay.java.service.cashcoupons.model.RefundFlowResponse;
import com.wechat.pay.java.service.cashcoupons.model.RestartStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.RestartStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.SendCouponRequest;
import com.wechat.pay.java.service.cashcoupons.model.SendCouponResponse;
import com.wechat.pay.java.service.cashcoupons.model.SetCallbackRequest;
import com.wechat.pay.java.service.cashcoupons.model.SetCallbackResponse;
import com.wechat.pay.java.service.cashcoupons.model.StartStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.StartStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.Stock;
import com.wechat.pay.java.service.cashcoupons.model.StockCollection;
import com.wechat.pay.java.service.cashcoupons.model.StopStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.StopStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.UseFlowRequest;
import com.wechat.pay.java.service.cashcoupons.model.UseFlowResponse;

/** CashCouponsService使用示例 */
public class CashCouponsServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static CashCouponsService service;

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
    service = new CashCouponsService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 查询代金券消息通知地址 */
  public static Callback queryCallback() {

    QueryCallbackRequest request = new QueryCallbackRequest();
    return service.queryCallback(request);
  }
  /** 设置代金券消息通知地址 */
  public static SetCallbackResponse setCallback() {
    SetCallbackRequest request = new SetCallbackRequest();
    return service.setCallback(request);
  }
  /** 根据过滤条件查询用户的券 */
  public static CouponCollection listCouponsByFilter() {

    ListCouponsByFilterRequest request = new ListCouponsByFilterRequest();
    return service.listCouponsByFilter(request);
  }
  /** 查询代金券详情 */
  public static Coupon queryCoupon() {

    QueryCouponRequest request = new QueryCouponRequest();
    return service.queryCoupon(request);
  }
  /** 发放指定批次的代金券 */
  public static SendCouponResponse sendCoupon() {

    SendCouponRequest request = new SendCouponRequest();
    return service.sendCoupon(request);
  }
  /** 创建代金券批次 */
  public static CreateCouponStockResponse createCouponStock() {
    CreateCouponStockRequest request = new CreateCouponStockRequest();
    return service.createCouponStock(request);
  }
  /** 查询代金券可用商户 */
  public static AvailableMerchantCollection listAvailableMerchants() {

    ListAvailableMerchantsRequest request = new ListAvailableMerchantsRequest();
    return service.listAvailableMerchants(request);
  }
  /** 查询可核销商品编码 */
  public static AvailableSingleitemCollection listAvailableSingleitems() {

    ListAvailableSingleitemsRequest request = new ListAvailableSingleitemsRequest();
    return service.listAvailableSingleitems(request);
  }
  /** 条件查询批次列表 */
  public static StockCollection listStocks() {

    ListStocksRequest request = new ListStocksRequest();
    return service.listStocks(request);
  }
  /** 暂停批次 */
  public static PauseStockResponse pauseStock() {

    PauseStockRequest request = new PauseStockRequest();
    return service.pauseStock(request);
  }
  /** 查询批次详情 */
  public static Stock queryStock() {

    QueryStockRequest request = new QueryStockRequest();
    return service.queryStock(request);
  }
  /** 下载批次退款明细 */
  public static RefundFlowResponse refundFlow() {

    RefundFlowRequest request = new RefundFlowRequest();
    return service.refundFlow(request);
  }
  /** 重启批次 */
  public static RestartStockResponse restartStock() {

    RestartStockRequest request = new RestartStockRequest();
    return service.restartStock(request);
  }
  /** 激活开启批次 */
  public static StartStockResponse startStock() {

    StartStockRequest request = new StartStockRequest();
    return service.startStock(request);
  }
  /** 终止批次 */
  public static StopStockResponse stopStock() {

    StopStockRequest request = new StopStockRequest();
    return service.stopStock(request);
  }
  /** 下载批次核销明细 */
  public static UseFlowResponse useFlow() {

    UseFlowRequest request = new UseFlowRequest();
    return service.useFlow(request);
  }
}
