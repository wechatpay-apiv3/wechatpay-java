package com.wechat.pay.java.core.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.wechat.pay.java.core.http.okhttp.OkHttpMultiDomainInterceptor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class OkHttpMultiDomainInterceptorTest {
  private FakeDns fakeDns;

  @BeforeEach
  void setUp() {
    fakeDns = new FakeDns();
  }

  @Disabled("manual test")
  @Test
  void testOkHttpMultiDomain() throws IOException {
    OkHttpMultiDomainInterceptor interceptor = new OkHttpMultiDomainInterceptor();
    final OkHttpClient client =
        new OkHttpClient.Builder().dns(fakeDns).addInterceptor(interceptor).build();

    InetAddress nullAddress = InetAddress.getByName("10.0.0.1");
    InetAddress realAddress = InetAddress.getByName("121.51.50.140");
    fakeDns.addAddress("api.mch.weixin.qq.com", nullAddress);
    fakeDns.addAddress("api2.wechatpay.cn", realAddress);

    Request request =
        new Request.Builder()
            .url("https://api.mch.weixin.qq.com/v3/pay/transactions/id/1234?mchid=1234")
            .header("User-Agent", "testOkHttpMultiDomain")
            .header("Accept", "*/*")
            .build();

    try (Response response = client.newCall(request).execute()) {
      assertEquals(401, response.code());
    }
  }

  @Test
  void testNormalOther() throws Exception {
    MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse().setBody("Successful response"));

    OkHttpMultiDomainInterceptor interceptor = new OkHttpMultiDomainInterceptor();
    final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    // Act
    Request request = new Request.Builder().url(server.url("/")).build();
    Response response = client.newCall(request).execute();

    // Assert
    assertEquals(200, response.code());
    server.shutdown();
  }

  @Test
  void testNormalWeChatPay() throws Exception {
    MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse().setBody("Successful"));

    OkHttpMultiDomainInterceptor interceptor = new OkHttpMultiDomainInterceptor();
    final OkHttpClient client =
        new OkHttpClient.Builder().dns(fakeDns).addInterceptor(interceptor).build();

    HttpUrl mockUrl = server.url("/test");
    fakeDns.addAddress(
        "api.wechatpay.cn",
        InetAddress.getByAddress("api.wechatpay.cn", new byte[] {127, 0, 0, 1}));

    // Act
    Request request =
        new Request.Builder()
            .url(mockUrl.newBuilder().host("api.wechatpay.cn").build())
            .header("User-Agent", "testOkHttpMultiDomain")
            .header("Accept", "*/*")
            .build();

    Response response = client.newCall(request).execute();

    // Assert
    assertEquals(200, response.code());

    RecordedRequest request1 = server.takeRequest();
    assertEquals("testOkHttpMultiDomain", request1.getHeader("User-Agent"));
    server.shutdown();
  }

  @Test
  void testOkHttpMultiDomainWithMock() throws Exception {
    MockWebServer server = new MockWebServer();
    OkHttpMultiDomainInterceptor interceptor = new OkHttpMultiDomainInterceptor();
    final OkHttpClient client =
        new OkHttpClient.Builder().dns(fakeDns).addInterceptor(interceptor).build();

    server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));
    server.enqueue(new MockResponse().setBody("Successful"));

    HttpUrl mockUrl = server.url("/test");
    fakeDns.addAddress(
        "api.mch.weixin.qq.com",
        InetAddress.getByAddress("api.mch.weixin.qq.com", new byte[] {127, 0, 0, 1}));
    fakeDns.addAddress(
        "api2.wechatpay.cn",
        InetAddress.getByAddress("api2.wechatpay.cn", new byte[] {127, 0, 0, 1}));

    Request request =
        new Request.Builder()
            .url(mockUrl.newBuilder().host("api.mch.weixin.qq.com").build())
            .header("User-Agent", "testOkHttpMultiDomain")
            .header("Accept", "*/*")
            .build();

    try (Response response = client.newCall(request).execute()) {
      assertEquals(200, response.code());

      RecordedRequest request1 = server.takeRequest();
      // Host: <host>:<port>
      assertTrue(request1.getHeader("Host").contains("api.mch.weixin.qq.com"));

      RecordedRequest request2 = server.takeRequest();
      assertTrue(request2.getHeader("Host").contains("api2.wechatpay.cn"));
      assertEquals("testOkHttpMultiDomain (Retried-V1)", request2.getHeader("User-Agent"));
    }

    server.shutdown();
  }

  private static class FakeDns implements Dns {
    private final Map<String, List<InetAddress>> addressMappings = new HashMap<>();

    void addAddress(String hostname, InetAddress address) {
      addressMappings.computeIfAbsent(hostname, k -> new ArrayList<>()).add(address);
    }

    @NotNull
    @Override
    public List<InetAddress> lookup(@NotNull String hostname) throws UnknownHostException {
      List<InetAddress> addresses = addressMappings.get(hostname);
      if (addresses != null) {
        return addresses;
      } else {
        throw new UnknownHostException("Unable to resolve the hostname: " + hostname);
      }
    }
  }
}
