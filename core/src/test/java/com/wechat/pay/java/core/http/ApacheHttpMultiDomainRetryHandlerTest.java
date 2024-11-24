package com.wechat.pay.java.core.http;
import com.wechat.pay.java.core.http.apache.ApacheHttpMultiDomainRetryHandler;
import com.wechat.pay.java.core.http.apache.ApacheHttpMultiDomainRequestInterceptor;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.DnsResolver;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import okhttp3.HttpUrl;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApacheHttpMultiDomainRetryHandlerTest {
  private static class FakeDnsResolver implements DnsResolver  {
    private final Map<String, List<InetAddress>> addressMappings = new HashMap<>();

    void addAddress(String hostname, InetAddress address) {
      addressMappings.computeIfAbsent(hostname, k -> new ArrayList<>()).add(address);
    }

    @Override
    public InetAddress[] resolve(String host) throws UnknownHostException {
      List<InetAddress> addresses = addressMappings.get(host);
      if (addresses != null && !addresses.isEmpty()) {
        return addresses.toArray(new InetAddress[0]);
      } else {
        throw new UnknownHostException("Unable to resolve the hostname: " + host);
      }
    }
  }
  private FakeDnsResolver fakeDnsResolver;

  @BeforeEach
  void setUp() {
    fakeDnsResolver = new FakeDnsResolver();
  }

  @Disabled("manual test")
  @Test
  void testOkHttpMultiDomain() throws Exception {
    final CloseableHttpClient client = HttpClientBuilder.create().setDnsResolver(fakeDnsResolver).
        addInterceptorLast(new ApacheHttpMultiDomainRequestInterceptor()).
        setRetryHandler(new ApacheHttpMultiDomainRetryHandler()).build();

    InetAddress nullAddress = InetAddress.getByName("10.0.0.1");
    InetAddress realAddress = InetAddress.getByName("121.51.50.140");
    fakeDnsResolver.addAddress("api.mch.weixin.qq.com", nullAddress);
    fakeDnsResolver.addAddress("api2.wechatpay.cn", realAddress);

    HttpGet httpGet = new HttpGet("https://api.mch.weixin.qq.com/v3/pay/transactions/id/1234?mchid=1234");
    httpGet.addHeader("User-Agent", "testOkHttpMultiDomain");
    httpGet.addHeader("Accept", "*/*");
    CloseableHttpResponse response = client.execute(httpGet);
  }

  @Test
  void testNormalOther() throws Exception {
    MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse().setBody("Successful response"));

    final CloseableHttpClient client = HttpClientBuilder.create().
        addInterceptorFirst(new ApacheHttpMultiDomainRequestInterceptor()).
        setRetryHandler(new ApacheHttpMultiDomainRetryHandler()).build();

    HttpGet request = new HttpGet(server.url("/").toString());
    CloseableHttpResponse response = client.execute(request);
    assertEquals(200, response.getStatusLine().getStatusCode());
    server.shutdown();
  }

  @Test
  void testNormalWeChatPay() throws Exception {
    MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse().setBody("Successful"));
    fakeDnsResolver.addAddress(
        "api.wechatpay.cn",
        InetAddress.getByAddress("api.wechatpay.cn", new byte[] {127, 0, 0, 1}));
    final CloseableHttpClient client = HttpClientBuilder.create().
        setDnsResolver(fakeDnsResolver).
        addInterceptorFirst(new ApacheHttpMultiDomainRequestInterceptor()).
        setRetryHandler(new ApacheHttpMultiDomainRetryHandler()).build();

    HttpUrl mockUrl = server.url("/test");
    HttpUrl getUrl = mockUrl.newBuilder().host("api.wechatpay.cn").build();

    HttpGet httpGet = new HttpGet(getUrl.toString());
    httpGet.addHeader("User-Agent", "testOkHttpMultiDomain");
    httpGet.addHeader("Accept", "*/*");

    CloseableHttpResponse response = client.execute(httpGet);
    assertEquals(200, response.getStatusLine().getStatusCode());
    RecordedRequest request1 = server.takeRequest();
    assertEquals("testOkHttpMultiDomain", request1.getHeader("User-Agent"));
    server.shutdown();
  }

  @Test
  void testOkHttpMultiDomainWithMock() throws Exception {
    MockWebServer server = new MockWebServer();
    final CloseableHttpClient client = HttpClientBuilder.create().setDnsResolver(fakeDnsResolver).
        setRetryHandler(new ApacheHttpMultiDomainRetryHandler()).
        addInterceptorFirst(new ApacheHttpMultiDomainRequestInterceptor()).build();

    server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));
    server.enqueue(new MockResponse().setBody("Successful"));

    HttpUrl mockUrl = server.url("/test");
    fakeDnsResolver.addAddress(
        "api.mch.weixin.qq.com",
        InetAddress.getByAddress("api.mch.weixin.qq.com", new byte[] {127, 0, 0, 1}));
    fakeDnsResolver.addAddress(
        "api1.wechatpay.cn",
        InetAddress.getByAddress("api2.wechatpay.cn", new byte[] {127, 0, 0, 1}));

    HttpGet httpGet = new HttpGet(mockUrl.newBuilder().host("api.mch.weixin.qq.com").build().toString());
    httpGet.addHeader("User-Agent", "testOkHttpMultiDomain");
    httpGet.addHeader("Accept", "*/*");

    try (CloseableHttpResponse response = client.execute(httpGet)) {
      assertEquals(200, response.getStatusLine().getStatusCode());
      RecordedRequest request1 = server.takeRequest();
      // Host: <host>:<port>
      assertTrue(request1.getHeader("Host").contains("api.mch.weixin.qq.com"));
      RecordedRequest request2 = server.takeRequest();
      assertTrue(request2.getHeader("Host").contains("api2.wechatpay.cn"));
      assertEquals("testOkHttpMultiDomain (Retried-V1)", request2.getHeader("User-Agent"));
    }
    server.shutdown();
  }
}
