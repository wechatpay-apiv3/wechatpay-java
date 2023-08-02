package com.wechat.pay.java.service.merchantexclusivecoupon;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.AssociateTradeInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.AssociateTradeInfoResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponCodeInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponCodeInfoResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponCodeListResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponEntity;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponListResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponSendGovCardResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CreateBusiFavorStockRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CreateBusiFavorStockResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DeactivateCouponRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DeactivateCouponResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DeleteCouponCodeRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DeleteCouponCodeResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DisassociateTradeInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DisassociateTradeInfoResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.GetCouponNotifyRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.GetCouponNotifyResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ListCouponsByFilterRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ModifyBudgetRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ModifyBudgetResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ModifyStockInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.PayReceiptInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.PayReceiptListRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.QueryCouponCodeListRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.QueryCouponRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.QueryStockRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ReturnCouponRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ReturnCouponResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ReturnReceiptInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SendCouponRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SendCouponResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SendGovCardRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SetCouponNotifyRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SetCouponNotifyResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.StockGetResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SubsidyPayReceipt;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SubsidyPayReceiptListResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SubsidyPayRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SubsidyReturnReceipt;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SubsidyReturnRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.UploadCouponCodeRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.UploadCouponCodeResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.UseCouponRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.UseCouponResponse;

/** MerchantExclusiveCouponService使用示例 */
public class MerchantExclusiveCouponServiceExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static MerchantExclusiveCouponService service;

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
    service = new MerchantExclusiveCouponService.Builder().config(config).build();
    // ... 调用接口
  }
  /** 查询预存code详情 */
  public static CouponCodeInfoResponse couponCodeInfo() {

    CouponCodeInfoRequest request = new CouponCodeInfoRequest();
    return service.couponCodeInfo(request);
  }
  /** 创建商家券 */
  public static CreateBusiFavorStockResponse createBusifavorStock() {
    CreateBusiFavorStockRequest request = new CreateBusiFavorStockRequest();
    return service.createBusifavorStock(request);
  }
  /** 删除预存code */
  public static DeleteCouponCodeResponse deleteCouponCode() {

    DeleteCouponCodeRequest request = new DeleteCouponCodeRequest();
    return service.deleteCouponCode(request);
  }
  /** 修改批次预算 */
  public static ModifyBudgetResponse modifyBudget() {

    ModifyBudgetRequest request = new ModifyBudgetRequest();
    return service.modifyBudget(request);
  }
  /** 修改商家券基本信息 */
  public static void modifyStockInfo() {

    ModifyStockInfoRequest request = new ModifyStockInfoRequest();
    service.modifyStockInfo(request);
  }
  /** 查询预存code列表 */
  public static CouponCodeListResponse queryCouponCodeList() {

    QueryCouponCodeListRequest request = new QueryCouponCodeListRequest();
    return service.queryCouponCodeList(request);
  }
  /** 查询商家券批次详情 */
  public static StockGetResponse queryStock() {

    QueryStockRequest request = new QueryStockRequest();
    return service.queryStock(request);
  }
  /** 上传预存code */
  public static UploadCouponCodeResponse uploadCouponCode() {

    UploadCouponCodeRequest request = new UploadCouponCodeRequest();
    return service.uploadCouponCode(request);
  }
  /** 获取商家券事件通知地址 */
  public static GetCouponNotifyResponse getCouponNotify() {

    GetCouponNotifyRequest request = new GetCouponNotifyRequest();
    return service.getCouponNotify(request);
  }
  /** 设置商家券事件通知地址 */
  public static SetCouponNotifyResponse setCouponNotify() {
    SetCouponNotifyRequest request = new SetCouponNotifyRequest();
    return service.setCouponNotify(request);
  }
  /** 关联订单信息 */
  public static AssociateTradeInfoResponse associateTradeInfo() {
    AssociateTradeInfoRequest request = new AssociateTradeInfoRequest();
    return service.associateTradeInfo(request);
  }
  /** 使券失效 */
  public static DeactivateCouponResponse deactivateCoupon() {
    DeactivateCouponRequest request = new DeactivateCouponRequest();
    return service.deactivateCoupon(request);
  }
  /** 取消关联订单信息 */
  public static DisassociateTradeInfoResponse disassociateTradeInfo() {
    DisassociateTradeInfoRequest request = new DisassociateTradeInfoRequest();
    return service.disassociateTradeInfo(request);
  }
  /** 根据过滤条件查询用户的券 */
  public static CouponListResponse listCouponsByFilter() {

    ListCouponsByFilterRequest request = new ListCouponsByFilterRequest();
    return service.listCouponsByFilter(request);
  }
  /** 查询用户券详情 */
  public static CouponEntity queryCoupon() {

    QueryCouponRequest request = new QueryCouponRequest();
    return service.queryCoupon(request);
  }
  /** 申请退券 */
  public static ReturnCouponResponse returnCoupon() {
    ReturnCouponRequest request = new ReturnCouponRequest();
    return service.returnCoupon(request);
  }
  /** 向用户发券 */
  public static SendCouponResponse sendCoupon() {
    SendCouponRequest request = new SendCouponRequest();
    return service.sendCoupon(request);
  }
  /** 发放政府消费卡 */
  public static CouponSendGovCardResponse sendGovCard() {

    SendGovCardRequest request = new SendGovCardRequest();
    return service.sendGovCard(request);
  }
  /** 核销用户的券 */
  public static UseCouponResponse useCoupon() {
    UseCouponRequest request = new UseCouponRequest();
    return service.useCoupon(request);
  }
  /** 查询商家券营销补差付款单详情 */
  public static SubsidyPayReceipt payReceiptInfo() {

    PayReceiptInfoRequest request = new PayReceiptInfoRequest();
    return service.payReceiptInfo(request);
  }
  /** 查询商家券营销补差付款单列表 */
  public static SubsidyPayReceiptListResponse payReceiptList() {

    PayReceiptListRequest request = new PayReceiptListRequest();
    return service.payReceiptList(request);
  }
  /** 查询商家券营销补差回退单详情 */
  public static SubsidyReturnReceipt returnReceiptInfo() {

    ReturnReceiptInfoRequest request = new ReturnReceiptInfoRequest();
    return service.returnReceiptInfo(request);
  }
  /** 商家券营销补差付款 */
  public static SubsidyPayReceipt subsidyPay() {
    SubsidyPayRequest request = new SubsidyPayRequest();
    return service.subsidyPay(request);
  }
  /** 商家券营销补差回退 */
  public static SubsidyReturnReceipt subsidyReturn() {
    SubsidyReturnRequest request = new SubsidyReturnRequest();
    return service.subsidyReturn(request);
  }
}
