package com.wechat.pay.java.core.http.apache;

import com.wechat.pay.java.core.http.Constant;
import org.apache.http.*;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class ApacheHttpMultiDomainRequestInterceptor implements HttpRequestInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(ApacheHttpMultiDomainRequestInterceptor.class);
  @Override
  public void process(HttpRequest request, HttpContext context) throws HttpException {
    Object useSecondaryDomain = context.getAttribute("USE_SECONDARY_API_DOMAIN");
    if (useSecondaryDomain == null || !((boolean) useSecondaryDomain)) {
      return;
    }
    HttpRequestWrapper httpRequestWrapper = HttpRequestWrapper.class.cast(request);
    URI originUri = httpRequestWrapper.getURI();
    try {
      URI newUri = new URI(originUri.getScheme(), originUri.getUserInfo(), Constant.SECONDARY_API_DOMAIN, originUri.getPort(), originUri.getPath(), originUri.getQuery(), originUri.getFragment());
      httpRequestWrapper.setURI(newUri);
      httpRequestWrapper.setHeader(HttpHeaders.HOST, Constant.SECONDARY_API_DOMAIN);
      httpRequestWrapper.setHeader(Constant.USER_AGENT, getHeaderValue(request.getAllHeaders(), Constant.USER_AGENT) + " (Retried-V1)");
    } catch (URISyntaxException e) {
      logger.error("new Uri catch exception", e.getMessage());
    }
  }

  private String getHeaderValue(Header[] headers, String name) {
    for (Header header : headers) {
      if (header.getName().equalsIgnoreCase(name)) {
        return header.getValue();
      }
    }
    return null;
  }
}
