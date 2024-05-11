// Copyright 2021 Tencent Inc. All rights reserved.
//
// 微信支付平台补差API
//
// 微信支付平台补差API
//
// API version: 0.1.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.ecommercesubsidy;

import static com.wechat.pay.java.core.util.GsonUtil.toJson;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.HttpResponse;
import com.wechat.pay.java.core.http.JsonRequestBody;
import com.wechat.pay.java.core.http.MediaType;
import com.wechat.pay.java.core.http.RequestBody;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCancelEntity;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCancelRequest;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCreateEntity;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCreateRequest;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesReturnEntity;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesReturnRequest;

/** EcommerceSubsidyService服务 */
public class EcommerceSubsidyService {

  private final HttpClient httpClient;
  private final HostName hostName;

  private EcommerceSubsidyService(HttpClient httpClient, HostName hostName) {
    this.httpClient = requireNonNull(httpClient);
    this.hostName = hostName;
  }

  /** EcommerceSubsidyService构造器 */
  public static class Builder {

    private HttpClient httpClient;
    private HostName hostName;

    public Builder config(Config config) {
      this.httpClient = new DefaultHttpClientBuilder().config(config).build();

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

    public EcommerceSubsidyService build() {
      return new EcommerceSubsidyService(httpClient, hostName);
    }
  }

  /**
   * 取消补差
   *
   * @param request 请求参数
   * @return SubsidiesCancelEntity
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public SubsidiesCancelEntity cancelSubsidy(SubsidiesCancelRequest request) {
    String requestPath = "https://api.mch.weixin.qq.com/v3/ecommerce/subsidies/cancel";
    SubsidiesCancelRequest realRequest = request;
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
    HttpResponse<SubsidiesCancelEntity> httpResponse =
        httpClient.execute(httpRequest, SubsidiesCancelEntity.class);
    return httpResponse.getServiceResponse();
  }

  /**
   * 请求补差
   *
   * @param request 请求参数
   * @return SubsidiesCreateEntity
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public SubsidiesCreateEntity createSubsidy(SubsidiesCreateRequest request) {
    String requestPath = "https://api.mch.weixin.qq.com/v3/ecommerce/subsidies/create";
    SubsidiesCreateRequest realRequest = request;
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
    HttpResponse<SubsidiesCreateEntity> httpResponse =
        httpClient.execute(httpRequest, SubsidiesCreateEntity.class);
    return httpResponse.getServiceResponse();
  }

  /**
   * 请求补差回退
   *
   * @param request 请求参数
   * @return SubsidiesReturnEntity
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public SubsidiesReturnEntity returnSubsidy(SubsidiesReturnRequest request) {
    String requestPath = "https://api.mch.weixin.qq.com/v3/ecommerce/subsidies/return";
    SubsidiesReturnRequest realRequest = request;
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
    HttpResponse<SubsidiesReturnEntity> httpResponse =
        httpClient.execute(httpRequest, SubsidiesReturnEntity.class);
    return httpResponse.getServiceResponse();
  }

  private RequestBody createRequestBody(Object request) {
    return new JsonRequestBody.Builder().body(toJson(request)).build();
  }
}
