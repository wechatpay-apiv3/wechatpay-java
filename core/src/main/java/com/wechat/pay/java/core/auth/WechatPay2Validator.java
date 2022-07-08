package com.wechat.pay.java.core.auth;

import static com.wechat.pay.java.core.http.Constant.REQUEST_ID;
import static com.wechat.pay.java.core.http.Constant.WECHAT_PAY_NONCE;
import static com.wechat.pay.java.core.http.Constant.WECHAT_PAY_SERIAL;
import static com.wechat.pay.java.core.http.Constant.WECHAT_PAY_SIGNATURE;
import static com.wechat.pay.java.core.http.Constant.WECHAT_PAY_TIMESTAMP;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.http.HttpHeaders;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 微信支付返回验证器 */
public final class WechatPay2Validator implements Validator {

  private static final Logger logger = LoggerFactory.getLogger(WechatPay2Validator.class);
  private static final int RESPONSE_EXPIRED_MINUTES = 5;
  private final Verifier verifier;

  public WechatPay2Validator(Verifier verifier) {
    this.verifier = requireNonNull(verifier);
  }

  @Override
  public <T> boolean validate(HttpHeaders responseHeaders, String responseBody) {
    String timestamp = responseHeaders.getHeader(WECHAT_PAY_TIMESTAMP);
    try {
      Instant responseTime = Instant.ofEpochSecond(Long.parseLong(timestamp));
      // 拒绝过期请求
      if (Duration.between(responseTime, Instant.now()).abs().toMinutes()
          >= RESPONSE_EXPIRED_MINUTES) {
        throw new IllegalArgumentException(
            String.format(
                "Validate http response,timestamp[%s] of httpResponse is expires, "
                    + "request-id[%s]",
                timestamp, responseHeaders.getHeader(REQUEST_ID)));
      }
    } catch (DateTimeException | NumberFormatException e) {
      throw new IllegalArgumentException(
          String.format(
              "Validate http response,timestamp[%s] of httpResponse is invalid, request-id[%s]",
              timestamp, responseHeaders.getHeader(REQUEST_ID)));
    }
    String message =
        timestamp
            + "\n"
            + responseHeaders.getHeader(WECHAT_PAY_NONCE)
            + "\n"
            + (responseBody == null ? "" : responseBody)
            + "\n";
    logger.debug("Message for verifying signatures is[{}]", message);
    String serialNumber = responseHeaders.getHeader(WECHAT_PAY_SERIAL);
    logger.debug("SerialNumber for verifying signatures is[{}]", serialNumber);
    String signature = responseHeaders.getHeader(WECHAT_PAY_SIGNATURE);
    logger.debug("Signature for verifying signatures is[{}]", signature);
    return verifier.verify(serialNumber, message, signature);
  }
}
