// Copyright 2021 Tencent Inc. All rights reserved.
//
// 点金计划对外API
//
// 特约商户点金计划管理API
//
// API version: 0.3.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.goldplan;

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
import com.wechat.pay.java.service.goldplan.model.ChangeCustomPageStatusRequest;
import com.wechat.pay.java.service.goldplan.model.ChangeCustomPageStatusResponse;
import com.wechat.pay.java.service.goldplan.model.ChangeGoldPlanStatusRequest;
import com.wechat.pay.java.service.goldplan.model.ChangeGoldPlanStatusResponse;
import com.wechat.pay.java.service.goldplan.model.CloseAdvertisingShowRequest;
import com.wechat.pay.java.service.goldplan.model.OpenAdvertisingShowRequest;
import com.wechat.pay.java.service.goldplan.model.SetAdvertisingIndustryFilterRequest;

/** GoldPlanService服务 */
public class GoldPlanService {

  private final HttpClient httpClient;
  private final HostName hostName;

  private GoldPlanService(HttpClient httpClient, HostName hostName) {
    this.httpClient = requireNonNull(httpClient);
    this.hostName = hostName;
  }

  /** GoldPlanService构造器 */
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

    public GoldPlanService build() {
      return new GoldPlanService(httpClient, hostName);
    }
  }

  /**
   * 关闭广告展示
   *
   * @param request 请求参数
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public void closeAdvertisingShow(CloseAdvertisingShowRequest request) {
    String requestPath =
        "https://api.mch.weixin.qq.com/v3/goldplan/merchants/close-advertising-show";
    CloseAdvertisingShowRequest realRequest = request;
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

  /**
   * 开通广告展示
   *
   * @param request 请求参数
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public void openAdvertisingShow(OpenAdvertisingShowRequest request) {
    String requestPath =
        "https://api.mch.weixin.qq.com/v3/goldplan/merchants/open-advertising-show";
    OpenAdvertisingShowRequest realRequest = request;
    if (this.hostName != null) {
      requestPath = requestPath.replaceFirst(HostName.API.getValue(), hostName.getValue());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.PATCH)
            .url(requestPath)
            .headers(headers)
            .body(createRequestBody(realRequest))
            .build();
    httpClient.execute(httpRequest, null);
  }

  /**
   * 同业过滤标签管理
   *
   * @param request 请求参数
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public void setAdvertisingIndustryFilter(SetAdvertisingIndustryFilterRequest request) {
    String requestPath =
        "https://api.mch.weixin.qq.com/v3/goldplan/merchants/set-advertising-industry-filter";
    SetAdvertisingIndustryFilterRequest realRequest = request;
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

  /**
   * 商家小票管理
   *
   * @param request 请求参数
   * @return ChangeCustomPageStatusResponse
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public ChangeCustomPageStatusResponse changeCustomPageStatus(
      ChangeCustomPageStatusRequest request) {
    String requestPath =
        "https://api.mch.weixin.qq.com/v3/goldplan/merchants/changecustompagestatus";
    ChangeCustomPageStatusRequest realRequest = request;
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
    HttpResponse<ChangeCustomPageStatusResponse> httpResponse =
        httpClient.execute(httpRequest, ChangeCustomPageStatusResponse.class);
    return httpResponse.getServiceResponse();
  }

  /**
   * 点金计划管理
   *
   * @param request 请求参数
   * @return ChangeGoldPlanStatusResponse
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public ChangeGoldPlanStatusResponse changeGoldPlanStatus(ChangeGoldPlanStatusRequest request) {
    String requestPath = "https://api.mch.weixin.qq.com/v3/goldplan/merchants/changegoldplanstatus";
    ChangeGoldPlanStatusRequest realRequest = request;
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
    HttpResponse<ChangeGoldPlanStatusResponse> httpResponse =
        httpClient.execute(httpRequest, ChangeGoldPlanStatusResponse.class);
    return httpResponse.getServiceResponse();
  }

  private RequestBody createRequestBody(Object request) {
    return new JsonRequestBody.Builder().body(toJson(request)).build();
  }
}
