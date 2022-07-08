package com.wechat.pay.java.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Assert;
import org.junit.Test;

public class ShaUtilTest {

  private static final String MESSAGE = "message";

  @Test
  public void testInputStreamGetSha1HexString() throws IOException {
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(MESSAGE.getBytes(StandardCharsets.UTF_8));
    String sha = ShaUtil.getSha1HexString(inputStream);
    Assert.assertNotNull(sha);
  }

  @Test
  public void testInputStreamGetSha256HexString() throws IOException {
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(MESSAGE.getBytes(StandardCharsets.UTF_8));
    String sha = ShaUtil.getSha256HexString(inputStream);
    Assert.assertNotNull(sha);
  }

  @Test
  public void testBytesGetSha256HexString() {
    String sha = ShaUtil.getSha256HexString(MESSAGE.getBytes(StandardCharsets.UTF_8));
    Assert.assertNotNull(sha);
  }
}
