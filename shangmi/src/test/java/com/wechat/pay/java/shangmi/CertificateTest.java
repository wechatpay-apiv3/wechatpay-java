package com.wechat.pay.java.shangmi;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.service.certificate.CertificateService;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("use for manual test")
class CertificateTest {
  final String merchantId = "";
  final String privateKey = "";
  final String merchantSerialNo = "";
  final String wechatPayCertificate = "";
  final String apiV3Key = "";

  SMConfig config;

  @BeforeEach
  void init() {
    config =
        new SMConfig.Builder()
            .merchantId(merchantId)
            .merchantSerialNumber(merchantSerialNo)
            .privateKey(privateKey)
            .addWechatPayCertificate(wechatPayCertificate)
            .build();
  }

  @Test
  void testDownloadCertificate() {
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader("Accept", "application/json");

    CertificateService service = new CertificateService.Builder().config(config).build();
    AeadCipher aeadCipher = new AeadSM4Cipher(apiV3Key.getBytes(StandardCharsets.UTF_8));
    assertDoesNotThrow(
        () -> {
          List<X509Certificate> l =
              service.downloadCertificate(
                  "https://api.mch.weixin.qq.com/v3/certificates?algorithm_type=SM2",
                  aeadCipher,
                  SMPemUtil::loadX509FromString);
        });
  }
}
