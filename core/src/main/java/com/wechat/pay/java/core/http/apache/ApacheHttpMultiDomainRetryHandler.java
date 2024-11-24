package com.wechat.pay.java.core.http.apache;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import com.wechat.pay.java.core.http.Constant;
import org.apache.http.client.methods.RequestBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpHost;


public class ApacheHttpMultiDomainRetryHandler implements HttpRequestRetryHandler {
  private static final Logger logger = LoggerFactory.getLogger(ApacheHttpMultiDomainRetryHandler.class);
  @Override
  public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
    if (!shouldRetry(context)) {
      return false;
    }
    if (exception != null) {
      context.setAttribute("USE_SECONDARY_API_DOMAIN", true);
      return true;
    }
    // 其他异常不重试
    return false;
  }

  private boolean shouldRetry(HttpContext context) {
    Object host = context.getAttribute("http.target_host");
    if (host == null || !(host instanceof HttpHost)) {
        logger.error("http.target_host is null or not instance of HttpHost");
        return false;
    }
    String domain = ((HttpHost) host).getHostName();
    return Constant.PRIMARY_API_DOMAIN.contains(domain);
  }
}


