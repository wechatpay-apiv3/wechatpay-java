package com.wechat.pay.java.shangmi.kona;

import com.wechat.pay.java.core.util.IOUtil;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class TestConfig {

  public static final String RESOURCES_DIR;
  public static final String MERCHANT_PRIVATE_KEY_PATH;
  public static final String MERCHANT_CERTIFICATE_PATH;
  public static final String MERCHANT_PRIVATE_KEY_STRING;
  public static final String MERCHANT_CERTIFICATE_STRING;
  public static final String WECHAT_PAY_CERTIFICATE_PATH;
  public static final String WECHAT_PAY_CERTIFICATE_STRING;
  public static final PrivateKey MERCHANT_PRIVATE_KEY;
  public static final X509Certificate MERCHANT_CERTIFICATE;
  public static final X509Certificate WECHAT_PAY_CERTIFICATE;
  public static final String MERCHANT_CERTIFICATE_SERIAL_NUMBER;
  public static final String WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER;
  public static final String MERCHANT_ID;
  public static final String API_V3_KEY = "a7cde1ZJB1kG2e7VfTs3jQzaWizur8Gb";

  static {
    try {
      RESOURCES_DIR = System.getProperty("user.dir") + "/src/test/resources";
      MERCHANT_PRIVATE_KEY_PATH = RESOURCES_DIR + "/merchant_private_key.pem";
      MERCHANT_CERTIFICATE_PATH = RESOURCES_DIR + "/merchant_certificate.pem";
      WECHAT_PAY_CERTIFICATE_PATH = RESOURCES_DIR + "/wechat_pay_certificate.pem";
      MERCHANT_PRIVATE_KEY_STRING = IOUtil.loadStringFromPath(MERCHANT_PRIVATE_KEY_PATH);
      MERCHANT_PRIVATE_KEY = SMPemUtil.loadPrivateKeyFromString(MERCHANT_PRIVATE_KEY_STRING);
      MERCHANT_CERTIFICATE_STRING = IOUtil.loadStringFromPath(MERCHANT_CERTIFICATE_PATH);
      MERCHANT_CERTIFICATE = SMPemUtil.loadCertificateFromString(MERCHANT_CERTIFICATE_STRING);
      WECHAT_PAY_CERTIFICATE_STRING = IOUtil.loadStringFromPath(WECHAT_PAY_CERTIFICATE_PATH);
      WECHAT_PAY_CERTIFICATE = SMPemUtil.loadCertificateFromString(WECHAT_PAY_CERTIFICATE_STRING);
      MERCHANT_CERTIFICATE_SERIAL_NUMBER = MERCHANT_CERTIFICATE.getSerialNumber().toString(16);
      WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER = WECHAT_PAY_CERTIFICATE.getSerialNumber().toString(16);
      MERCHANT_ID = "1234567891";
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
