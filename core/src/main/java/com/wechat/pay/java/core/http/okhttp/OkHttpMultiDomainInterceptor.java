package com.wechat.pay.java.core.http.okhttp;

import com.wechat.pay.java.core.http.Constant;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A dual-domain retry interceptor. This is an application interceptor that retries a request using
 * "api2.wechatpay.cn" in case of network failure. It leverages disaster recovery in different city
 * access data centers, while also avoiding failures due to DNS blocking.
 */
public class OkHttpMultiDomainInterceptor implements Interceptor {
  private static final Logger logger = LoggerFactory.getLogger(OkHttpMultiDomainInterceptor.class);

  @NotNull
  @Override
  public okhttp3.Response intercept(Chain chain) throws IOException {
    Request request = chain.request();

    if (shouldRetry(request)) {
      try {
        /*
         * Implementations of this interface throw IOException to signal connectivity failures.
         * This includes both natural exceptions such as unreachable servers,
         * as well as synthetic exceptions when responses are of an unexpected type or cannot be decoded
         */
        return chain.proceed(request);
      } catch (IOException e) {
        logger.warn("Retrying request due to connectivity failure: {}", e.getMessage(), e);

        Request retryRequest = modifyRequestForRetry(request);
        return chain.proceed(retryRequest);
      }
    }

    return chain.proceed(request);
  }

  private boolean shouldRetry(Request request) {
    return Constant.PRIMARY_API_DOMAIN.contains(request.url().host());
  }

  private Request modifyRequestForRetry(Request originalRequest) {
    HttpUrl.Builder urlBuilder = originalRequest.url().newBuilder();
    urlBuilder.host(Constant.SECONDARY_API_DOMAIN);

    Request.Builder reqBuilder = originalRequest.newBuilder();
    reqBuilder.url(urlBuilder.build());
    reqBuilder.header(
        Constant.USER_AGENT, originalRequest.header(Constant.USER_AGENT) + " (Retried-V1)");

    return reqBuilder.build();
  }
}
