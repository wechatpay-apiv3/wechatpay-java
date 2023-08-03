package com.wechat.pay.java.service.billdownload;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.service.billdownload.model.GetFundFlowBillRequest;
import com.wechat.pay.java.service.billdownload.model.GetTradeBillRequest;
import com.wechat.pay.java.service.billdownload.model.QueryBillEntity;
import com.wechat.pay.java.service.billdownload.model.TarType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class BillDownloadServiceExtension {

  private final BillDownloadService service;

  private final HttpClient httpClient;

  private BillDownloadServiceExtension(Builder extBuilder) {
    this.service =
        new BillDownloadService.Builder()
            .httpClient(extBuilder.httpClient)
            .decryptor(extBuilder.decryptor)
            .hostName(extBuilder.hostName)
            .build();
    this.httpClient = extBuilder.httpClient;
  }

  /**
   * 获取交易账单。从 DigestBillEntity 得到账单的流，应在消费完后验证摘要。支持解压缩。
   *
   * @param request 请求参数
   * @return DigestBillEntity
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public DigestBillEntity getTradeBill(GetTradeBillRequest request) {
    QueryBillEntity billEntity = service.getTradeBill(request);
    InputStream stream = getBillStream(billEntity, request.getTarType());

    return new DigestBillEntity(stream, billEntity.getHashValue(), billEntity.getHashType());
  }

  /**
   * 获取资金账单。从 DigestBillEntity 得到账单的流，应在消费完后验证摘要。
   *
   * @param request 请求参数
   * @return DigestBillEntity
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public DigestBillEntity getFundFlowBill(GetFundFlowBillRequest request) {
    QueryBillEntity billEntity = service.getFundFlowBill(request);
    InputStream stream = getBillStream(billEntity, request.getTarType());

    return new DigestBillEntity(stream, billEntity.getHashValue(), billEntity.getHashType());
  }

  private InputStream getBillStream(QueryBillEntity billEntity, TarType tarType) {
    InputStream stream = httpClient.download(billEntity.getDownloadUrl());
    if (tarType == TarType.GZIP) {
      try {
        stream = new GZIPInputStream(stream);
      } catch (IOException e) {
        throw new MalformedMessageException("invalid response", e);
      }
    }
    return stream;
  }

  /** BillDownloadServiceExtension 构造器 */
  public static class Builder {

    private HttpClient httpClient;
    private HostName hostName;

    private PrivacyDecryptor decryptor;

    public Builder config(Config config) {
      Objects.requireNonNull(config, "Config must not be null");
      this.httpClient = new DefaultHttpClientBuilder().config(config).build();

      this.decryptor = config.createDecryptor();
      return this;
    }

    public Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder decryptor(PrivacyDecryptor decryptor) {
      this.decryptor = decryptor;
      return this;
    }

    public BillDownloadServiceExtension build() {
      Objects.requireNonNull(httpClient, "HttpClient must not be null");
      Objects.requireNonNull(decryptor, "Decryptor must not be null");

      return new BillDownloadServiceExtension(this);
    }
  }
}
