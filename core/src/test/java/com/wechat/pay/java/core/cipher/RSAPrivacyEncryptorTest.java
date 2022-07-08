package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.model.TestConfig.*;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RSAPrivacyEncryptorTest {

  private static RSAPrivacyEncryptor rsaPrivacyEncryptor;
  private static RSAPrivacyDecryptor rsaPrivacyDecryptor;
  private static final String PLAINTEXT = "plaintext";
  private static final String RSA_OAEP = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
  private static final String RSA_PKCS1v15 = "RSA/ECB/PKCS1Padding";

  @BeforeClass
  public static void init() {
    rsaPrivacyEncryptor =
        new RSAPrivacyEncryptor(
            MERCHANT_CERTIFICATE.getPublicKey(), MERCHANT_CERTIFICATE_SERIAL_NUMBER);
    rsaPrivacyDecryptor = new RSAPrivacyDecryptor(MERCHANT_PRIVATE_KEY);
  }

  @Test
  public void testEncrypt() {
    String ciphertext = rsaPrivacyEncryptor.encrypt(PLAINTEXT);
    String decryptMessage = rsaPrivacyDecryptor.decrypt(ciphertext);
    Assert.assertEquals(PLAINTEXT, decryptMessage);
    Assert.assertEquals(
        MERCHANT_CERTIFICATE_SERIAL_NUMBER, rsaPrivacyEncryptor.getWechatpaySerial());
  }

  @Test
  public void testEncryptWithOAEP() {
    String ciphertext = rsaPrivacyEncryptor.encryptWithTransformation(RSA_OAEP, PLAINTEXT);
    String decryptMessage = rsaPrivacyDecryptor.decryptWithTransformation(RSA_OAEP, ciphertext);
    Assert.assertEquals(PLAINTEXT, decryptMessage);
  }

  @Test
  public void testEncryptWithPKCS1v15() {
    String ciphertext = rsaPrivacyEncryptor.encryptWithTransformation(RSA_PKCS1v15, PLAINTEXT);
    String decryptMessage = rsaPrivacyDecryptor.decryptWithTransformation(RSA_PKCS1v15, ciphertext);
    Assert.assertEquals(PLAINTEXT, decryptMessage);
  }
}
