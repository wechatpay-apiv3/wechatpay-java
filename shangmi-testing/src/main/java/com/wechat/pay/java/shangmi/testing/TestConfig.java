package com.wechat.pay.java.shangmi.testing;

import com.wechat.pay.java.core.util.IOUtil;
import java.io.IOException;
import java.io.UncheckedIOException;

public class TestConfig {
  public static final String MERCHANT_PRIVATE_KEY_PATH;
  public static final String MERCHANT_CERTIFICATE_PATH;
  public static final String MERCHANT_PRIVATE_KEY_STRING;
  public static final String MERCHANT_CERTIFICATE_STRING;
  public static final String WECHAT_PAY_CERTIFICATE_PATH;
  public static final String WECHAT_PAY_CERTIFICATE_STRING;
  public static final String MERCHANT_CERTIFICATE_SERIAL_NUMBER =
      "54E7F59ED8DF4570CFB14780EA8152D45A675EE6";
  public static final String MERCHANT_ID = "1234567891";
  public static final String API_V3_KEY = "a7cde1ZJB1kG2e7VfTs3jQzaWizur8Gb";

  private TestConfig() {
    throw new IllegalStateException("Utility class");
  }

  static {
    try {
      final String resourceDir =
          System.getProperty("user.dir") + "/../shangmi-testing/src/main/resources/";
      MERCHANT_PRIVATE_KEY_PATH = resourceDir + "merchant_private_key.pem";
      MERCHANT_CERTIFICATE_PATH = resourceDir + "merchant_certificate.pem";
      WECHAT_PAY_CERTIFICATE_PATH = resourceDir + "wechat_pay_certificate.pem";
      MERCHANT_PRIVATE_KEY_STRING = IOUtil.loadStringFromPath(MERCHANT_PRIVATE_KEY_PATH);
      MERCHANT_CERTIFICATE_STRING = IOUtil.loadStringFromPath(MERCHANT_CERTIFICATE_PATH);
      WECHAT_PAY_CERTIFICATE_STRING = IOUtil.loadStringFromPath(WECHAT_PAY_CERTIFICATE_PATH);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
