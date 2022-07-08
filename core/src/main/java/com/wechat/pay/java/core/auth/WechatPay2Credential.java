package com.wechat.pay.java.core.auth;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.cipher.SignatureResult;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.util.NonceUtil;
import java.net.URI;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 微信支付认证凭据生成器 */
public final class WechatPay2Credential implements Credential {

  private static final Logger logger = LoggerFactory.getLogger(WechatPay2Credential.class);

  private static final int NONCE_LENGTH = 32;
  public static final String SCHEMA_PREFIX = "WECHATPAY2-";
  private final String merchantId;
  private final Signer signer;

  public WechatPay2Credential(String merchantId, Signer signer) {
    this.merchantId = requireNonNull(merchantId);
    this.signer = requireNonNull(signer);
  }

  @Override
  public String getMerchantId() {
    return merchantId;
  }

  @Override
  public String getSchema() {
    return SCHEMA_PREFIX + signer.getAlgorithm();
  }

  @Override
  public String getAuthorization(URI uri, String httpMethod, String signBody) {
    requireNonNull(uri);
    requireNonNull(httpMethod);
    return getSchema() + " " + getToken(uri, httpMethod, signBody);
  }

  private String getToken(URI uri, String httpMethod, String signBody) {
    String nonceStr = NonceUtil.createNonce(NONCE_LENGTH);
    long timestamp = Instant.now().getEpochSecond();
    String message = buildMessage(nonceStr, timestamp, uri, httpMethod, signBody);
    logger.debug("authorization message[{}]", message);
    SignatureResult signature = signer.sign(message);
    String token =
        "mchid=\""
            + getMerchantId()
            + "\","
            + "nonce_str=\""
            + nonceStr
            + "\","
            + "timestamp=\""
            + timestamp
            + "\","
            + "serial_no=\""
            + signature.getCertificateSerialNumber()
            + "\","
            + "signature=\""
            + signature.getSign()
            + "\"";
    logger.debug("The generated request signature information is[{}]", token);
    return token;
  }

  private String buildMessage(
      String nonce, long timestamp, URI uri, String httpMethod, String signBody) {
    String canonicalUrl = uri.getRawPath();
    if (uri.getQuery() != null) {
      canonicalUrl += "?" + uri.getRawQuery();
    }
    return httpMethod
        + "\n"
        + canonicalUrl
        + "\n"
        + timestamp
        + "\n"
        + nonce
        + "\n"
        + (signBody == null ? "" : signBody)
        + "\n";
  }
}
