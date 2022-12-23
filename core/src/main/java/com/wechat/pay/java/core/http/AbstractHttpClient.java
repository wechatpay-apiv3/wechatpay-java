package com.wechat.pay.java.core.http;

import static com.wechat.pay.java.core.http.Constant.ACCEPT;
import static com.wechat.pay.java.core.http.Constant.AUTHORIZATION;
import static com.wechat.pay.java.core.http.Constant.OS;
import static com.wechat.pay.java.core.http.Constant.REQUEST_ID;
import static com.wechat.pay.java.core.http.Constant.USER_AGENT;
import static com.wechat.pay.java.core.http.Constant.USER_AGENT_FORMAT;
import static com.wechat.pay.java.core.http.Constant.VERSION;
import static java.net.HttpURLConnection.HTTP_MULT_CHOICE;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.HttpRequest.Builder;
import java.io.InputStream;

/** 请求客户端抽象基类 */
public abstract class AbstractHttpClient implements HttpClient {

  protected final Credential credential;
  protected final Validator validator;

  public AbstractHttpClient(Credential credential, Validator validator) {
    this.credential = requireNonNull(credential);
    this.validator = requireNonNull(validator);
  }

  @Override
  public <T> HttpResponse<T> execute(HttpRequest httpRequest, Class<T> responseClass) {
    HttpRequest innerRequest =
        new Builder()
            .url(httpRequest.getUrl())
            .httpMethod(httpRequest.getHttpMethod())
            .headers(httpRequest.getHeaders())
            .addHeader(AUTHORIZATION, getAuthorization(httpRequest))
            .addHeader(USER_AGENT, getUserAgent())
            .body(httpRequest.getBody())
            .build();
    OriginalResponse originalResponse = innerExecute(innerRequest);
    validateResponse(originalResponse);
    return assembleHttpResponse(originalResponse, responseClass);
  }

  @Override
  public InputStream download(String url) {
    HttpRequest originRequest =
        new HttpRequest.Builder().httpMethod(HttpMethod.GET).url(url).build();
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .url(url)
            .httpMethod(HttpMethod.GET)
            .addHeader(AUTHORIZATION, getAuthorization(originRequest))
            .addHeader(ACCEPT, "*/*")
            .addHeader(USER_AGENT, getUserAgent())
            .build();
    return innerDownload(httpRequest);
  }

  protected abstract InputStream innerDownload(HttpRequest httpRequest);

  protected abstract OriginalResponse innerExecute(HttpRequest httpRequest);

  private void validateResponse(OriginalResponse originalResponse) {

    if (isInvalidHttpCode(originalResponse.getStatusCode())) {
      throw new ServiceException(
          originalResponse.getRequest(),
          originalResponse.getStatusCode(),
          originalResponse.getBody());
    }

    if (originalResponse.getBody() != null
        && !originalResponse.getBody().isEmpty()
        && !MediaType.APPLICATION_JSON.equalsWith(originalResponse.getContentType())) {
      throw new MalformedMessageException(
          String.format(
              "Unsupported content-type[%s]%nhttpRequest[%s]",
              originalResponse.getContentType(), originalResponse.getRequest()));
    }

    if (!validator.validate(originalResponse.getHeaders(), originalResponse.getBody())) {
      String requestId = originalResponse.getHeaders().getHeader(REQUEST_ID);
      throw new ValidationException(
          String.format(
              "Validate response failed,the WechatPay signature is incorrect.%n"
                  + "Request-ID[%s]\tresponseHeader[%s]\tresponseBody[%.1024s]",
              requestId, originalResponse.getHeaders(), originalResponse.getBody()));
    }
  }

  protected boolean isInvalidHttpCode(int httpCode) {
    return httpCode < HTTP_OK || httpCode >= HTTP_MULT_CHOICE;
  }

  private <T> HttpResponse<T> assembleHttpResponse(
      OriginalResponse originalResponse, Class<T> responseClass) {
    return new HttpResponse.Builder<T>()
        .originalResponse(originalResponse)
        .serviceResponseType(responseClass)
        .build();
  }

  private String getSignBody(RequestBody requestBody) {
    if (requestBody == null) {
      return "";
    }
    if (requestBody instanceof JsonRequestBody) {
      return ((JsonRequestBody) requestBody).getBody();
    }
    if (requestBody instanceof FileRequestBody) {
      return ((FileRequestBody) requestBody).getMeta();
    }
    throw new UnsupportedOperationException(
        String.format("Unsupported RequestBody Type[%s]", requestBody.getClass().getName()));
  }

  private String getUserAgent() {
    return String.format(
        USER_AGENT_FORMAT,
        getClass().getPackage().getImplementationVersion(),
        OS,
        VERSION == null ? "Unknown" : VERSION,
        credential.getClass().getSimpleName(),
        validator.getClass().getSimpleName(),
        getHttpClientInfo());
  }

  /**
   * 获取http客户端信息，用于User-Agent。 格式：客户端名称/版本 示例：okhttp3/4.9.3
   *
   * @return 客户端信息
   */
  protected abstract String getHttpClientInfo();

  private String getAuthorization(HttpRequest request) {
    return credential.getAuthorization(
        request.getUri(), request.getHttpMethod().name(), getSignBody(request.getBody()));
  }
}
