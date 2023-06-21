package com.wechat.pay.java.core.http;

import static com.wechat.pay.java.core.http.Constant.AUTHORIZATION;
import static com.wechat.pay.java.core.http.Constant.OS;
import static com.wechat.pay.java.core.http.Constant.USER_AGENT;
import static com.wechat.pay.java.core.http.Constant.USER_AGENT_FORMAT;
import static com.wechat.pay.java.core.http.Constant.VERSION;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static com.wechat.pay.java.core.model.TestConfig.RESOURCES_DIR;
import static java.net.HttpURLConnection.HTTP_OK;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import com.wechat.pay.java.core.model.TestServiceResponse;
import com.wechat.pay.java.core.util.IOUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import okio.GzipSource;
import okio.Okio;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OkHttpClientAdapterTest {

  private static final String OK_HTTP_CLIENT_MOCK_DATA_DIR = RESOURCES_DIR + "/http";
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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            executeSendGetCredential.getClass().getSimpleName(),
            executeSendGetRequestValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());
    OkHttpClient.Builder executeSendGetOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String executeSendGetMethod = chain.request().method();
                  Headers executeSendGetHeaders = chain.request().headers();

                  Assert.assertEquals(executeSendGetMethod, HttpMethod.GET.name());
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, executeSendGetHeaders.size());
                  Assert.assertEquals(
                      REQUEST_HEADER_VALUE, executeSendGetHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(userAgent, executeSendGetHeaders.get(USER_AGENT));
                  Assert.assertEquals(FAKE_AUTHORIZATION, executeSendGetHeaders.get(AUTHORIZATION));
                  Assert.assertEquals(
                      executeSendGetCredential.getAuthorization(
                          URI.create(URL), executeSendGetMethod, ""),
                      executeSendGetHeaders.get(AUTHORIZATION));
                  Assert.assertNull(chain.request().body());

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });
    HttpClient executeSendGetHttpClient =
        new DefaultHttpClientBuilder()
            .credential(executeSendGetCredential)
            .validator(executeSendGetRequestValidator)
            .okHttpClient(executeSendGetOkHttpClientBuilder.build())
            .build();

    HttpRequest executeSendGetHttpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(URL)
            .headers(requestHeaders)
            .build();

    HttpResponse<TestServiceResponse> executeSendGetResponse =
        executeSendGetHttpClient.execute(executeSendGetHttpRequest, TestServiceResponse.class);

    Assert.assertEquals(3, executeSendGetResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendGetResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        userAgent, executeSendGetResponse.getRequest().getHeaders().getHeader(USER_AGENT));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendGetResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(1, executeSendGetResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendGetResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendGetResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendGetResponse.getBody()).getBody());
    Assert.assertEquals("123", executeSendGetResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testExecuteSendPostReqWithJsonBody() {
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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            executeSendPostCredential.getClass().getSimpleName(),
            executeSendPostValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());
    OkHttpClient.Builder executeSendPostWithJsonOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String executeSendPostWithJsonMethod = chain.request().method();
                  Headers executeSendPostWithJsonHeaders = chain.request().headers();
                  RequestBody requestBody = chain.request().body();
                  RequestBody createRequestBody =
                      RequestBody.create(
                          JSON_REQUEST_BODY.getBody(),
                          MediaType.parse(JSON_REQUEST_BODY.getContentType()));

                  Assert.assertEquals(executeSendPostWithJsonMethod, HttpMethod.POST.name());
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, executeSendPostWithJsonHeaders.size());
                  Assert.assertEquals(userAgent, executeSendPostWithJsonHeaders.get(USER_AGENT));
                  Assert.assertEquals(
                      executeSendPostCredential.getAuthorization(
                          URI.create(URL),
                          executeSendPostWithJsonMethod,
                          JSON_REQUEST_BODY.getBody()),
                      executeSendPostWithJsonHeaders.get(AUTHORIZATION));
                  Assert.assertEquals(
                      REQUEST_HEADER_VALUE, executeSendPostWithJsonHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(createRequestBody.contentType(), requestBody.contentType());
                  Assert.assertEquals(
                      createRequestBody.contentLength(), requestBody.contentLength());

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient executeSendPostHttpClient =
        new DefaultHttpClientBuilder()
            .credential(executeSendPostCredential)
            .validator(executeSendPostValidator)
            .okHttpClient(executeSendPostWithJsonOkHttpClientBuilder.build())
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

    Assert.assertEquals(3, executeSendPostResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPostResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        userAgent, executeSendPostResponse.getRequest().getHeaders().getHeader(USER_AGENT));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPostResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(1, executeSendPostResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendPostResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPostResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPostResponse.getBody()).getBody());
    Assert.assertEquals("123", executeSendPostResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testExecuteSendPostReqWithFileBody() throws IOException {
    String imageName = "test_image.jpg";
    String imagePath = OK_HTTP_CLIENT_MOCK_DATA_DIR + "/" + imageName;
    InputStream inputStream = Files.newInputStream(Paths.get(imagePath));
    FileRequestBody requestBody =
        new FileRequestBody.Builder()
            .meta("meta")
            .fileName(imageName)
            .file(IOUtil.toByteArray(inputStream))
            .build();
    okhttp3.RequestBody fileBody =
        okhttp3.RequestBody.create(
            requestBody.getFile(), okhttp3.MediaType.parse(requestBody.getContentType()));
    okhttp3.RequestBody multiPartBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("meta", "meta")
            .addFormDataPart("file", imageName, fileBody)
            .build();

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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            executeSendPostWithFileCredential.getClass().getSimpleName(),
            executeSendPostWithFileValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());
    OkHttpClient.Builder executeSendPostWithFileOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String executeSendPostWithFileMethod = chain.request().method();

                  Headers executeSendPostWithFileHeaders = chain.request().headers();
                  okhttp3.RequestBody okHttpRequestBody = chain.request().body();

                  Assert.assertNotNull(requestBody);
                  Assert.assertEquals(
                      multiPartBody.contentLength(), okHttpRequestBody.contentLength());
                  Assert.assertEquals(executeSendPostWithFileMethod, HttpMethod.POST.name());
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, executeSendPostWithFileHeaders.size());
                  Assert.assertEquals(
                      REQUEST_HEADER_VALUE, executeSendPostWithFileHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(userAgent, executeSendPostWithFileHeaders.get(USER_AGENT));
                  Assert.assertEquals(
                      executeSendPostWithFileCredential.getAuthorization(
                          URI.create(URL), executeSendPostWithFileMethod, "meta"),
                      executeSendPostWithFileHeaders.get(AUTHORIZATION));

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient executeSendPostWithFileHttpClient =
        new DefaultHttpClientBuilder()
            .credential(executeSendPostWithFileCredential)
            .validator(executeSendPostWithFileValidator)
            .okHttpClient(executeSendPostWithFileOkHttpClientBuilder.build())
            .build();

    HttpResponse<TestServiceResponse> executeSendPostWithFileResponse =
        executeSendPostWithFileHttpClient.post(
            requestHeaders, URL, requestBody, TestServiceResponse.class);

    Assert.assertEquals(
        3, executeSendPostWithFileResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPostWithFileResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        userAgent, executeSendPostWithFileResponse.getRequest().getHeaders().getHeader(USER_AGENT));
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
    Assert.assertEquals("123", executeSendPostWithFileResponse.getServiceResponse().getRequestId());
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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            executeSendPutCredential.getClass().getSimpleName(),
            executeSendPutValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());

    OkHttpClient.Builder executeSendPutOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String executeSendPutMethod = chain.request().method();

                  Headers executeSendPutHeaders = chain.request().headers();
                  RequestBody requestBody = chain.request().body();
                  RequestBody createRequestBody =
                      RequestBody.create(
                          JSON_REQUEST_BODY.getBody(),
                          MediaType.parse(JSON_REQUEST_BODY.getContentType()));

                  Assert.assertEquals(executeSendPutMethod, HttpMethod.PUT.name());
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, executeSendPutHeaders.size());
                  Assert.assertEquals(userAgent, executeSendPutHeaders.get(USER_AGENT));
                  Assert.assertEquals(
                      executeSendPutCredential.getAuthorization(
                          URI.create(URL), executeSendPutMethod, JSON_REQUEST_BODY.getBody()),
                      executeSendPutHeaders.get(AUTHORIZATION));
                  Assert.assertEquals(
                      REQUEST_HEADER_VALUE, executeSendPutHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(createRequestBody.contentType(), requestBody.contentType());
                  Assert.assertEquals(
                      createRequestBody.contentLength(), requestBody.contentLength());

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient executeSendPutHttpClient =
        new DefaultHttpClientBuilder()
            .credential(executeSendPutCredential)
            .validator(executeSendPutValidator)
            .okHttpClient(executeSendPutOkHttpClientBuilder.build())
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

    Assert.assertEquals(3, executeSendPutResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPutResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        userAgent, executeSendPutResponse.getRequest().getHeaders().getHeader(USER_AGENT));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPutResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(1, executeSendPutResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, executeSendPutResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPutResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPutResponse.getBody()).getBody());
    Assert.assertEquals("123", executeSendPutResponse.getServiceResponse().getRequestId());
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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            executeSendPatchCredential.getClass().getSimpleName(),
            executeSendPatchValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());
    OkHttpClient.Builder executeSendPatchOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String executeSendPatchMethod = chain.request().method();

                  Headers executeSendPatchHeaders = chain.request().headers();
                  RequestBody requestBody = chain.request().body();
                  RequestBody createRequestBody =
                      RequestBody.create(
                          JSON_REQUEST_BODY.getBody(),
                          MediaType.parse(JSON_REQUEST_BODY.getContentType()));

                  Assert.assertEquals(executeSendPatchMethod, HttpMethod.PATCH.name());
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, executeSendPatchHeaders.size());
                  Assert.assertEquals(
                      REQUEST_HEADER_VALUE, executeSendPatchHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(userAgent, executeSendPatchHeaders.get(USER_AGENT));
                  Assert.assertEquals(
                      executeSendPatchCredential.getAuthorization(
                          URI.create(URL), executeSendPatchMethod, JSON_REQUEST_BODY.getBody()),
                      executeSendPatchHeaders.get(AUTHORIZATION));
                  Assert.assertEquals(
                      REQUEST_HEADER_VALUE, executeSendPatchHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(createRequestBody.contentType(), requestBody.contentType());
                  Assert.assertEquals(
                      createRequestBody.contentLength(), requestBody.contentLength());

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient executeSendPatchHttpClient =
        new DefaultHttpClientBuilder()
            .credential(executeSendPatchCredential)
            .validator(executeSendPatchValidator)
            .okHttpClient(executeSendPatchOkHttpClientBuilder.build())
            .build();

    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.PATCH)
            .url(URL)
            .headers(requestHeaders)
            .body(JSON_REQUEST_BODY)
            .build();

    HttpResponse<TestServiceResponse> executeSendPatchResponse =
        executeSendPatchHttpClient.execute(httpRequest, TestServiceResponse.class);

    Assert.assertEquals(3, executeSendPatchResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendPatchResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        userAgent, executeSendPatchResponse.getRequest().getHeaders().getHeader(USER_AGENT));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendPatchResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(1, executeSendPatchResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE,
        executeSendPatchResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendPatchResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendPatchResponse.getBody()).getBody());
    Assert.assertEquals("123", executeSendPatchResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testExecuteSendDeleteRequest() {

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
            Assert.assertEquals("", signBody);
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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            executeSendDeleteCredential.getClass().getSimpleName(),
            executeSendDeleteValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());
    OkHttpClient.Builder executeSendDeleteOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String executeSendDeleteMethod = chain.request().method();

                  Headers executeSendDeleteHeaders = chain.request().headers();

                  Assert.assertEquals(HttpMethod.DELETE.name(), executeSendDeleteMethod);
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, executeSendDeleteHeaders.size());
                  Assert.assertEquals(
                      REQUEST_HEADER_VALUE, executeSendDeleteHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(userAgent, executeSendDeleteHeaders.get(USER_AGENT));
                  Assert.assertEquals(
                      executeSendDeleteCredential.getAuthorization(
                          URI.create(URL), executeSendDeleteMethod, ""),
                      executeSendDeleteHeaders.get(AUTHORIZATION));
                  Assert.assertEquals(
                      REQUEST_HEADER_VALUE, executeSendDeleteHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(0, chain.request().body().contentLength());

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient executeSendDeleteHttpClient =
        new DefaultHttpClientBuilder()
            .credential(executeSendDeleteCredential)
            .validator(executeSendDeleteValidator)
            .okHttpClient(executeSendDeleteOkHttpClientBuilder.build())
            .build();

    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.DELETE)
            .url(URL)
            .headers(requestHeaders)
            .build();

    HttpResponse<TestServiceResponse> executeSendDeleteResponse =
        executeSendDeleteHttpClient.execute(httpRequest, TestServiceResponse.class);

    Assert.assertEquals(3, executeSendDeleteResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        executeSendDeleteResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(
        userAgent, executeSendDeleteResponse.getRequest().getHeaders().getHeader(USER_AGENT));
    Assert.assertEquals(
        FAKE_AUTHORIZATION,
        executeSendDeleteResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(1, executeSendDeleteResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE,
        executeSendDeleteResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(executeSendDeleteResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(
        RESPONSE_JSON, ((JsonResponseBody) executeSendDeleteResponse.getBody()).getBody());
    Assert.assertEquals("123", executeSendDeleteResponse.getServiceResponse().getRequestId());
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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            getCredential.getClass().getSimpleName(),
            getHttpValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());
    OkHttpClient.Builder getOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String getMethod = chain.request().method();

                  Headers getHeaders = chain.request().headers();

                  Assert.assertEquals(getMethod, HttpMethod.GET.name());
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, getHeaders.size());
                  Assert.assertEquals(REQUEST_HEADER_VALUE, getHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(userAgent, getHeaders.get(USER_AGENT));
                  Assert.assertEquals(
                      getCredential.getAuthorization(URI.create(URL), getMethod, ""),
                      getHeaders.get(AUTHORIZATION));
                  Assert.assertNull(chain.request().body());

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient getHttpClient =
        new DefaultHttpClientBuilder()
            .credential(getCredential)
            .validator(getHttpValidator)
            .okHttpClient(getOkHttpClientBuilder.build())
            .build();

    HttpResponse<TestServiceResponse> getResponse =
        getHttpClient.get(requestHeaders, URL, TestServiceResponse.class);
    Assert.assertNotNull(getResponse.getRequest().getHeaders());
    Assert.assertEquals(3, getResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(userAgent, getResponse.getRequest().getHeaders().getHeader(USER_AGENT));
    Assert.assertEquals(
        FAKE_AUTHORIZATION, getResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.GET, getResponse.getRequest().getHttpMethod());
    Assert.assertNull(getResponse.getRequest().getBody());
    Assert.assertEquals(URL, getResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, getResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, getResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(getResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(RESPONSE_JSON, ((JsonResponseBody) getResponse.getBody()).getBody());
    Assert.assertEquals(RESPONSE_JSON_VALUE, getResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testPost() {

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
    Validator postHttpValidator =
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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            postCredential.getClass().getSimpleName(),
            postHttpValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());
    OkHttpClient.Builder postOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String postMethod = chain.request().method();

                  Headers postHeaders = chain.request().headers();
                  RequestBody requestBody = chain.request().body();
                  RequestBody createRequestBody =
                      RequestBody.create(
                          JSON_REQUEST_BODY.getBody(),
                          MediaType.parse(JSON_REQUEST_BODY.getContentType()));

                  Assert.assertEquals(postMethod, HttpMethod.POST.name());
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, postHeaders.size());
                  Assert.assertEquals(REQUEST_HEADER_VALUE, postHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(userAgent, postHeaders.get(USER_AGENT));
                  Assert.assertEquals(
                      postCredential.getAuthorization(
                          URI.create(URL), postMethod, JSON_REQUEST_BODY.getBody()),
                      postHeaders.get(AUTHORIZATION));
                  Assert.assertEquals(createRequestBody.contentType(), requestBody.contentType());
                  Assert.assertEquals(
                      createRequestBody.contentLength(), requestBody.contentLength());

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient postHttpClient =
        new DefaultHttpClientBuilder()
            .credential(postCredential)
            .validator(postHttpValidator)
            .okHttpClient(postOkHttpClientBuilder.build())
            .build();
    HttpResponse<TestServiceResponse> postResponse =
        postHttpClient.post(requestHeaders, URL, JSON_REQUEST_BODY, TestServiceResponse.class);

    Assert.assertEquals(3, postResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE, postResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(userAgent, postResponse.getRequest().getHeaders().getHeader(USER_AGENT));
    Assert.assertEquals(
        FAKE_AUTHORIZATION, postResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.POST, postResponse.getRequest().getHttpMethod());
    Assert.assertEquals(JSON_REQUEST_BODY, postResponse.getRequest().getBody());
    Assert.assertEquals(URL, postResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, postResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, postResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(postResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(RESPONSE_JSON, ((JsonResponseBody) postResponse.getBody()).getBody());
    Assert.assertEquals(RESPONSE_JSON_VALUE, postResponse.getServiceResponse().getRequestId());
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
    Validator patchHttpValidator =
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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            patchCredential.getClass().getSimpleName(),
            patchHttpValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());
    OkHttpClient.Builder patchOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String method = chain.request().method();

                  Headers headers = chain.request().headers();
                  RequestBody requestBody = chain.request().body();
                  RequestBody createRequestBody =
                      RequestBody.create(
                          JSON_REQUEST_BODY.getBody(),
                          MediaType.parse(JSON_REQUEST_BODY.getContentType()));

                  Assert.assertEquals(method, HttpMethod.PATCH.name());
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, headers.size());
                  Assert.assertEquals(REQUEST_HEADER_VALUE, headers.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(userAgent, headers.get(USER_AGENT));
                  Assert.assertEquals(
                      patchCredential.getAuthorization(
                          URI.create(URL), method, JSON_REQUEST_BODY.getBody()),
                      headers.get(AUTHORIZATION));
                  Assert.assertEquals(createRequestBody.contentType(), requestBody.contentType());
                  Assert.assertEquals(
                      createRequestBody.contentLength(), requestBody.contentLength());

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient patchHttpClient =
        new DefaultHttpClientBuilder()
            .credential(patchCredential)
            .validator(patchHttpValidator)
            .okHttpClient(patchOkHttpClientBuilder.build())
            .build();

    HttpResponse<TestServiceResponse> patchResponse =
        patchHttpClient.patch(requestHeaders, URL, JSON_REQUEST_BODY, TestServiceResponse.class);

    Assert.assertEquals(3, patchResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        patchResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(userAgent, patchResponse.getRequest().getHeaders().getHeader(USER_AGENT));
    Assert.assertEquals(
        FAKE_AUTHORIZATION, patchResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.PATCH, patchResponse.getRequest().getHttpMethod());
    Assert.assertEquals(JSON_REQUEST_BODY, patchResponse.getRequest().getBody());
    Assert.assertEquals(URL, patchResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, patchResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, patchResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(patchResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(RESPONSE_JSON, ((JsonResponseBody) patchResponse.getBody()).getBody());
    Assert.assertEquals(RESPONSE_JSON_VALUE, patchResponse.getServiceResponse().getRequestId());
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
    Validator putHttpValidator =
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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            putCredential.getClass().getSimpleName(),
            putHttpValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());
    OkHttpClient.Builder putOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String putMethod = chain.request().method();

                  Headers putHeaders = chain.request().headers();
                  RequestBody requestBody = chain.request().body();
                  RequestBody createRequestBody =
                      RequestBody.create(
                          JSON_REQUEST_BODY.getBody(),
                          MediaType.parse(JSON_REQUEST_BODY.getContentType()));

                  Assert.assertEquals(putMethod, HttpMethod.PUT.name());
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, putHeaders.size());
                  Assert.assertEquals(REQUEST_HEADER_VALUE, putHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(userAgent, putHeaders.get(USER_AGENT));
                  Assert.assertEquals(
                      putCredential.getAuthorization(
                          URI.create(URL), putMethod, JSON_REQUEST_BODY.getBody()),
                      putHeaders.get(AUTHORIZATION));
                  Assert.assertEquals(createRequestBody.contentType(), requestBody.contentType());
                  Assert.assertEquals(
                      createRequestBody.contentLength(), requestBody.contentLength());

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient putHttpClient =
        new DefaultHttpClientBuilder()
            .credential(putCredential)
            .validator(putHttpValidator)
            .okHttpClient(putOkHttpClientBuilder.build())
            .build();
    HttpResponse<TestServiceResponse> putResponse =
        putHttpClient.put(requestHeaders, URL, JSON_REQUEST_BODY, TestServiceResponse.class);

    Assert.assertEquals(3, putResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE, putResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(userAgent, putResponse.getRequest().getHeaders().getHeader(USER_AGENT));
    Assert.assertEquals(
        FAKE_AUTHORIZATION, putResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.PUT, putResponse.getRequest().getHttpMethod());
    Assert.assertEquals(JSON_REQUEST_BODY, putResponse.getRequest().getBody());
    Assert.assertEquals(URL, putResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, putResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, putResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(putResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(RESPONSE_JSON, ((JsonResponseBody) putResponse.getBody()).getBody());
    Assert.assertEquals(RESPONSE_JSON_VALUE, putResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testDelete() {

    Credential deleteCredential =
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
            Assert.assertEquals("", signBody);
            return FAKE_AUTHORIZATION;
          }
        };
    Validator deleteHttpValidator =
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
        };
    String userAgent =
        String.format(
            USER_AGENT_FORMAT,
            OkHttpClientAdapter.class.getPackage().getImplementationVersion(),
            OS,
            VERSION == null ? "Unknown" : VERSION,
            deleteCredential.getClass().getSimpleName(),
            deleteHttpValidator.getClass().getSimpleName(),
            "okhttp3/" + getClass().getPackage().getImplementationVersion());
    OkHttpClient.Builder deleteOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  String deleteMethod = chain.request().method();

                  Headers deleteHeaders = chain.request().headers();

                  Assert.assertEquals(deleteMethod, HttpMethod.DELETE.name());
                  Assert.assertEquals(URL, chain.request().url().url().toString());
                  Assert.assertEquals(3, deleteHeaders.size());
                  Assert.assertEquals(REQUEST_HEADER_VALUE, deleteHeaders.get(REQUEST_HEADER_KEY));
                  Assert.assertEquals(userAgent, deleteHeaders.get(USER_AGENT));
                  Assert.assertEquals(
                      deleteCredential.getAuthorization(URI.create(URL), deleteMethod, ""),
                      deleteHeaders.get(AUTHORIZATION));
                  Assert.assertEquals(0, chain.request().body().contentLength());

                  return new Response.Builder()
                      .request(chain.request())
                      .code(HTTP_OK)
                      .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              RESPONSE_JSON,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient deleteHttpClient =
        new DefaultHttpClientBuilder()
            .credential(deleteCredential)
            .validator(deleteHttpValidator)
            .okHttpClient(deleteOkHttpClientBuilder.build())
            .build();

    HttpResponse<TestServiceResponse> deleteResponse =
        deleteHttpClient.delete(requestHeaders, URL, TestServiceResponse.class);

    Assert.assertEquals(3, deleteResponse.getRequest().getHeaders().getHeaders().size());
    Assert.assertEquals(
        REQUEST_HEADER_VALUE,
        deleteResponse.getRequest().getHeaders().getHeader(REQUEST_HEADER_KEY));
    Assert.assertEquals(userAgent, deleteResponse.getRequest().getHeaders().getHeader(USER_AGENT));
    Assert.assertEquals(
        FAKE_AUTHORIZATION, deleteResponse.getRequest().getHeaders().getHeader(AUTHORIZATION));
    Assert.assertEquals(HttpMethod.DELETE, deleteResponse.getRequest().getHttpMethod());
    Assert.assertNull(deleteResponse.getRequest().getBody());
    Assert.assertEquals(URL, deleteResponse.getRequest().getUrl().toString());
    Assert.assertEquals(1, deleteResponse.getHeaders().getHeaders().size());
    Assert.assertEquals(
        RESPONSE_HEADER_VALUE, deleteResponse.getHeaders().getHeader(RESPONSE_HEADER_KEY));
    Assert.assertTrue(deleteResponse.getBody() instanceof JsonResponseBody);
    Assert.assertEquals(RESPONSE_JSON, ((JsonResponseBody) deleteResponse.getBody()).getBody());
    Assert.assertEquals(RESPONSE_JSON_VALUE, deleteResponse.getServiceResponse().getRequestId());
  }

  @Test
  public void testGzipResponseBody() throws IOException {
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
            return FAKE_AUTHORIZATION;
          }
        };
    Validator getHttpValidator =
        new Validator() {
          @Override
          public <T> boolean validate(HttpHeaders responseHeaders, String body) {
            return true;
          }
        };
    try (Buffer buffer = new Buffer()) {
      // gzip string
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
        gzip.write(RESPONSE_JSON.getBytes());
      }
      buffer.write(out.toByteArray());
      GzipSource gzipSource = new GzipSource(buffer);
      OkHttpClient.Builder getOkHttpClientBuilder =
          new Builder()
              .addInterceptor(
                  chain -> {
                    Response response =
                        new Response.Builder()
                            .request(chain.request())
                            .code(HTTP_OK)
                            .header(RESPONSE_HEADER_KEY, RESPONSE_HEADER_VALUE)
                            .header("Content-Encoding", "gzip")
                            .message("ok")
                            .protocol(Protocol.HTTP_1_1)
                            .body(
                                new RealResponseBody(
                                    com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                        .getValue(),
                                    -1,
                                    Okio.buffer(gzipSource)))
                            .build();
                    Assert.assertEquals(-1, response.body().contentLength());
                    return response;
                  });
      HttpClient getHttpClient =
          new DefaultHttpClientBuilder()
              .credential(getCredential)
              .validator(getHttpValidator)
              .okHttpClient(getOkHttpClientBuilder.build())
              .build();
      HttpResponse<TestServiceResponse> getResponse =
          getHttpClient.get(requestHeaders, URL, TestServiceResponse.class);
      Assert.assertNotNull(getResponse.getBody());
      Assert.assertEquals(RESPONSE_JSON, ((JsonResponseBody) getResponse.getBody()).getBody());
    }
  }
}
