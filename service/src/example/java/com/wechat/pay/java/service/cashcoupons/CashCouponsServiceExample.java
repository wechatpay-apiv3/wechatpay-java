package com.wechat.pay.java.service.cashcoupons;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.cashcoupons.model.AvailableMerchantCollection;
import com.wechat.pay.java.service.cashcoupons.model.AvailableSingleitemCollection;
import com.wechat.pay.java.service.cashcoupons.model.Callback;
import com.wechat.pay.java.service.cashcoupons.model.Coupon;
import com.wechat.pay.java.service.cashcoupons.model.CouponCollection;
import com.wechat.pay.java.service.cashcoupons.model.CreateCouponStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.CreateCouponStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.CreateNocashSingleitemStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.CreateNocashSingleitemStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.CreateNocashStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.CreateNocashStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.GetPlatformFavorRequest;
import com.wechat.pay.java.service.cashcoupons.model.ListAvailableMerchantsRequest;
import com.wechat.pay.java.service.cashcoupons.model.ListAvailableSingleitemsRequest;
import com.wechat.pay.java.service.cashcoupons.model.ListCouponsByFilterRequest;
import com.wechat.pay.java.service.cashcoupons.model.ListStocksRequest;
import com.wechat.pay.java.service.cashcoupons.model.ModifyAvailableMerchantResponse;
import com.wechat.pay.java.service.cashcoupons.model.ModifyAvailableMerchantsRequest;
import com.wechat.pay.java.service.cashcoupons.model.ModifyAvailableSingleitemResponse;
import com.wechat.pay.java.service.cashcoupons.model.ModifyAvailableSingleitemsRequest;
import com.wechat.pay.java.service.cashcoupons.model.ModifyBudgetRequest;
import com.wechat.pay.java.service.cashcoupons.model.ModifyStockBudgetResponse;
import com.wechat.pay.java.service.cashcoupons.model.ModifyStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.ModifyStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.PauseStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.PauseStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.PlatformFavorCreateRequest;
import com.wechat.pay.java.service.cashcoupons.model.PlatformFavorStockEntity;
import com.wechat.pay.java.service.cashcoupons.model.QueryCallbackRequest;
import com.wechat.pay.java.service.cashcoupons.model.QueryCouponRequest;
import com.wechat.pay.java.service.cashcoupons.model.QueryStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.RefundFlowRequest;
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
import com.wechat.pay.java.service.cashcoupons.model.StockDownloadRefundFlowResponse;
import com.wechat.pay.java.service.cashcoupons.model.StockDownloadUseFlowResponse;
import com.wechat.pay.java.service.cashcoupons.model.StopStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.StopStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.UseFlowRequest;

/** CashCouponsService使用示例 */
public class CashCouponsServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static CashCouponsService service;

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
  /** 创建立减金批次 */
  public static PlatformFavorStockEntity createPlatformFavor() {
    PlatformFavorCreateRequest request = new PlatformFavorCreateRequest();
    return service.createPlatformFavor(request);
  }
  /** 查询立减金批次信息 */
  public static PlatformFavorStockEntity getPlatformFavor() {

    GetPlatformFavorRequest request = new GetPlatformFavorRequest();
    return service.getPlatformFavor(request);
  }
  /** 创建代金券批次 */
  public static CreateCouponStockResponse createCouponStock() {
    CreateCouponStockRequest request = new CreateCouponStockRequest();
    return service.createCouponStock(request);
  }
  /** 创建无资金流单品批次 */
  public static CreateNocashSingleitemStockResponse createNocashSingleitemStock() {
    CreateNocashSingleitemStockRequest request = new CreateNocashSingleitemStockRequest();
    return service.createNocashSingleitemStock(request);
  }
  /** 创建无资金流全场批次 */
  public static CreateNocashStockResponse createNocashStock() {
    CreateNocashStockRequest request = new CreateNocashStockRequest();
    return service.createNocashStock(request);
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
  /** 修改批次可用商户号 */
  public static ModifyAvailableMerchantResponse modifyAvailableMerchants() {

    ModifyAvailableMerchantsRequest request = new ModifyAvailableMerchantsRequest();
    return service.modifyAvailableMerchants(request);
  }
  /** 修改可核销商品编码 */
  public static ModifyAvailableSingleitemResponse modifyAvailableSingleitems() {

    ModifyAvailableSingleitemsRequest request = new ModifyAvailableSingleitemsRequest();
    return service.modifyAvailableSingleitems(request);
  }
  /** 修改批次预算金额 */
  public static ModifyStockBudgetResponse modifyBudget() {

    ModifyBudgetRequest request = new ModifyBudgetRequest();
    return service.modifyBudget(request);
  }
  /** 修改批次基本信息 */
  public static ModifyStockResponse modifyStock() {

    ModifyStockRequest request = new ModifyStockRequest();
    return service.modifyStock(request);
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
  public static StockDownloadRefundFlowResponse refundFlow() {

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
  public static StockDownloadUseFlowResponse useFlow() {

    UseFlowRequest request = new UseFlowRequest();
    return service.useFlow(request);
  }
}
