package com.wechat.pay.java.core.util;

import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_STRING;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY_STRING;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import org.junit.Assert;
import org.junit.Test;

public class PemUtilTest {

  @Test
  public void testLoadPrivateKeyFromString() {
    PrivateKey privateKey = PemUtil.loadPrivateKeyFromString(MERCHANT_PRIVATE_KEY_STRING);
    Assert.assertNotNull(privateKey);
  }

  @Test
  public void testLoadPrivateKeyFromPath() {
    PrivateKey privateKey = PemUtil.loadPrivateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH);
    Assert.assertNotNull(privateKey);
  }

  @Test
  public void testLoadX509FromPath() {
    X509Certificate certificate = PemUtil.loadX509FromPath(MERCHANT_CERTIFICATE_PATH);
    Assert.assertNotNull(certificate);
  }

  @Test
  public void testLoadX509FromString() {
    X509Certificate certificate = PemUtil.loadX509FromString(MERCHANT_CERTIFICATE_STRING);
    Assert.assertNotNull(certificate);
  }

  @Test
  public void testLoadX509FromStream() {
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(MERCHANT_CERTIFICATE_STRING.getBytes(StandardCharsets.UTF_8));
    X509Certificate certificate = PemUtil.loadX509FromStream(inputStream);
    Assert.assertNotNull(certificate);
  }

  @Test
  public void testLoadPrivateKeyWithProvider() {
    PrivateKey privateKey =
        PemUtil.loadPrivateKeyFromString(MERCHANT_PRIVATE_KEY_STRING, "RSA", "SunRsaSign");
    Assert.assertNotNull(privateKey);
  }

  @Test
  public void testLoadPrivFromPathWithProv() {
    PrivateKey privateKey =
        PemUtil.loadPrivateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH, "RSA", "SunRsaSign");
    Assert.assertNotNull(privateKey);
  }

  @Test
  public void testLoadX509FromPathWithProv() {
    X509Certificate certificate = PemUtil.loadX509FromPath(MERCHANT_CERTIFICATE_PATH, "SUN");
    Assert.assertNotNull(certificate);
  }

  @Test
  public void testLoadX509FromStringWithProv() {
    X509Certificate certificate = PemUtil.loadX509FromString(MERCHANT_CERTIFICATE_STRING, "SUN");
    Assert.assertNotNull(certificate);
  }
}
