package com.wechat.pay.java.core.http;

import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.ParseException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;

/** HTTP请求客户端，自动生成签名和验证签名 */
public interface HttpClient {

  /**
   * 发送HTTP请求
   *
   * @param request HTTP请求
   * @param responseClass 业务返回类的Class对象，如果不确定业务返回类型，或该请求没有返回体，可以传入Object.class
   * @param <T> 由Class对象建模的类的类型
   * @return HTTP返回
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  <T> HttpResponse<T> execute(HttpRequest request, Class<T> responseClass);

  /**
   * 发送GET请求
   *
   * @param headers 请求头
   * @param url 请求URL
   * @param responseClass 业务返回类的Class对象，如果不确定业务返回类型，或该请求没有返回体，可以传入Object.class
   * @param <T> 由Class对象建模的类的类型
   * @return HTTP返回
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  <T> HttpResponse<T> get(HttpHeaders headers, String url, Class<T> responseClass);

  /**
   * 发送POST请求
   *
   * @param headers 请求头
   * @param url 请求URL
   * @param body 请求体
   * @param responseClass 业务返回类的Class对象，如果不确定业务返回类型，或该请求没有返回体，可以传入Object.class
   * @param <T> 由Class对象建模的类的类型
   * @return HTTP返回
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  <T> HttpResponse<T> post(
      HttpHeaders headers, String url, RequestBody body, Class<T> responseClass);

  /**
   * 发送PATCH请求
   *
   * @param headers 请求头
   * @param url 请求URL
   * @param body 请求体
   * @param responseClass 业务返回类的Class对象，如果不确定业务返回类型，或该请求没有返回体，可以传入Object.class
   * @param <T> 由Class对象建模的类的类型
   * @return HTTP返回
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  <T> HttpResponse<T> patch(
      HttpHeaders headers, String url, RequestBody body, Class<T> responseClass);

  /**
   * 发送PUT请求
   *
   * @param headers 请求头
   * @param url 请求URL
   * @param body 请求体
   * @param responseClass 业务返回类的Class对象，如果不确定业务返回类型，或该请求没有返回体，可以传入Object.class
   * @param <T> 由Class对象建模的类的类型
   * @return HTTP返回
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  <T> HttpResponse<T> put(
      HttpHeaders headers, String url, RequestBody body, Class<T> responseClass);

  /**
   * 发送DELETE请求
   *
   * @param headers 请求头
   * @param url 请求URL
   * @param responseClass 业务返回类的Class对象，如果不确定业务返回类型，或该请求没有返回体，可以传入Object.class
   * @param <T> 由Class对象建模的类的类型
   * @return HTTP返回
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws ParseException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  <T> HttpResponse<T> delete(HttpHeaders headers, String url, Class<T> responseClass);
}
