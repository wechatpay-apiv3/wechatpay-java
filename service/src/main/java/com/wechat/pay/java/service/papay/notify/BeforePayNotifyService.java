package com.wechat.pay.java.service.papay.notify;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.http.*;
import com.wechat.pay.java.service.papay.notify.model.BeforePayNotifyRequest;

import static com.wechat.pay.java.core.http.UrlEncoder.urlEncode;
import static com.wechat.pay.java.core.util.GsonUtil.toJson;
import static java.util.Objects.requireNonNull;

/**
 * 周期扣款 预扣款通知
 */
public class BeforePayNotifyService {

  private final HttpClient httpClient;
  private final HostName hostName;

  private BeforePayNotifyService(HttpClient httpClient, HostName hostName) {
    this.httpClient = requireNonNull(httpClient);
    this.hostName = hostName;
  }

  /**
   * H5Service构造器
   */
  public static class Builder {

    private HttpClient httpClient;
    private HostName hostName;

    /**
     * 设置请求配置，以该配置构造默认的httpClient，若未调用httpClient()方法，则必须调用该方法
     *
     * @param config 请求配置
     * @return Builder
     */
    public BeforePayNotifyService.Builder config(Config config) {
      this.httpClient = new DefaultHttpClientBuilder().config(config).build();

      return this;
    }

    /**
     * 设置微信支付域名，可选，默认为api.mch.weixin.qq.com
     *
     * @param hostName 微信支付域名
     * @return Builder
     */
    public BeforePayNotifyService.Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    /**
     * 设置自定义httpClient，若未调用config()，则必须调用该方法
     *
     * @param httpClient httpClient
     * @return Builder
     */
    public BeforePayNotifyService.Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    /**
     * 构造服务
     *
     * @return PrePayNotifyService
     */
    public BeforePayNotifyService build() {
      return new BeforePayNotifyService(httpClient, hostName);
    }

  }

  /**
   * 发送预扣款通知
   *
   * @param request 请求参数
   */
  public void sendNotify(BeforePayNotifyRequest request) {
    String requestPath = "https://api.mch.weixin.qq.com/v3/papay/contracts/{contract_id}/notify";

    BeforePayNotifyRequest realRequest = request;
    // 添加 path param
    requestPath =
        requestPath.replace("{" + "contract_id" + "}", urlEncode(realRequest.getContractId()));

    if (this.hostName != null) {
      requestPath = requestPath.replaceFirst(HostName.API.getValue(), hostName.getValue());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.POST)
            .url(requestPath)
            .headers(headers)
            .body(createRequestBody(realRequest))
            .build();

    httpClient.execute(httpRequest, null);
  }

  private RequestBody createRequestBody(Object request) {
    return new JsonRequestBody.Builder().body(toJson(request)).build();
  }

}
