package com.wechat.pay.java.service.payscore.serviceorder;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.payscore.serviceorder.model.CancelServiceOrderRequest;
import com.wechat.pay.java.service.payscore.serviceorder.model.CancelServiceOrderResponse;
import com.wechat.pay.java.service.payscore.serviceorder.model.CollectServiceOrderRequest;
import com.wechat.pay.java.service.payscore.serviceorder.model.CollectServiceOrderResponse;
import com.wechat.pay.java.service.payscore.serviceorder.model.CompleteServiceOrderRequest;
import com.wechat.pay.java.service.payscore.serviceorder.model.CompleteServiceOrderResponse;
import com.wechat.pay.java.service.payscore.serviceorder.model.CreateServiceOrderRequest;
import com.wechat.pay.java.service.payscore.serviceorder.model.CreateServiceOrderResponse;
import com.wechat.pay.java.service.payscore.serviceorder.model.DirectCompleteServiceOrderRequest;
import com.wechat.pay.java.service.payscore.serviceorder.model.DirectCompleteServiceOrderResponse;
import com.wechat.pay.java.service.payscore.serviceorder.model.GetServiceOrderRequest;
import com.wechat.pay.java.service.payscore.serviceorder.model.ModifyServiceOrderRequest;
import com.wechat.pay.java.service.payscore.serviceorder.model.ServiceOrderEntity;
import com.wechat.pay.java.service.payscore.serviceorder.model.SyncServiceOrderRequest;

/** ServiceorderService使用示例 */
public class ServiceorderServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static ServiceorderService service;

  public static void main(String[] args) {
    // 初始化商户配置
    RSAConfig config =
        new RSAConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util
            // 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();

    // 初始化服务
    service = new ServiceorderService(config);
    // ... 调用接口
  }
  /** 取消 */
  public static CancelServiceOrderResponse cancelServiceOrder() {

    CancelServiceOrderRequest request = new CancelServiceOrderRequest();
    return service.cancelServiceOrder(request);
  }
  /** 收款 */
  public static CollectServiceOrderResponse collectServiceOrder() {

    CollectServiceOrderRequest request = new CollectServiceOrderRequest();
    return service.collectServiceOrder(request);
  }
  /** 完结 */
  public static CompleteServiceOrderResponse completeServiceOrder() {

    CompleteServiceOrderRequest request = new CompleteServiceOrderRequest();
    return service.completeServiceOrder(request);
  }
  /** 创建 */
  public static CreateServiceOrderResponse createServiceOrder() {
    CreateServiceOrderRequest request = new CreateServiceOrderRequest();
    return service.createServiceOrder(request);
  }
  /** 创单结单合并接口 */
  public static DirectCompleteServiceOrderResponse directCompleteServiceOrder() {
    DirectCompleteServiceOrderRequest request = new DirectCompleteServiceOrderRequest();
    return service.directCompleteServiceOrder(request);
  }
  /** 查询 */
  public static ServiceOrderEntity getServiceOrder() {

    GetServiceOrderRequest request = new GetServiceOrderRequest();
    return service.getServiceOrder(request);
  }
  /** 修改金额 */
  public static ServiceOrderEntity modifyServiceOrder() {

    ModifyServiceOrderRequest request = new ModifyServiceOrderRequest();
    return service.modifyServiceOrder(request);
  }
  /** 同步服务订单信息 */
  public static ServiceOrderEntity syncServiceOrder() {

    SyncServiceOrderRequest request = new SyncServiceOrderRequest();
    return service.syncServiceOrder(request);
  }
}
