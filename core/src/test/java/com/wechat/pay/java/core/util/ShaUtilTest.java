package com.wechat.pay.java.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Assert;
import org.junit.Test;

public class ShaUtilTest {

  private static final String MESSAGE = "message";
  private static final String SHA1 = "6f9b9af3cd6e8b8a73c2cdced37fe9f59226e27d";
  private static final String SHA256 =
      "ab530a13e45914982b79f9b7e3fba994cfd1f3fb22f71cea1afbf02b460c6d1d";

  @Test
  public void testInputStreamGetSha1HexString() throws IOException {
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(MESSAGE.getBytes(StandardCharsets.UTF_8));
    String sha = ShaUtil.getSha1HexString(inputStream);
    Assert.assertEquals(SHA1, sha);
  }

  @Test
  public void testInputStreamGetSha256HexString() throws IOException {
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(MESSAGE.getBytes(StandardCharsets.UTF_8));
    String sha = ShaUtil.getSha256HexString(inputStream);
    Assert.assertEquals(SHA256, sha);
  }

  @Test
  public void testBytesGetSha256HexString() {
    String sha = ShaUtil.getSha256HexString(MESSAGE.getBytes(StandardCharsets.UTF_8));
    Assert.assertEquals(SHA256, sha);
  }

  @Test
  public void testBytesGetSha1HexString() {
    String sha = ShaUtil.getSha1HexString(MESSAGE.getBytes(StandardCharsets.UTF_8));
    Assert.assertEquals(SHA1, sha);
  }
}
