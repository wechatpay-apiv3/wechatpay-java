package com.wechat.pay.java.core.http;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import java.util.stream.Stream;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public interface HttpClientTest {
  HttpClient createHttpClient();

  HttpClient createFalseValidationHttpClient();

  class Response {
    public String one;
    public int two;
  }

  String testUrl = "/path/to/test?p1=a&p2=b&p3=[ab%2F%23cd]";
  String testResponseBody = "{\"one\":\"one\",\"two\":2}";

  static Stream<Arguments> requestProvider() {
    return Stream.of(
        arguments(HttpMethod.GET, Named.of("无body", "")),
        arguments(HttpMethod.DELETE, Named.of("无body", "")),
        arguments(HttpMethod.POST, Named.of("无body", "")),
        arguments(HttpMethod.POST, Named.of("有body", "post-data")),
        arguments(HttpMethod.PUT, Named.of("无body", "")),
        arguments(HttpMethod.PUT, Named.of("有body", "put-data")),
        arguments(HttpMethod.PATCH, Named.of("无body", "")),
        arguments(HttpMethod.PATCH, Named.of("有body", "patch-data")));
  }

  @DisplayName("测试请求参数是否正确")
  @ParameterizedTest(name = "case {index}: {0} {1}")
  @MethodSource("requestProvider")
  default void testExecute_Request(HttpMethod method, String requestBody) throws Exception {
    HttpClient client = createHttpClient();

    MockWebServer server = new MockWebServer();
    server.enqueue(
        new MockResponse()
            .setBody(testResponseBody)
            .setHeader("Content-Type", "application/json; charset=utf-8"));
    server.start();

    HttpUrl requestUrl = server.url(testUrl);
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader("Test-Header1", "HeaderValue1");
    headers.addHeader("Test-Header2", "HeaderValue2");

    HttpRequest.Builder requestBuilder =
        new HttpRequest.Builder().httpMethod(method).url(requestUrl.url()).headers(headers);

    if (method == HttpMethod.POST || method == HttpMethod.PATCH || method == HttpMethod.PUT) {
      requestBuilder.body(new JsonRequestBody.Builder().body(requestBody).build());
    }

    client.execute(requestBuilder.build(), null);

    RecordedRequest request = server.takeRequest();
    assertEquals(method.name(), request.getMethod());
    assertEquals(requestUrl, request.getRequestUrl());
    assertEquals("HeaderValue1", request.getHeader("Test-Header1"));
    assertEquals("HeaderValue2", request.getHeader("Test-Header2"));
    assertEquals(requestBody, request.getBody().readUtf8());

    server.shutdown();
  }

  @DisplayName("测试应答处理200是否正确")
  @ParameterizedTest(name = "case {index}: {0} {1}")
  @MethodSource("requestProvider")
  default void testExecute_Response_200Ok(HttpMethod method, String requestBody) throws Exception {
    HttpClient client = createHttpClient();

    MockWebServer server = new MockWebServer();
    server.enqueue(
        new MockResponse()
            .setBody(testResponseBody)
            .setHeader("Test-Header", "HeaderValue")
            .setHeader("Content-Type", "application/json; charset=utf-8"));
    server.start();

    HttpUrl requestUrl = server.url(testUrl);
    HttpRequest.Builder requestBuilder =
        new HttpRequest.Builder().httpMethod(method).url(requestUrl.url());

    if (method == HttpMethod.POST || method == HttpMethod.PATCH || method == HttpMethod.PUT) {
      requestBuilder.body(new JsonRequestBody.Builder().body(requestBody).build());
    }

    HttpResponse<Response> response = client.execute(requestBuilder.build(), Response.class);
    assertEquals("one", response.getServiceResponse().one);
    assertEquals(2, response.getServiceResponse().two);
    assertEquals("HeaderValue", response.getHeaders().getHeader("Test-Header"));

    JsonResponseBody responseBody = (JsonResponseBody) response.getBody();
    assertEquals(testResponseBody, responseBody.getBody());

    server.shutdown();
  }

  @DisplayName("测试应答处理204是否正确")
  @ParameterizedTest(name = "case {index}: {0} {1}")
  @MethodSource("requestProvider")
  default void testExecute_Response_204NoContent(HttpMethod method, String requestBody)
      throws Exception {
    HttpClient client = createHttpClient();

    MockWebServer server = new MockWebServer();
    server.enqueue(
        new MockResponse()
            .setStatus("HTTP/1.1 204 No Content")
            .setHeader("Test-Header", "HeaderValue"));
    server.start();

    HttpUrl requestUrl = server.url(testUrl);
    HttpRequest.Builder requestBuilder =
        new HttpRequest.Builder().httpMethod(method).url(requestUrl.url());

    if (method == HttpMethod.POST || method == HttpMethod.PATCH || method == HttpMethod.PUT) {
      requestBuilder.body(new JsonRequestBody.Builder().body(requestBody).build());
    }

    assertDoesNotThrow(() -> client.execute(requestBuilder.build(), Response.class));
    // todo: need an download() API which would not check response's Content-Type

    server.shutdown();
  }

  @Test
  default void testExecute_ServiceException() throws Exception {
    HttpClient client = createHttpClient();
    MockWebServer server = new MockWebServer();
    server.enqueue(
        new MockResponse()
            .setStatus("HTTP/1.1 400 Bad Request")
            .setBody("{\"code\":\"INVALID_REQUEST\",\"message\":\"test message\"}")
            .setHeader("Content-Type", "application/json; charset=utf-8"));
    server.start();
    HttpUrl requestUrl = server.url(testUrl);

    final ServiceException thrown =
        assertThrows(
            ServiceException.class, () -> client.get(null, requestUrl.toString(), Response.class));
    assertEquals(400, thrown.getHttpStatusCode());
    assertEquals("INVALID_REQUEST", thrown.getErrorCode());
    assertEquals("test message", thrown.getErrorMessage());
    server.shutdown();
  }

  @Test
  default void testExecute_MalformedMessageException() throws Exception {
    HttpClient client = createHttpClient();
    MockWebServer server = new MockWebServer();
    server.enqueue(
        new MockResponse()
            .setBody("testResponseBody")
            .setHeader("Content-Type", "text/plain; charset=utf-8"));
    server.start();
    HttpUrl requestUrl = server.url(testUrl);
    assertThrows(
        MalformedMessageException.class,
        () -> client.get(null, requestUrl.toString(), Response.class));

    server.shutdown();
  }

  @Test
  default void testExecute_ValidationException() throws Exception {
    HttpClient client = createFalseValidationHttpClient();
    MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse().setStatus("HTTP/1.1 204 No Content"));
    server.start();
    HttpUrl requestUrl = server.url(testUrl);

    assertThrows(
        ValidationException.class, () -> client.get(null, requestUrl.toString(), Response.class));

    server.shutdown();
  }

  @Test
  default void testExecute_HttpException() {
    HttpClient client = createHttpClient();
    assertThrows(
        HttpException.class, () -> client.get(null, "http://url.not.avalible", Response.class));
  }
}
