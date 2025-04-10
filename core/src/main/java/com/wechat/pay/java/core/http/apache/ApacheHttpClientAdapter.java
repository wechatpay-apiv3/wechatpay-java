package com.wechat.pay.java.core.http.apache;

import static java.util.Objects.requireNonNull;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

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
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApacheHttpClientAdapter extends AbstractHttpClient {

  private static final Logger logger = LoggerFactory.getLogger(ApacheHttpClientAdapter.class);
  private static final String META_NAME = "meta";
  private static final String FILE_NAME = "file";

  private final CloseableHttpClient apacheHttpClient;

  public ApacheHttpClientAdapter(
      Credential credential, Validator validator, CloseableHttpClient client) {
    super(credential, validator);
    this.apacheHttpClient = requireNonNull(client);
  }

  @Override
  protected String getHttpClientInfo() {
    return "apachehttp/" + apacheHttpClient.getClass().getPackage().getImplementationVersion();
  }

  @Override
  public OriginalResponse innerExecute(HttpRequest wechatPayRequest) {
    try {
      CloseableHttpResponse apacheHttpResponse =
          apacheHttpClient.execute(buildApacheHttpRequest(wechatPayRequest));
      return assembleOriginalResponse(wechatPayRequest, apacheHttpResponse);
    } catch (IOException e) {
      throw new HttpException(wechatPayRequest, e);
    }
  }

  private org.apache.http.client.methods.HttpUriRequest buildApacheHttpRequest(
      HttpRequest wechatPayRequest) {
    String url = wechatPayRequest.getUrl().toString();
    org.apache.http.client.methods.HttpUriRequest apacheHttpRequest;

    switch (wechatPayRequest.getHttpMethod().name()) {
      case "GET":
        apacheHttpRequest = new HttpGet(url);
        break;
      case "POST":
        apacheHttpRequest = new HttpPost(url);
        ((HttpPost) apacheHttpRequest).setEntity(buildApacheHttpEntity(wechatPayRequest.getBody()));
        break;
      case "PUT":
        apacheHttpRequest = new HttpPut(url);
        ((HttpPut) apacheHttpRequest).setEntity(buildApacheHttpEntity(wechatPayRequest.getBody()));
        break;
      case "PATCH":
        apacheHttpRequest = new HttpPatch(url);
        ((HttpPatch) apacheHttpRequest)
            .setEntity(buildApacheHttpEntity(wechatPayRequest.getBody()));
        break;
      case "DELETE":
        apacheHttpRequest = new HttpDelete(url);
        break;
      default:
        throw new IllegalArgumentException(
            "Unsupported HTTP method: " + wechatPayRequest.getHttpMethod().name());
    }
    Map<String, String> headers = wechatPayRequest.getHeaders().getHeaders();
    headers.forEach(apacheHttpRequest::addHeader);
    return apacheHttpRequest;
  }

  private HttpEntity buildApacheHttpEntity(
      com.wechat.pay.java.core.http.RequestBody wechatPayRequestBody) {
    // 处理空请求体的情况
    if (wechatPayRequestBody == null) {
      return new StringEntity("", "");
    }
    // 指定ContentType参数为UTF-8， fix issues #352
    if (wechatPayRequestBody instanceof JsonRequestBody) {
      return new StringEntity(
          ((JsonRequestBody) wechatPayRequestBody).getBody(),
          ContentType.create(wechatPayRequestBody.getContentType(),"UTF-8")); 
    }
    if (wechatPayRequestBody instanceof FileRequestBody) {
      FileRequestBody fileRequestBody = (FileRequestBody) wechatPayRequestBody;
      MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
      entityBuilder.setMode(HttpMultipartMode.RFC6532);
      entityBuilder.addTextBody(META_NAME, fileRequestBody.getMeta(), APPLICATION_JSON);
      entityBuilder.addBinaryBody(
          FILE_NAME,
          fileRequestBody.getFile(),
          ContentType.create(fileRequestBody.getContentType()),
          fileRequestBody.getFileName());
      return entityBuilder.build();
    }
    logger.error(
        "When an http request is sent and the apache request body is constructed, the requestBody"
            + " parameter"
            + " type cannot be found,requestBody class name[{}]",
        wechatPayRequestBody.getClass().getName());
    return null;
  }

  private OriginalResponse assembleOriginalResponse(
      HttpRequest wechatPayRequest, CloseableHttpResponse apacheHttpResponse) throws IOException {
    Map<String, String> responseHeaders = assembleResponseHeader(apacheHttpResponse);
    HttpEntity entity = apacheHttpResponse.getEntity();
    try {
      String responseBody = entity != null ? EntityUtils.toString(entity) : null;
      return new OriginalResponse.Builder()
          .request(wechatPayRequest)
          .headers(responseHeaders)
          .statusCode(apacheHttpResponse.getStatusLine().getStatusCode())
          .contentType(entity != null ? entity.getContentType().getValue() : null)
          .body(responseBody)
          .build();
    } catch (IOException e) {
      throw new MalformedMessageException(
          String.format(
              "Assemble OriginalResponse,get responseBody failed.%nHttpRequest[%s]",
              wechatPayRequest));
    }
  }

  private Map<String, String> assembleResponseHeader(CloseableHttpResponse apacheHttpResponse) {
    Map<String, String> responseHeaders = new ConcurrentHashMap<>();
    Header[] headers = apacheHttpResponse.getAllHeaders();
    for (Header header : headers) {
      responseHeaders.put(header.getName(), header.getValue());
    }
    return responseHeaders;
  }

  @Override
  protected InputStream innerDownload(HttpRequest httpRequest) {
    try {
      CloseableHttpResponse apacheHttpResponse =
          apacheHttpClient.execute(buildApacheHttpRequest(httpRequest));
      if (isInvalidHttpCode(apacheHttpResponse.getStatusLine().getStatusCode())) {
        throw new ServiceException(
            httpRequest, apacheHttpResponse.getStatusLine().getStatusCode(), "");
      }
      InputStream responseBodyStream = null;
      if (apacheHttpResponse.getEntity() != null) {
        responseBodyStream = apacheHttpResponse.getEntity().getContent();
      }
      return responseBodyStream;
    } catch (IOException e) {
      throw new HttpException(httpRequest, e);
    }
  }
}
