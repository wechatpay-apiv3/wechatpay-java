// Copyright 2021 Tencent Inc. All rights reserved.
//
// JSAPI支付
//
// JSAPI支付API
//
// API version: 1.2.3

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.payment.jsapi;

import static com.wechat.pay.java.core.http.UrlEncoder.urlEncode;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.ParseException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.HttpResponse;
import com.wechat.pay.java.core.http.JsonRequestBody;
import com.wechat.pay.java.core.http.MediaType;
import com.wechat.pay.java.service.payment.jsapi.model.CloseOrderRequest;
import com.wechat.pay.java.service.payment.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payment.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.payment.jsapi.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.payment.jsapi.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.payment.model.Transaction;

/** JsapiService服务 */
public class JsapiService {

  private final HttpClient httpClient;

  public JsapiService(Config config) {
    this.httpClient =
        new DefaultHttpClientBuilder()
            .credential(requireNonNull(config.createCredential()))
            .validator(requireNonNull(config.createValidator()))
            .build();
  }

  public JsapiService(HttpClient httpClient) {
    this.httpClient = requireNonNull(httpClient);
  }

  /**
   * 关闭订单
   *
   * @param request 请求参数
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public void closeOrder(CloseOrderRequest request) {
    String requestPath =
        "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/{out_trade_no}/close";
    // 添加 path param
    requestPath =
        requestPath.replace("{" + "out_trade_no" + "}", urlEncode(request.getOutTradeNo()));
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.POST)
            .url(requestPath)
            .headers(headers)
            .body(new JsonRequestBody.Builder().body(request.toString()).build())
            .build();
    httpClient.execute(httpRequest, null);
  }
  /**
   * JSAPI支付下单
   *
   * @param request 请求参数
   * @return PrepayResponse
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public PrepayResponse prepay(PrepayRequest request) {
    String requestPath = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.POST)
            .url(requestPath)
            .headers(headers)
            .body(new JsonRequestBody.Builder().body(request.toString()).build())
            .build();
    HttpResponse<PrepayResponse> httpResponse =
        httpClient.execute(httpRequest, PrepayResponse.class);
    return httpResponse.getServiceResponse();
  }
  /**
   * 微信支付订单号查询订单
   *
   * @param request 请求参数
   * @return Transaction
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public Transaction queryOrderById(QueryOrderByIdRequest request) {
    String requestPath = "https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}";
    // 添加 path param
    requestPath =
        requestPath.replace("{" + "transaction_id" + "}", urlEncode(request.getTransactionId()));
    // 添加 query param
    if (request.getMchid() != null) {
      requestPath += "?mchid=" + urlEncode(request.getMchid());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(requestPath)
            .headers(headers)
            .build();
    HttpResponse<Transaction> httpResponse = httpClient.execute(httpRequest, Transaction.class);
    return httpResponse.getServiceResponse();
  }
  /**
   * 商户订单号查询订单
   *
   * @param request 请求参数
   * @return Transaction
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public Transaction queryOrderByOutTradeNo(QueryOrderByOutTradeNoRequest request) {
    String requestPath =
        "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/{out_trade_no}";
    // 添加 path param
    requestPath =
        requestPath.replace("{" + "out_trade_no" + "}", urlEncode(request.getOutTradeNo()));
    // 添加 query param
    if (request.getMchid() != null) {
      requestPath += "?mchid=" + urlEncode(request.getMchid());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(requestPath)
            .headers(headers)
            .build();
    HttpResponse<Transaction> httpResponse = httpClient.execute(httpRequest, Transaction.class);
    return httpResponse.getServiceResponse();
  }
}
