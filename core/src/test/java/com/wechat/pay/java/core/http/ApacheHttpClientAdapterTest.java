package com.wechat.pay.java.core.http;

import static com.wechat.pay.java.core.http.Constant.AUTHORIZATION;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.RESOURCES_DIR;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.model.TestServiceResponse;
import com.wechat.pay.java.core.util.IOUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ApacheHttpClientAdapterTest {

  private static final String APACHE_HTTP_CLIENT_MOCK_DATA_DIR = RESOURCES_DIR + "/http";
  private static final String URL = "https://api.mch.weixin.qq.com/v3/certificates";
  private static final String RESPONSE_JSON_KEY = "request_id";
  private static final String RESPONSE_JSON_VALUE = "123";
  private static final String RESPONSE_JSON =
      "{\"" + RESPONSE_JSON_KEY + "\":\"" + RESPONSE_JSON_VALUE + "\"}";
  private static final String REQUEST_HEADER_KEY = "request-header-key";
  private static final String REQUEST_HEADER_VALUE = "request-header-value";
  private static final String REQUEST_BODY_KEY = "request-body-key";
  private static final String REQUEST_BODY_VALUE = "request-body-value";
  private static final JsonRequestBody JSON_REQUEST_BODY =
      new JsonRequestBody.Builder()
          .body("{\"" + REQUEST_BODY_KEY + "\":\"" + REQUEST_BODY_VALUE + "\"}")
          .build();

  private static final String RESPONSE_HEADER_KEY = "response-header-key";
  private static final String RESPONSE_HEADER_VALUE = "response-header-value";

  private static final String FAKE_AUTHORIZATION = "fake-authorization";
  private static HttpHeaders requestHeaders;

  @BeforeClass
  public static void init() {
    requestHeaders = new HttpHeaders();
    requestHeaders.addHeader(REQUEST_HEADER_KEY, REQUEST_HEADER_VALUE);
  }

  @Test
  public void testExecuteSendGetRequest() {
    Credential executeSendGetCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.GET.name(), httpMethod);
            Assert.assertEquals("", signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator executeSendGetRequestValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor requestInterceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            String executeSendGetMethod = request.getRequestLine().getMethod();
            Assert.assertEquals(executeSendGetMethod, HttpMethod.GET.name());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(FAKE_AUTHORIZATION, getHeaderValue(headers, AUTHORIZATION));
            Assert.assertEquals(
                executeSendGetCredential.getAuthorization(
                    URI.create(URL), executeSendGetMethod, ""),
                getHeaderValue(headers, AUTHORIZATION));
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };
    CloseableHttpClient httpclient =
        HttpClients.custom()
            .addInterceptorFirst(requestInterceptor)
            .addInterceptorFirst(responseInterceptor)
            .build();
    HttpClient executeSendGetHttpClient =
        new ApacheHttpClientBuilder()
            .credential(executeSendGetCredential)
            .validator(executeSendGetRequestValidator)
            .apacheHttpClient(httpclient)
            .build();

    com.wechat.pay.java.core.http.HttpRequest executeSendGetHttpRequest =
        new com.wechat.pay.java.core.http.HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(URL)
            .headers(requestHeaders)
            .build();

    com.wechat.pay.java.core.http.HttpResponse<TestServiceResponse> executeSendGetResponse =
        executeSendGetHttpClient.execute(executeSendGetHttpRequest, TestServiceResponse.class);

    Assert.assertEquals(4, executeSendGetResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendGetResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendGetResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(1, executeSendGetResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendGetResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendGetResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendGetResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendGetResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testExecuteSendPostReqWithJsonBody() throws IOException {
    Credential executeSendPostCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.POST.name(), httpMethod);
            Assert.assertEquals(JSON_REQUEST_BODY.getBody(), signBody);
            return FAKE_AUTHORIZATION;
          }
        };

    Validator executeSendPostValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor interceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            Assert.assertEquals(HttpMethod.POST.name(), request.getRequestLine().getMethod());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(
                executeSendPostCredential.getAuthorization(
                    URI.create(URL),
                    request.getRequestLine().getMethod(),
                    JSON_REQUEST_BODY.getBody()),
                getHeaderValue(headers, AUTHORIZATION));

            Assert.assertTrue(request instanceof HttpEntityEnclosingRequest);
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity reqEntity = entityRequest.getEntity();
            HttpEntity entity =
                new StringEntity(
                    JSON_REQUEST_BODY.getBody(),
                    ContentType.create(JSON_REQUEST_BODY.getContentType()));
            Assert.assertEquals(
                entity.getContentType().getValue(), reqEntity.getContentType().getValue());
            Assert.assertEquals(entity.getContentLength(), reqEntity.getContentLength());
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});
            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };

    CloseableHttpClient httpClient =
        HttpClients.custom()
            .addInterceptorFirst(interceptor)
            .addInterceptorLast(responseInterceptor)
            .build();

    HttpClient executeSendPostHttpClient =
        new ApacheHttpClientBuilder()
            .credential(executeSendPostCredential)
            .validator(executeSendPostValidator)
            .apacheHttpClient(httpClient)
            .build();

    HttpRequest executeSendPostHttpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.POST)
            .url(URL)
            .headers(requestHeaders)
            .body(JSON_REQUEST_BODY)
            .build();

    HttpResponse<TestServiceResponse> executeSendPostResponse =
        executeSendPostHttpClient.execute(executeSendPostHttpRequest, TestServiceResponse.class);
    Assert.assertEquals(4, executeSendPostResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPostResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPostResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(1, executeSendPostResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendPostResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPostResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPostResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendPostResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testExecuteSendPostReqWithFileBody() throws IOException {
    String imageName = "test_image.jpg";
    String imagePath = APACHE_HTTP_CLIENT_MOCK_DATA_DIR + "/" + imageName;
    InputStream inputStream = Files.newInputStream(Paths.get(imagePath));
    FileRequestBody requestBody =
        new FileRequestBody.Builder()
            .meta("meta")
            .fileName(imageName)
            .file(IOUtil.toByteArray(inputStream))
            .build();

    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.RFC6532);
    builder.addTextBody("meta", "meta");
    builder.addBinaryBody(
        "file", requestBody.getFile(), ContentType.create(requestBody.getContentType()), imageName);
    HttpEntity multipartEntity = builder.build();
    Credential executeSendPostWithFileCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.POST.name(), httpMethod);
            System.out.println("signBody=" + signBody);
            Assert.assertEquals("meta", signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator executeSendPostWithFileValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor interceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            Assert.assertEquals(HttpMethod.POST.name(), request.getRequestLine().getMethod());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(
                executeSendPostWithFileCredential.getAuthorization(
                    URI.create(URL), request.getRequestLine().getMethod(), "meta"),
                getHeaderValue(headers, AUTHORIZATION));

            Assert.assertTrue(request instanceof HttpEntityEnclosingRequest);
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            System.out.println("process response: " + response);
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };

    CloseableHttpClient httpClient =
        HttpClients.custom()
            .addInterceptorFirst(interceptor)
            .addInterceptorLast(responseInterceptor)
            .build();

    HttpClient executeSendPostWithFileHttpClient =
        new ApacheHttpClientBuilder()
            .credential(executeSendPostWithFileCredential)
            .validator(executeSendPostWithFileValidator)
            .apacheHttpClient(httpClient)
            .build();

    HttpResponse<TestServiceResponse> executeSendPostWithFileResponse =
        executeSendPostWithFileHttpClient.post(
            requestHeaders, URL, requestBody, TestServiceResponse.class);

    Assert.assertEquals(
        4, executeSendPostWithFileResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPostWithFileResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPostWithFileResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(
        HttpMethod.POST, executeSendPostWithFileResponse.getRequest().getHttpMethod());
    Assert.assertEquals(requestBody, executeSendPostWithFileResponse.getRequest().getBody());
    Assert.assertEquals(URL, executeSendPostWithFileResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, executeSendPostWithFileResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE,
        executeSendPostWithFileResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPostWithFileResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPostWithFileResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendPostWithFileResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testExecuteSendPutRequest() {
    Credential executeSendPutCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.PUT.name(), httpMethod);
            Assert.assertEquals(JSON_REQUEST_BODY.getBody(), signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator executeSendPutValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor interceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            Assert.assertEquals(HttpMethod.PUT.name(), request.getRequestLine().getMethod());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(
                executeSendPutCredential.getAuthorization(
                    URI.create(URL),
                    request.getRequestLine().getMethod(),
                    JSON_REQUEST_BODY.getBody()),
                getHeaderValue(headers, AUTHORIZATION));

            Assert.assertTrue(request instanceof HttpEntityEnclosingRequest);
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity reqEntity = entityRequest.getEntity();
            HttpEntity entity =
                new StringEntity(
                    JSON_REQUEST_BODY.getBody(),
                    ContentType.create(JSON_REQUEST_BODY.getContentType()));
            Assert.assertEquals(entity.getContentLength(), reqEntity.getContentLength());
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };

    CloseableHttpClient httpClient =
        HttpClients.custom()
            .addInterceptorFirst(interceptor)
            .addInterceptorLast(responseInterceptor)
            .build();

    HttpClient executeSendPutHttpClient =
        new ApacheHttpClientBuilder()
            .credential(executeSendPutCredential)
            .validator(executeSendPutValidator)
            .apacheHttpClient(httpClient)
            .build();

    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.PUT)
            .url(URL)
            .headers(requestHeaders)
            .body(JSON_REQUEST_BODY)
            .build();

    HttpResponse<TestServiceResponse> executeSendPutResponse =
        executeSendPutHttpClient.execute(httpRequest, TestServiceResponse.class);

    Assert.assertEquals(4, executeSendPutResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPutResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPutResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.PUT, executeSendPutResponse.getRequest().getHttpMethod());
    Assert.assertEquals(URL, executeSendPutResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, executeSendPutResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendPutResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPutResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPutResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendPutResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testExecuteSendPatchRequest() {
    Credential executeSendPatchCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.PATCH.name(), httpMethod);
            Assert.assertEquals(JSON_REQUEST_BODY.getBody(), signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator executeSendPatchValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor interceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            Assert.assertEquals(HttpMethod.PATCH.name(), request.getRequestLine().getMethod());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(
                executeSendPatchCredential.getAuthorization(
                    URI.create(URL),
                    request.getRequestLine().getMethod(),
                    JSON_REQUEST_BODY.getBody()),
                getHeaderValue(headers, AUTHORIZATION));

            Assert.assertTrue(request instanceof HttpEntityEnclosingRequest);
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity reqEntity = entityRequest.getEntity();
            HttpEntity entity =
                new StringEntity(
                    JSON_REQUEST_BODY.getBody(),
                    ContentType.create(JSON_REQUEST_BODY.getContentType()));
            Assert.assertEquals(entity.getContentLength(), reqEntity.getContentLength());
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };

    CloseableHttpClient httpClient =
        HttpClients.custom()
            .addInterceptorFirst(interceptor)
            .addInterceptorLast(responseInterceptor)
            .build();

    HttpClient executeSendPutHttpClient =
        new ApacheHttpClientBuilder()
            .credential(executeSendPatchCredential)
            .validator(executeSendPatchValidator)
            .apacheHttpClient(httpClient)
            .build();

    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.PATCH)
            .url(URL)
            .headers(requestHeaders)
            .body(JSON_REQUEST_BODY)
            .build();

    HttpResponse<TestServiceResponse> executeSendPutResponse =
        executeSendPutHttpClient.execute(httpRequest, TestServiceResponse.class);

    Assert.assertEquals(4, executeSendPutResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPutResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPutResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.PATCH, executeSendPutResponse.getRequest().getHttpMethod());
    Assert.assertEquals(URL, executeSendPutResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, executeSendPutResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendPutResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPutResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPutResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendPutResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testExecuteSendDeleteRequest() throws IOException {
    Credential executeSendDeleteCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.DELETE.name(), httpMethod);
            Assert.assertEquals(JSON_REQUEST_BODY.getBody(), signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator executeSendDeleteValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor interceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            Assert.assertEquals(HttpMethod.DELETE.name(), request.getRequestLine().getMethod());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(
                executeSendDeleteCredential.getAuthorization(
                    URI.create(URL),
                    request.getRequestLine().getMethod(),
                    JSON_REQUEST_BODY.getBody()),
                getHeaderValue(headers, AUTHORIZATION));
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };

    CloseableHttpClient httpClient =
        HttpClients.custom()
            .addInterceptorFirst(interceptor)
            .addInterceptorLast(responseInterceptor)
            .build();

    HttpClient executeSendPutHttpClient =
        new ApacheHttpClientBuilder()
            .credential(executeSendDeleteCredential)
            .validator(executeSendDeleteValidator)
            .apacheHttpClient(httpClient)
            .build();

    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.DELETE)
            .url(URL)
            .headers(requestHeaders)
            .body(JSON_REQUEST_BODY)
            .build();

    HttpResponse<TestServiceResponse> executeSendPutResponse =
        executeSendPutHttpClient.execute(httpRequest, TestServiceResponse.class);

    Assert.assertEquals(4, executeSendPutResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPutResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPutResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.DELETE, executeSendPutResponse.getRequest().getHttpMethod());
    Assert.assertEquals(URL, executeSendPutResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, executeSendPutResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendPutResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPutResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPutResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendPutResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testGet() {
    Credential getCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.GET.name(), httpMethod);
            Assert.assertEquals("", signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator getHttpValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor requestInterceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            String getMethod = request.getRequestLine().getMethod();
            Assert.assertEquals(getMethod, HttpMethod.GET.name());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(FAKE_AUTHORIZATION, getHeaderValue(headers, AUTHORIZATION));
            Assert.assertEquals(
                getCredential.getAuthorization(URI.create(URL), getMethod, ""),
                getHeaderValue(headers, AUTHORIZATION));
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };
    CloseableHttpClient httpclient =
        HttpClients.custom()
            .addInterceptorFirst(requestInterceptor)
            .addInterceptorFirst(responseInterceptor)
            .build();

    HttpClient getHttpClient =
        new ApacheHttpClientBuilder()
            .credential(getCredential)
            .validator(getHttpValidator)
            .apacheHttpClient(httpclient)
            .build();

    com.wechat.pay.java.core.http.HttpResponse<TestServiceResponse> executeSendGetResponse =
        getHttpClient.get(requestHeaders, URL, TestServiceResponse.class);

    Assert.assertEquals(4, executeSendGetResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendGetResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendGetResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(1, executeSendGetResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendGetResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendGetResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendGetResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendGetResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testPost() throws IOException {
    Credential postCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.POST.name(), httpMethod);
            Assert.assertEquals(JSON_REQUEST_BODY.getBody(), signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator postValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor interceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            Assert.assertEquals(HttpMethod.POST.name(), request.getRequestLine().getMethod());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(
                postCredential.getAuthorization(
                    URI.create(URL),
                    request.getRequestLine().getMethod(),
                    JSON_REQUEST_BODY.getBody()),
                getHeaderValue(headers, AUTHORIZATION));

            Assert.assertTrue(request instanceof HttpEntityEnclosingRequest);
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity reqEntity = entityRequest.getEntity();
            HttpEntity entity =
                new StringEntity(
                    JSON_REQUEST_BODY.getBody(),
                    ContentType.create(JSON_REQUEST_BODY.getContentType()));
            Assert.assertEquals(
                entity.getContentType().getValue(), reqEntity.getContentType().getValue());
            Assert.assertEquals(entity.getContentLength(), reqEntity.getContentLength());
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };

    CloseableHttpClient httpClient =
        HttpClients.custom()
            .addInterceptorFirst(interceptor)
            .addInterceptorLast(responseInterceptor)
            .build();

    HttpClient postHttpClient =
        new ApacheHttpClientBuilder()
            .credential(postCredential)
            .validator(postValidator)
            .apacheHttpClient(httpClient)
            .build();

    HttpResponse<TestServiceResponse> executeSendPostResponse =
        postHttpClient.post(requestHeaders, URL, JSON_REQUEST_BODY, TestServiceResponse.class);
    Assert.assertEquals(4, executeSendPostResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPostResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPostResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(1, executeSendPostResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendPostResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPostResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPostResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendPostResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testPatch() {
    Credential patchCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.PATCH.name(), httpMethod);
            Assert.assertEquals(JSON_REQUEST_BODY.getBody(), signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator patchValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor interceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            Assert.assertEquals(HttpMethod.PATCH.name(), request.getRequestLine().getMethod());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(
                patchCredential.getAuthorization(
                    URI.create(URL),
                    request.getRequestLine().getMethod(),
                    JSON_REQUEST_BODY.getBody()),
                getHeaderValue(headers, AUTHORIZATION));

            Assert.assertTrue(request instanceof HttpEntityEnclosingRequest);
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity reqEntity = entityRequest.getEntity();
            HttpEntity entity =
                new StringEntity(
                    JSON_REQUEST_BODY.getBody(),
                    ContentType.create(JSON_REQUEST_BODY.getContentType()));
            Assert.assertEquals(entity.getContentLength(), reqEntity.getContentLength());
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };

    CloseableHttpClient httpClient =
        HttpClients.custom()
            .addInterceptorFirst(interceptor)
            .addInterceptorLast(responseInterceptor)
            .build();

    HttpClient patchHttpClient =
        new ApacheHttpClientBuilder()
            .credential(patchCredential)
            .validator(patchValidator)
            .apacheHttpClient(httpClient)
            .build();

    HttpResponse<TestServiceResponse> executeSendPutResponse =
        patchHttpClient.patch(requestHeaders, URL, JSON_REQUEST_BODY, TestServiceResponse.class);

    Assert.assertEquals(4, executeSendPutResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPutResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPutResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.PATCH, executeSendPutResponse.getRequest().getHttpMethod());
    Assert.assertEquals(URL, executeSendPutResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, executeSendPutResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendPutResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPutResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPutResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendPutResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testPut() {
    Credential putCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.PUT.name(), httpMethod);
            Assert.assertEquals(JSON_REQUEST_BODY.getBody(), signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator putValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor interceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            Assert.assertEquals(HttpMethod.PUT.name(), request.getRequestLine().getMethod());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(
                putCredential.getAuthorization(
                    URI.create(URL),
                    request.getRequestLine().getMethod(),
                    JSON_REQUEST_BODY.getBody()),
                getHeaderValue(headers, AUTHORIZATION));

            Assert.assertTrue(request instanceof HttpEntityEnclosingRequest);
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity reqEntity = entityRequest.getEntity();
            HttpEntity entity =
                new StringEntity(
                    JSON_REQUEST_BODY.getBody(),
                    ContentType.create(JSON_REQUEST_BODY.getContentType()));
            Assert.assertEquals(entity.getContentLength(), reqEntity.getContentLength());
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };

    CloseableHttpClient httpClient =
        HttpClients.custom()
            .addInterceptorFirst(interceptor)
            .addInterceptorLast(responseInterceptor)
            .build();

    HttpClient putHttpClient =
        new ApacheHttpClientBuilder()
            .credential(putCredential)
            .validator(putValidator)
            .apacheHttpClient(httpClient)
            .build();

    HttpResponse<TestServiceResponse> executeSendPutResponse =
        putHttpClient.put(requestHeaders, URL, JSON_REQUEST_BODY, TestServiceResponse.class);

    Assert.assertEquals(4, executeSendPutResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPutResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPutResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.PUT, executeSendPutResponse.getRequest().getHttpMethod());
    Assert.assertEquals(URL, executeSendPutResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, executeSendPutResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendPutResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPutResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPutResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendPutResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testDelete() {
    Credential executeSendDeleteCredential =
        new Credential() {
          @Override
          public String getSchema() {
            return "fake-schema";
          }

          @Override
          public String getMerchantId() {
            return MERCHANT_ID;
          }

          @Override
          public String getAuthorization(URI uri, String httpMethod, String signBody) {
            Assert.assertEquals(URI.create(URL), uri);
            Assert.assertEquals(HttpMethod.DELETE.name(), httpMethod);
            Assert.assertEquals(JSON_REQUEST_BODY.getBody(), signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator executeSendDeleteValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            Assert.assertNotNull(responseHeaders);
            Assert.assertEquals(1, responseHeaders.getHeaders().size());
            Assert.assertEquals(
                RESPONSE_HEADER_VALUE, responseHeaders.getHeader(RESPONSE_HEADER_KEY));
            Assert.assertEquals(RESPONSE_JSON, body);
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    HttpRequestInterceptor interceptor =
        new HttpRequestInterceptor() {
          @Override
          public void process(org.apache.http.HttpRequest request, HttpContext context)
              throws HttpException, IOException {
            Assert.assertEquals(HttpMethod.DELETE.name(), request.getRequestLine().getMethod());
            String host = context.getAttribute("http.target_host").toString();
            Assert.assertEquals(URL, host + request.getRequestLine().getUri());
            Header[] headers = request.getAllHeaders();
            Assert.assertEquals(4, headers.length);
            Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaderValue(headers, REQUEST_HEADER_KEY));
            Assert.assertEquals(
                executeSendDeleteCredential.getAuthorization(
                    URI.create(URL),
                    request.getRequestLine().getMethod(),
                    JSON_REQUEST_BODY.getBody()),
                getHeaderValue(headers, AUTHORIZATION));
          }

          private String getHeaderValue(Header[] headers, String name) {
            for (Header header : headers) {
              if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
              }
            }
            return null;
          }
        };

    HttpResponseInterceptor responseInterceptor =
        new HttpResponseInterceptor() {
          @Override
          public void process(org.apache.http.HttpResponse response, HttpContext context)
              throws HttpException, IOException {
            Header header = new BasicHeader(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE);
            response.setHeaders(new Header[] {header});

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int statusCode = HttpStatus.SC_OK;
            String reasonPhrase = "OK";
            BasicStatusLine statusLine =
                new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);
            response.setStatusLine(statusLine);
            response.setEntity(new StringEntity(RESPONSE_JSON, ContentType.APPLICATION_JSON));
          }
        };

    CloseableHttpClient httpClient =
        HttpClients.custom()
            .addInterceptorFirst(interceptor)
            .addInterceptorLast(responseInterceptor)
            .build();

    HttpClient deleteHttpClient =
        new ApacheHttpClientBuilder()
            .credential(executeSendDeleteCredential)
            .validator(executeSendDeleteValidator)
            .apacheHttpClient(httpClient)
            .build();

    HttpResponse<TestServiceResponse> executeSendPutResponse =
        deleteHttpClient.delete(requestHeaders, URL, TestServiceResponse.class);

    Assert.assertEquals(4, executeSendPutResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPutResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPutResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.DELETE, executeSendPutResponse.getRequest().getHttpMethod());
    Assert.assertEquals(URL, executeSendPutResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, executeSendPutResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendPutResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPutResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPutResponse.getBody()).getBody());
    Assert.assertEquals(
        RESPONSE_JSON_VALUE, executeSendPutResponse.getServiceResponse().getRequestId());
  }
}
