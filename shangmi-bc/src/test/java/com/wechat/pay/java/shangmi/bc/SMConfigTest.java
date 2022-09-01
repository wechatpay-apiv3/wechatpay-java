package com.wechat.pay.java.shangmi.bc;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.shangmi.testing.BaseSMConfigTest;

import static com.wechat.pay.java.shangmi.testing.TestConfig.*;

public class SMConfigTest implements BaseSMConfigTest {

    @Override
    public Config createConfigFromString() {
        return new SMConfig.Builder()
                        .merchantId(MERCHANT_ID)
                        .privateKey(MERCHANT_PRIVATE_KEY_STRING)
                        .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
                        .addWechatPayCertificate(WECHAT_PAY_CERTIFICATE_STRING)
                        .build();
    }

    @Override
    public Config createConfigFromPath() {
        return new SMConfig.Builder()
                .merchantId(MERCHANT_ID)
                .privateKeyFromPath(MERCHANT_PRIVATE_KEY_PATH)
                .merchantSerialNumber(MERCHANT_CERTIFICATE_SERIAL_NUMBER)
                .addWechatPayCertificateFromPath(WECHAT_PAY_CERTIFICATE_PATH)
                .build();
    }
}
