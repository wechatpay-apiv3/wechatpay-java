package com.wechat.pay.java.core;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public interface ConfigTest {

  Config createConfig();

  @Test
  default void testCreateEncryptor() {
    assertNotNull(createConfig().createEncryptor());
  }

  @Test
  default void testCreateDecryptor() {
    assertNotNull(createConfig().createDecryptor());
  }

  @Test
  default void testCreateCredential() {
    assertNotNull(createConfig().createCredential());
  }

  @Test
  default void testCreateValidator() {
    assertNotNull(createConfig().createValidator());
  }
}
