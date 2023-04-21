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
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** OkHttp请求客户端 */
public final class OkHttpClientAdapter extends AbstractHttpClient {

  static final class ContentLengthRequestInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
      Request original = chain.request();

      if (original.body() == null || original.body().contentLength() >= 0) {
        return chain.proceed(original);
      }

      Request newRequest =
          original
              .newBuilder()
              .method(original.method(), addContentLength(original.body()))
              .build();
      return chain.proceed(newRequest);
    }

    private RequestBody addContentLength(final RequestBody delegate) {
      return new RequestBody() {
        @Nullable
        @Override
        public MediaType contentType() {
          return delegate.contentType();
        }

        @Override
        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
          delegate.writeTo(bufferedSink);
        }

        @Override
        public long contentLength() throws IOException {
          final Buffer buffer = new Buffer();
          delegate.writeTo(buffer);
          return buffer.size();
        }
      };
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(OkHttpClientAdapter.class);
  private static final String META_NAME = "meta";
  private static final String FILE_NAME = "file";

  private final okhttp3.OkHttpClient okHttpClient;

  public OkHttpClientAdapter(
      Credential credential, Validator validator, okhttp3.OkHttpClient client) {
    super(credential, validator);

    // OkHttpClient is immutable, add Interceptor to new copy
    OkHttpClient.Builder builder = requireNonNull(client).newBuilder();
    builder.interceptors().add(0, new ContentLengthRequestInterceptor());
    this.okHttpClient = builder.build();
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
    RequestBody okHttpRequestBody = buildOkHttpRequestBody(wechatPayRequest.getBody());
    okHttpRequestBuilder.method(method, okHttpRequestBody);
    return okHttpRequestBuilder.build();
  }

  private RequestBody buildOkHttpRequestBody(
      com.wechat.pay.java.core.http.RequestBody wechatPayRequestBody) {
    if (wechatPayRequestBody == null) {
      return null;
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

  /**
   * 构造一个不会返回 Content-Length 的 RequestBody（默认是-1L） 这样构造 FormDataPart 输出 MultiPart 的时候就不会携带
   * Content-Length
   */
  private RequestBody stripLength(RequestBody delegate) {
    return new RequestBody() {
      @Override
      public MediaType contentType() {
        return delegate.contentType();
      }

      @Override
      public void writeTo(@NotNull BufferedSink sink) throws IOException {
        delegate.writeTo(sink);
      }
    };
  }

  private RequestBody createOkHttpMultipartRequestBody(
      com.wechat.pay.java.core.http.RequestBody wechatPayRequestBody) {
    FileRequestBody fileRequestBody = (FileRequestBody) wechatPayRequestBody;
    okhttp3.RequestBody okHttpMetaBody =
        createRequestBody(
            fileRequestBody.getMeta(),
            okhttp3.MediaType.parse(
                com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON.getValue()));
    okhttp3.RequestBody okHttpFileBody =
        createRequestBody(
            fileRequestBody.getFile(), okhttp3.MediaType.parse(fileRequestBody.getContentType()));
    return new okhttp3.MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(META_NAME, null, stripLength(okHttpMetaBody))
        .addFormDataPart(FILE_NAME, fileRequestBody.getFileName(), stripLength(okHttpFileBody))
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
