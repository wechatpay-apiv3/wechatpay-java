package com.wechat.pay.java.core.auth;

import static com.wechat.pay.java.core.cipher.Constant.HEX;
import static com.wechat.pay.java.core.http.Constant.REQUEST_ID;
import static com.wechat.pay.java.core.http.Constant.WECHAT_PAY_NONCE;
import static com.wechat.pay.java.core.http.Constant.WECHAT_PAY_SERIAL;
import static com.wechat.pay.java.core.http.Constant.WECHAT_PAY_SIGNATURE;
import static com.wechat.pay.java.core.http.Constant.WECHAT_PAY_TIMESTAMP;
import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE;

import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.http.HttpHeaders;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class WechatPay2ValidatorTest {

  private static final String BODY = "body";
  private static final String NONCE = "nonce";
  private static Validator validator;
  private static HttpHeaders httpHeaders;

  @BeforeClass
  public static void init() {

    long timestamp = Instant.now().getEpochSecond();

    String buildMessage = timestamp + "\n" + NONCE + "\n" + BODY + "\n";

    Verifier fakeVerifier =
        (serialNumber, message, signature) -> {
          Assert.assertEquals(buildMessage, message);
          return true;
        };

    validator = new WechatPay2Validator(fakeVerifier);
    Map<String, String> headers = new HashMap<>();
    headers.put(REQUEST_ID, "request-id");
    headers.put(WECHAT_PAY_SERIAL, WECHAT_PAY_CERTIFICATE.getSerialNumber().toString(HEX));
    headers.put(WECHAT_PAY_TIMESTAMP, Long.toString(timestamp));
    headers.put(WECHAT_PAY_NONCE, NONCE);
    headers.put(WECHAT_PAY_SIGNATURE, "signature");
    httpHeaders = new HttpHeaders(headers);
  }

  @Test
  public void testValidate() {
    Assert.assertTrue(validator.validate(httpHeaders, BODY));
  }
}
