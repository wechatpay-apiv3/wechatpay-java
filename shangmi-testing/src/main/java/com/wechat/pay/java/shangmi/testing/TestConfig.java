package com.wechat.pay.java.shangmi.testing;

import com.wechat.pay.java.core.util.IOUtil;
import java.io.IOException;

public class TestConfig {
    public static final String MERCHANT_PRIVATE_KEY_PATH;
    public static final String MERCHANT_CERTIFICATE_PATH;
    public static final String MERCHANT_PRIVATE_KEY_STRING;
    public static final String MERCHANT_CERTIFICATE_STRING;
    public static final String WECHAT_PAY_CERTIFICATE_PATH;
    public static final String WECHAT_PAY_CERTIFICATE_STRING;
    public static final String MERCHANT_CERTIFICATE_SERIAL_NUMBER;
    public static final String MERCHANT_ID;
    public static final String API_V3_KEY = "a7cde1ZJB1kG2e7VfTs3jQzaWizur8Gb";

    static {
        try {
            ClassLoader classLoader = TestConfig.class.getClassLoader();
            MERCHANT_PRIVATE_KEY_PATH = classLoader.getResource("merchant_private_key.pem").getPath();
            MERCHANT_CERTIFICATE_PATH = classLoader.getResource("merchant_certificate.pem").getPath();
            WECHAT_PAY_CERTIFICATE_PATH = classLoader.getResource("wechat_pay_certificate.pem").getPath();
            MERCHANT_PRIVATE_KEY_STRING = IOUtil.loadStringFromPath(MERCHANT_PRIVATE_KEY_PATH);
            MERCHANT_CERTIFICATE_STRING = IOUtil.loadStringFromPath(MERCHANT_CERTIFICATE_PATH);
            MERCHANT_CERTIFICATE_SERIAL_NUMBER = "54E7F59ED8DF4570CFB14780EA8152D45A675EE6";
            WECHAT_PAY_CERTIFICATE_STRING = IOUtil.loadStringFromPath(WECHAT_PAY_CERTIFICATE_PATH);
            MERCHANT_ID = "1234567891";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

