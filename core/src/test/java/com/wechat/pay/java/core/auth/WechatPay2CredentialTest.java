package com.wechat.pay.java.core.auth;

import static com.wechat.pay.java.core.auth.WechatPay2Credential.SCHEMA_PREFIX;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_ID;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.wechat.pay.java.core.cipher.SignatureResult;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.util.NonceUtil;
import java.net.URI;
import java.time.Instant;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;

public class WechatPay2CredentialTest {

  private static String buildMessage;
  private static WechatPay2Credential credential;
  private static Signer fakeSigner;

  private static final String NONCE = "eBYqCOxa0QudMnPvVilkTzOg3gHh5Z3u";
  private static final long TIMESTAMP = 1652750623;
  private static final String SIGN_BODY = "";
  private static final URI REQUEST_URI =
      URI.create(
          "https://api.mch.weixin.qq.com/v3/pay/transactions/id/1217752501201407033233368018?mchid=1230000109");
  private static final String HTTP_METHOD = HttpMethod.GET.name();
  private static MockedStatic<Instant> instantMockedStatic;

  @BeforeClass
  public static void init() {
    Instant mockInstant = mock(Instant.class);
    instantMockedStatic = mockStatic(Instant.class);
    instantMockedStatic.when(Instant::now).thenReturn(mockInstant);
    doReturn(TIMESTAMP).when(mockInstant).getEpochSecond();

    String canonicalUrl = REQUEST_URI.getRawPath();
    if (REQUEST_URI.getQuery() != null) {
      canonicalUrl += "?" + REQUEST_URI.getRawQuery();
    }

    buildMessage =
        HTTP_METHOD
            + "\n"
            + canonicalUrl
            + "\n"
            + Instant.now().getEpochSecond()
            + "\n"
            + NONCE
            + "\n"
            + SIGN_BODY
            + "\n";

    fakeSigner =
        new Signer() {
          @Override
          public SignatureResult sign(String message) {
            Assert.assertEquals(buildMessage, message);
            return new SignatureResult("fake-sign", MERCHANT_CERTIFICATE_SERIAL_NUMBER);
          }

          @Override
          public String getAlgorithm() {
            return "fake-algorithm";
          }
        };

    credential = new WechatPay2Credential(MERCHANT_ID, fakeSigner);
  }

  @AfterClass
  public static void close() {
    instantMockedStatic.close();
  }

  @Test
  public void testGetMerchantId() {
    Assert.assertEquals(MERCHANT_ID, credential.getMerchantId());
  }

  @Test
  public void testGetSchema() {
    Assert.assertEquals(SCHEMA_PREFIX + fakeSigner.getAlgorithm(), credential.getSchema());
  }

  @Test
  public void testGetAuthorization() {

    try (MockedStatic<NonceUtil> nonceUtilMockedStatic = mockStatic(NonceUtil.class)) {
      nonceUtilMockedStatic.when(() -> NonceUtil.createNonce(anyInt())).thenReturn(NONCE);

      String schema = SCHEMA_PREFIX + fakeSigner.getAlgorithm();
      SignatureResult signatureResult = fakeSigner.sign(buildMessage);
      String signatureMessage =
          "mchid=\""
              + MERCHANT_ID
              + "\","
              + "nonce_str=\""
              + NONCE
              + "\","
              + "timestamp=\""
              + TIMESTAMP
              + "\","
              + "serial_no=\""
              + signatureResult.getCertificateSerialNumber()
              + "\","
              + "signature=\""
              + signatureResult.getSign()
              + "\"";

      String authorization = schema + " " + signatureMessage;
      String result = credential.getAuthorization(REQUEST_URI, HTTP_METHOD, SIGN_BODY);
      Assert.assertEquals(authorization, result);
    }
  }
}
