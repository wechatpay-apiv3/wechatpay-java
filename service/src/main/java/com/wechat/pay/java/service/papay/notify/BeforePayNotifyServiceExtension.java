package com.wechat.pay.java.service.papay.notify;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.service.papay.notify.model.BeforePayNotifyRequest;

/**
 * 周期扣款 预扣款通知 扩展类
 */
public class BeforePayNotifyServiceExtension {
  private final Signer signer;
  private final String signType;
  private final BeforePayNotifyService beforePayNotifyService;

  private BeforePayNotifyServiceExtension(
      Config config, HttpClient httpClient, HostName hostName, String signType) {
    this.signer = config.createSigner();
    BeforePayNotifyService.Builder builder = new BeforePayNotifyService.Builder().config(config);
    this.signType = signType;
    if (httpClient != null) {
      builder.httpClient(httpClient);
    }
    if (hostName != null) {
      builder.hostName(hostName);
    }
    this.beforePayNotifyService = builder.build();
  }

  /**
   * 发送 预扣款通知
   *
   * @param request 请求参数
   */
  public void sendNotify(BeforePayNotifyRequest request) {
    beforePayNotifyService.sendNotify(request);
  }

  public static class Builder {
    private Config config;
    private HttpClient httpClient;
    private HostName hostName;
    private String signType;

    public BeforePayNotifyServiceExtension.Builder config(Config config) {
      this.config = config;
      return this;
    }

    public BeforePayNotifyServiceExtension.Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public BeforePayNotifyServiceExtension.Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    public BeforePayNotifyServiceExtension.Builder signType(String signType) {
      this.signType = signType;
      return this;
    }

    public BeforePayNotifyServiceExtension build() {
      return new BeforePayNotifyServiceExtension(
          config, httpClient, hostName, signType == null ? "RSA" : signType);
    }
  }

}
