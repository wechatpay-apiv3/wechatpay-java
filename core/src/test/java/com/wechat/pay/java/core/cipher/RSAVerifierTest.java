package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE;
import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_CERTIFICATE_SERIAL_NUMBER;

import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Vector;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RSAVerifierTest {

  private static Verifier rsaVerifier;
  private static final String MESSAGE = "message";
  /** signature为使用RSASigner和测试商户证书私钥对MESSAGE签名得到的结果 */
  private static final SignatureResult SIGNATURE_RESULT =
      new SignatureResult(
          "Rq+5ZpWV6IHTWE1+9gCrYJKguNgbItpMdlIA+PVCPCdUAKNlohZz7CL7Y8xWDIFJjdGnErMj6Z+Q/h9CF5MV+nAVkqDmBR0uP5pp"
              + "sUEzco4ijvKHsvI2ro4boSW6vSfQs3W8I4QSI9a0/NFIgvj83Fv27LIYZf7deaFWmEwd1fpAeJRCyoKyBeJPdsjVx0//Lqe6"
              + "Wze2zVdliLcBvaHCfpd8+rAVGS+FQDeWgHqJ7n/Zy09LmV1/ghs7pO1mdjdD16SHKlysAD3K5hNGUJX/SHrEXEcu1yO1AOb/"
              + "YvOVkjNh8243rcfXNE5L4ph2XS11vS+2aToKYSOcKUagf0LLaw==",
          MERCHANT_CERTIFICATE_SERIAL_NUMBER);

  @BeforeClass
  public static void init() {
    List<X509Certificate> list = new Vector<>();
    list.add(MERCHANT_CERTIFICATE);
    rsaVerifier = new RSAVerifier(new InMemoryCertificateProvider(list));
  }

  @Test
  public void testVerify() {
    Assert.assertTrue(
        rsaVerifier.verify(
            MERCHANT_CERTIFICATE_SERIAL_NUMBER, MESSAGE, SIGNATURE_RESULT.getSign()));
  }
}
