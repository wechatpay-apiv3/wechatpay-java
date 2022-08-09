package com.wechat.pay.java.core.cipher;

import static com.wechat.pay.java.core.model.TestConfig.MERCHANT_PRIVATE_KEY;

import com.wechat.pay.java.core.exception.DecryptionException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RSAPrivacyDecryptorTest {

  private static RSAPrivacyDecryptor rsaPrivacyDecryptor;
  private static final String CIPHERTEXT =
      "Qqsluo7R1IZyAqdkm/jBzwXDP5VndCwwFbRA+yILFTYciZzrRChQIQ7SKuI2k3YSR5auB3Zsp"
          + "djR1WPB3PTCKe6BCFx3NnaPWhSwonYTmW8ZseXjkbcYSbKL5O/6dr6Zftg3t2A2FUd97acjM1fr2EIvIvRieQ3Hl8qVkDBY9520wm1rz"
          + "vePUnxQWN70RZiBcqr7O7Gb0gU3l7FkvoUFSbY44HgDJetmVow3yGIV3Tcd45o2MNQ+5F1qvjIjb3/6dkGKce4/kNTYraUMO6o6kfFXl"
          + "fg+bFRIiz8hiUpbToWdu7g0R5Hq0/YIE5vw/5Ms4gbk2HIQIfOLkY8sEPjYHA==";
  private static final String PLAINTEXT = "plaintext";

  @BeforeClass
  public static void init() {
    rsaPrivacyDecryptor = new RSAPrivacyDecryptor(MERCHANT_PRIVATE_KEY);
  }

  @Test
  public void testDecrypt() {
    String decryptMsg = rsaPrivacyDecryptor.decrypt(CIPHERTEXT);
    Assert.assertEquals(PLAINTEXT, decryptMsg);
  }

  @Test(expected = DecryptionException.class)
  public void testDecryptFail() {
    rsaPrivacyDecryptor.decrypt("MTIzNA==");
  }
}
