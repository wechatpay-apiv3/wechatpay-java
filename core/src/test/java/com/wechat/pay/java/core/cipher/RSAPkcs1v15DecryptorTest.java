package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY;
import static org.junit.jupiter.api.Assertions.*;

import com.wechat.pay.java.core.exception.DecryptionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RSAPkcs1v15DecryptorTest {

  private static RSAPkcs1v15Decryptor rsaPkcs1v15Decryptor;

  @BeforeAll
  public static void init() {
    rsaPkcs1v15Decryptor = new RSAPkcs1v15Decryptor(MERCHANT_PRIVATE_KEY);
  }

  @Test
  void testDecrypt() {
    String ciphertext =
        "ckMdMJormpuX4nBmmhiDJqFBqPsrFIi6uLQEY5s8+Dbr91q2kC29yfghp97zG/BCo6KBGp+Blh+lIPg4/WQuQiCxJ5ZkwWvxtVd1TO7UkZVpgrW9cxDGZP28Rn5pbSMqeGA4rKMROJDblb+KBSCu/Nkvejo3yxoFDasf8ehW7rrXR21eQq1SgbvO/uUR+c81I3fUr5gO+gKgV9aKn8kTgqIRTprZkUE5CE6hvk3ybqAP7gRXAJsMGpHeoxr2uuYDBOhCeXPpRG/6ypkLd52bYOWXFHdd68GWWhN3OGXpdPUwaaJafEW4vnYTMhkacyicxVH3AtWLcsOKJoylIxjFHQ==";
    assertEquals("plaintext", rsaPkcs1v15Decryptor.decrypt(ciphertext));
  }

  @Test
  void testDecryptFail() {

    assertThrows(DecryptionException.class, () -> rsaPkcs1v15Decryptor.decrypt("MTIzNA=="));
  }
}
