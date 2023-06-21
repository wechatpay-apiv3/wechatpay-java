package com.wechat.pay.java.core.http.okhttp;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.http.AbstractHttpClient;
import com.wechat.pay.java.core.http.FileRequestBody;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.JsonRequestBody;
import com.wechat.pay.java.core.http.OriginalResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** OkHttp请求客户端 */
public final class OkHttpClientAdapter extends AbstractHttpClient {

  private static final Logger logger = LoggerFactory.getLogger(OkHttpClientAdapter.class);
  private static final String META_NAME = "meta";
  private static final String FILE_NAME = "file";

  private final okhttp3.OkHttpClient okHttpClient;

  public OkHttpClientAdapter(
      Credential credential, Validator validator, okhttp3.OkHttpClient client) {
    super(credential, validator);
    this.okHttpClient = requireNonNull(client);
  }

  @Override
  protected String getHttpClientInfo() {
    return "okhttp3/" + okHttpClient.getClass().getPackage().getImplementationVersion();
  }

  @Override
  public OriginalResponse innerExecute(HttpRequest wechatPayRequest) {
    Request okHttpRequest = buildOkHttpRequest(wechatPayRequest);
    try (Response okHttpResponse = okHttpClient.newCall(okHttpRequest).execute()) {
      return assembleOriginalResponse(wechatPayRequest, okHttpResponse);
    } catch (IOException e) {
      throw new HttpException(wechatPayRequest, e);
    }
  }

  private Request buildOkHttpRequest(HttpRequest wechatPayRequest) {
    Request.Builder okHttpRequestBuilder = new Request.Builder().url(wechatPayRequest.getUrl());
    Map<String, String> headers = wechatPayRequest.getHeaders().getHeaders();
    headers.forEach(okHttpRequestBuilder::addHeader);
    String method = wechatPayRequest.getHttpMethod().name();
    RequestBody okHttpRequestBody =
        method.equals("GET") ? null : buildOkHttpRequestBody(wechatPayRequest.getBody());
    okHttpRequestBuilder.method(method, okHttpRequestBody);
    return okHttpRequestBuilder.build();
  }

  private RequestBody buildOkHttpRequestBody(
      com.wechat.pay.java.core.http.RequestBody wechatPayRequestBody) {
    if (wechatPayRequestBody == null) {
      // create an empty request body
      return RequestBody.create("", null);
    }
    if (wechatPayRequestBody instanceof JsonRequestBody) {
      return createOkHttpRequestBody(wechatPayRequestBody);
    }
    if (wechatPayRequestBody instanceof FileRequestBody) {
      return createOkHttpMultipartRequestBody(wechatPayRequestBody);
    }
    logger.error(
        "When an http request is sent and the okhttp request body is constructed, the requestBody parameter"
            + " type cannot be found,requestBody class name[{}]",
        wechatPayRequestBody.getClass().getName());
    return null;
  }

  @SuppressWarnings("deprecation")
  private okhttp3.RequestBody createRequestBody(String content, okhttp3.MediaType mediaType) {
    // use an OkHttp3.x compatible method
    // see https://github.com/wechatpay-apiv3/wechatpay-java/issues/70
    return okhttp3.RequestBody.create(mediaType, content);
  }

  @SuppressWarnings("deprecation")
  private okhttp3.RequestBody createRequestBody(byte[] content, okhttp3.MediaType mediaType) {
    return okhttp3.RequestBody.create(mediaType, content);
  }

  private RequestBody createOkHttpRequestBody(
      com.wechat.pay.java.core.http.RequestBody wechatPayRequestBody) {
    return createRequestBody(
        ((JsonRequestBody) wechatPayRequestBody).getBody(),
        okhttp3.MediaType.parse(wechatPayRequestBody.getContentType()));
  }

  private RequestBody createOkHttpMultipartRequestBody(
      com.wechat.pay.java.core.http.RequestBody wechatPayRequestBody) {
    FileRequestBody fileRequestBody = (FileRequestBody) wechatPayRequestBody;
    okhttp3.RequestBody okHttpFileBody =
        createRequestBody(
            fileRequestBody.getFile(), okhttp3.MediaType.parse(fileRequestBody.getContentType()));
    return new okhttp3.MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(META_NAME, fileRequestBody.getMeta())
        .addFormDataPart(FILE_NAME, fileRequestBody.getFileName(), okHttpFileBody)
        .build();
  }

  private Map<String, String> assembleResponseHeader(Response okHttpResponse) {
    Map<String, String> responseHeaders = new ConcurrentHashMap<>();
    // use an OkHttp3.x compatible method
    int headerSize = okHttpResponse.headers().size();
    for (int i = 0; i < headerSize; ++i) {
      responseHeaders.put(okHttpResponse.headers().name(i), okHttpResponse.headers().value(i));
    }
    return responseHeaders;
  }

  private OriginalResponse assembleOriginalResponse(
      HttpRequest wechatPayRequest, Response okHttpResponse) {
    Map<String, String> responseHeaders = assembleResponseHeader(okHttpResponse);
    try {
      return new OriginalResponse.Builder()
          .request(wechatPayRequest)
          .headers(responseHeaders)
          .statusCode(okHttpResponse.code())
          .contentType(
              okHttpResponse.body() == null || okHttpResponse.body().contentType() == null
                  ? null
                  : okHttpResponse.body().contentType().toString())
          .body(okHttpResponse.body().string())
          .build();
    } catch (IOException e) {
      throw new MalformedMessageException(
          String.format(
              "Assemble OriginalResponse,get responseBody failed.%nHttpRequest[%s]",
              wechatPayRequest));
    }
  }

  @Override
  protected InputStream innerDownload(HttpRequest httpRequest) {
    Request okHttpRequest = buildOkHttpRequest(httpRequest);
    try {
      Response okHttpResponse = okHttpClient.newCall(okHttpRequest).execute();
      if (isInvalidHttpCode(okHttpResponse.code())) {
        throw new ServiceException(httpRequest, okHttpResponse.code(), "");
      }
      InputStream responseBodyStream = null;
      if (okHttpResponse.body() != null) {
        responseBodyStream = okHttpResponse.body().byteStream();
      }
      return responseBodyStream;
    } catch (IOException e) {
      throw new HttpException(httpRequest, e);
    }
  }
}
