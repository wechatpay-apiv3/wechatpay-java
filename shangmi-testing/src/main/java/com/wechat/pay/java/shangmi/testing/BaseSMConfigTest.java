package com.wechat.pay.java.shangmi.testing;

import com.wechat.pay.java.core.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public interface BaseSMConfigTest {

    Config createConfigFromString();
    Config createConfigFromPath();

    @Test
    default void testBuildConfigFromString() {
        Config c = createConfigFromString();

        assertNotNull(c);
        assertNotNull(c.createCredential());
        assertNotNull(c.createValidator());
        assertNotNull(c.createDecryptor());
        assertNotNull(c.createEncryptor());
    }

    @Test
    default void testBuildConfigFromPath() {
        Config c = createConfigFromPath();

        assertNotNull(c);
        assertNotNull(c.createCredential());
        assertNotNull(c.createValidator());
        assertNotNull(c.createDecryptor());
        assertNotNull(c.createEncryptor());
    }
}
