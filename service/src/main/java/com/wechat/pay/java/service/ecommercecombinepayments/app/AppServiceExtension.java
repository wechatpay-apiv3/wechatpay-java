package com.wechat.pay.java.service.ecommercecombinepayments.app;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.util.NonceUtil;
import com.wechat.pay.java.service.ecommercecombinepayments.app.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * APP 支付的扩展类。
 *
 * <p>它封装了 AppService，并提供了一个增强的 APP 下单方法 prepayWithRequestPayment。
 */
public class AppServiceExtension {
  private final Signer signer;
  private final AppService appService;
  private static final Logger logger = LoggerFactory.getLogger(AppServiceExtension.class);

  private AppServiceExtension(Config config, HttpClient httpClient, HostName hostName) {
    this.signer = config.createSigner();
    AppService.Builder builder = new AppService.Builder().config(config);
    if (httpClient != null) {
      builder.httpClient(httpClient);
    }
    if (hostName != null) {
      builder.hostName(hostName);
    }
    this.appService = builder.build();
  }

  /**
   * APP 支付下单，并返回 APP 调起支付数据。推荐使用！
   *
   * <p>请求成功后，该方法返回预支付交易会话标识 prepay_id 和客户端 APP 调起支付所需参数。 它相比 AppService.prepay
   * 更简单易用，因为无需开发者自行计算调起支付签名。
   *
   * @param request 请求参数
   * @param requestPaymentAppid 商户申请的公众号对应的appid
   * @return PrepayWithRequestPaymentResponse
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public PrepayWithRequestPaymentResponse prepayWithRequestPayment(
          PrepayRequest request, String requestPaymentAppid) {
    String prepayId = appService.prepay(request).getPrepayId();
    long timestamp = Instant.now().getEpochSecond();
    String nonceStr = NonceUtil.createNonce(32);
    String message =
        requestPaymentAppid + "\n" + timestamp + "\n" + nonceStr + "\n" + prepayId + "\n";
    logger.debug("Message for RequestPayment signatures is[{}]", message);
    String sign = signer.sign(message).getSign();
    PrepayWithRequestPaymentResponse response = new PrepayWithRequestPaymentResponse();
    response.setAppid(requestPaymentAppid);
    response.setPartnerId(request.getCombineMchid());
    response.setPrepayId(prepayId);
    response.setPackageVal("Sign=WXPay");
    response.setNonceStr(nonceStr);
    response.setTimestamp(String.valueOf(timestamp));
    response.setSign(sign);
    return response;
  }

  public static class Builder {
    private Config config;

    private HttpClient httpClient;
    private HostName hostName;

    public Builder config(Config config) {
      this.config = config;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    public AppServiceExtension build() {
      return new AppServiceExtension(config, httpClient, hostName);
    }
  }
}
