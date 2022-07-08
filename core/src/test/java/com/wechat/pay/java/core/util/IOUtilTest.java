package com.wechat.pay.java.core.util;

import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_PATH;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_STRING;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Assert;
import org.junit.Test;

public class IOUtilTest {

  private static final String TEXT = "hello world";

  @Test
  public void testInputStreamToByteArray() throws IOException {
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(TEXT.getBytes(StandardCharsets.UTF_8));
    byte[] bytes = IOUtil.toByteArray(inputStream);
    Assert.assertArrayEquals(bytes, TEXT.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testInputStreamToString() throws IOException {
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(TEXT.getBytes(StandardCharsets.UTF_8));
    String text = IOUtil.toString(inputStream);
    Assert.assertEquals(TEXT, text);
  }

  @Test
  public void testLoadStringFromPath() throws IOException {
    String merchantCertificateString = IOUtil.loadStringFromPath(MERCHANT_CERTIFICATE_PATH);
    Assert.assertEquals(MERCHANT_CERTIFICATE_STRING, merchantCertificateString);
  }
}
