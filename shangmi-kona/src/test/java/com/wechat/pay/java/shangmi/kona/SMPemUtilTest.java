package com.wechat.pay.java.shangmi.kona;

import static com.wechat.pay.java.shangmi.kona.TestConfig.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import org.junit.jupiter.api.Test;

class SMPemUtilTest {
  @Test
  public void testLoadPrivateKeyFromPath() {
    PrivateKey privateKey = SMPemUtil.loadPrivateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH);
    assertNotNull(privateKey);
  }

  @Test
  public void testLoadPrivateKeyFromString() {
    PrivateKey privateKey = SMPemUtil.loadPrivateKeyFromString(MERCHANT_PRIVATE_KEY_STRING);
    assertNotNull(privateKey);
  }

  @Test
  public void testLoadX509FromPath() {
    X509Certificate certificate = SMPemUtil.loadX509FromPath(MERCHANT_CERTIFICATE_PATH);
    assertNotNull(certificate);
  }

  @Test
  public void testLoadX509FromString() {
    X509Certificate certificate = SMPemUtil.loadX509FromString(MERCHANT_CERTIFICATE_STRING);
    assertNotNull(certificate);
  }

  @Test
  public void testLoadX509FromStream() {
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(MERCHANT_CERTIFICATE_STRING.getBytes(StandardCharsets.UTF_8));
    X509Certificate certificate = SMPemUtil.loadX509FromStream(inputStream);
    assertNotNull(certificate);
  }
}
