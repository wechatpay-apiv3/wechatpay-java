package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.model.TestConfig.WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER;

import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.RequestParam.Builder;
import com.wechat.pay.java.core.util.GsonUtil;
import com.wechat.pay.java.core.util.IOUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NotificationParserTest {

  private static final String SIGN_TYPE = "WECHATPAY2-SHA256-RSA2048";
  private static final String TRANSFORMATION = "AES/GCM/NoPadding";
  private static final String DECRYPT_OBJECT_STRING = "{\"decrypt-key\":\"decrypt-value\"}";
  private static final String SIGNATURE =
      "tJHIiIS9eB2hAYstmAmbbD3ZE5LiIm/Ug5tuL4fC0YOFRWIHV39UFIZXC0e9Wl6lBu6sKvkqDkzpqzBsVHyXFlbYZTOQrVdG4b6LfTnK4m"
          + "ikv9++ixJMd3vTf2yCqvBkh98zs3Ds5zsYQakzbcwhmw4fMJs4nPLws28H0UW9FjDR//rxELLwXvV1VEA1IBLX70xptjL8hr"
          + "fUjEE8kkry6yNJTHZRU8CAc7qHli2Ng1V1qb9ARbK8A3ThmFmPQvRGrapI/jS2laKKgYUmfdEdkNO6B2Cke5e8VTxY406ArA"
          + "mQ90GAihDwIcb16TQMnzCMBoutnwZKNiKRACrFmtxw2Q==";
  private static final String NOTIFICATION_JSON_PATH =
      System.getProperty("user.dir")
          + "/src/test/resources/notification"
          + "/wechat_pay_notification.json";
  private static String notificationJson;
  private static Notification notification;

  private static String verifyAlgorithm;
  private static String verifyNonce;
  private static final int KEY_LENGTH_BIT = 256;
  private static final byte[] KEY =
      "4EYG13V2Uz15h0JrAd8tk5k35Vlfn8c5".getBytes(StandardCharsets.UTF_8);
  private static final long TIMESTAMP = Instant.now().getEpochSecond();

  @BeforeClass
  public static void init() throws IOException {}

  @Test
  public void testParse() throws IOException {
    notificationJson = IOUtil.loadStringFromPath(NOTIFICATION_JSON_PATH);
    notification = GsonUtil.getGson().fromJson(notificationJson, Notification.class);
    verifyAlgorithm = notification.getResource().getAlgorithm();
    verifyNonce = notification.getResource().getNonce();

    AeadCipher fakeAeadCipher =
        new AeadCipher() {
          @Override
          public String encrypt(byte[] associatedData, byte[] nonce, byte[] plaintext) {
            return "fake-ciphertext";
          }

          @Override
          public String decrypt(byte[] associatedData, byte[] nonce, byte[] ciphertext) {
            Assert.assertArrayEquals(
                notification.getResource().getAssociatedData().getBytes(StandardCharsets.UTF_8),
                associatedData);
            Assert.assertArrayEquals(verifyNonce.getBytes(StandardCharsets.UTF_8), nonce);
            Assert.assertArrayEquals(
                Base64.getDecoder().decode(notification.getResource().getCiphertext()), ciphertext);
            return DECRYPT_OBJECT_STRING;
          }
        };
    Verifier fakeVerifier =
        (serialNumber, message, signature) -> {
          Assert.assertEquals(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER, serialNumber);
          String verifyMessage = TIMESTAMP + "\n" + verifyNonce + "\n" + notificationJson + "\n";
          Assert.assertEquals(verifyMessage, message);
          Assert.assertEquals(SIGNATURE, signature);
          return true;
        };

    Map<String, Verifier> verifiers = new HashMap<>();
    Map<String, AeadCipher> ciphers = new HashMap<>();
    verifiers.put(SIGN_TYPE, fakeVerifier);
    ciphers.put(verifyAlgorithm, fakeAeadCipher);
    NotificationParser parser = new NotificationParser(verifiers, ciphers);
    RequestParam requestParam =
        new Builder()
            .serialNumber(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .nonce(verifyNonce)
            .signature(SIGNATURE)
            .signType(SIGN_TYPE)
            .timestamp(String.valueOf(TIMESTAMP))
            .body(notificationJson)
            .build();

    Object decryptObject = parser.parse(requestParam, Object.class);
    Assert.assertEquals(DECRYPT_OBJECT_STRING, GsonUtil.getGson().toJson(decryptObject));
  }

  @Test(expected = ValidationException.class)
  public void testNullSignature() {
    Map<String, Verifier> verifiers = new HashMap<>();
    Map<String, AeadCipher> ciphers = new HashMap<>();
    NotificationParser parser = new NotificationParser(verifiers, ciphers);
    RequestParam requestParam =
        new Builder()
            .serialNumber(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .nonce(verifyNonce)
            .timestamp(String.valueOf(TIMESTAMP))
            .body(notificationJson)
            .build();
    parser.parse(requestParam, Object.class);
  }

  @Test(expected = ValidationException.class)
  public void testNullSerialNumber() {
    Map<String, Verifier> verifiers = new HashMap<>();
    Map<String, AeadCipher> ciphers = new HashMap<>();
    NotificationParser parser = new NotificationParser(verifiers, ciphers);
    RequestParam requestParam =
        new Builder()
            .nonce(verifyNonce)
            .signature(SIGNATURE)
            .timestamp(String.valueOf(TIMESTAMP))
            .body(notificationJson)
            .build();
    parser.parse(requestParam, Object.class);
  }

  @Test(expected = ValidationException.class)
  public void testNoVerifier() {
    Map<String, Verifier> verifiers = new HashMap<>();
    Map<String, AeadCipher> ciphers = new HashMap<>();
    NotificationParser parser = new NotificationParser(verifiers, ciphers);
    RequestParam requestParam =
        new Builder()
            .serialNumber(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .nonce(verifyNonce)
            .signature(SIGNATURE)
            .signType(SIGN_TYPE)
            .timestamp(String.valueOf(TIMESTAMP))
            .body(notificationJson)
            .build();
    parser.parse(requestParam, Object.class);
  }

  @Test(expected = ValidationException.class)
  public void testNoRequestParam() {
    Map<String, Verifier> verifiers = new HashMap<>();
    Map<String, AeadCipher> ciphers = new HashMap<>();
    NotificationParser parser = new NotificationParser(verifiers, ciphers);
    parser.parse(null, Object.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithNullConfig() {
    new NotificationParser();
  }

  @Test(expected = Test.None.class)
  public void testConstructWithConfig() {
    NotificationParser parser =
        new NotificationParser(
            new NotificationConfig() {
              @Override
              public String getSignType() {
                return "fake-sign-type";
              }

              @Override
              public String getCipherType() {
                return "fake-cipher-type";
              }

              @Override
              public Verifier createVerifier() {
                return (serialNumber, message, signature) -> false;
              }

              @Override
              public AeadCipher createAeadCipher() {
                return new AeadAesCipher("test".getBytes(StandardCharsets.UTF_8));
              }
            });
  }

  @Test(expected = MalformedMessageException.class)
  public void testInvalidAlgorithm() throws IOException {
    notificationJson = IOUtil.loadStringFromPath(NOTIFICATION_JSON_PATH);
    notification = GsonUtil.getGson().fromJson(notificationJson, Notification.class);
    verifyAlgorithm = notification.getResource().getAlgorithm();
    verifyNonce = notification.getResource().getNonce();
    AeadCipher fakeAeadCipher =
        new AeadCipher() {
          @Override
          public String encrypt(byte[] associatedData, byte[] nonce, byte[] plaintext) {
            return "fake-ciphertext";
          }

          @Override
          public String decrypt(byte[] associatedData, byte[] nonce, byte[] ciphertext) {
            return DECRYPT_OBJECT_STRING;
          }
        };
    Verifier fakeVerifier = (serialNumber, message, signature) -> true;
    Map<String, Verifier> verifiers = new HashMap<>();
    Map<String, AeadCipher> ciphers = new HashMap<>();
    verifiers.put(SIGN_TYPE, fakeVerifier);
    ciphers.put(verifyAlgorithm, fakeAeadCipher);
    NotificationParser parser = new NotificationParser(verifiers, ciphers);
    String body = "{\"resource\":{}}";
    RequestParam requestParam =
        new Builder()
            .serialNumber(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .nonce(verifyNonce)
            .signature(SIGNATURE)
            .timestamp(String.valueOf(TIMESTAMP))
            .body(body)
            .build();
    parser.parse(requestParam, Object.class);
  }

  @Test(expected = ValidationException.class)
  public void testVerifyFail() throws IOException {
    notificationJson = IOUtil.loadStringFromPath(NOTIFICATION_JSON_PATH);
    notification = GsonUtil.getGson().fromJson(notificationJson, Notification.class);
    verifyAlgorithm = notification.getResource().getAlgorithm();
    verifyNonce = notification.getResource().getNonce();

    AeadCipher fakeAeadCipher =
        new AeadCipher() {
          @Override
          public String encrypt(byte[] associatedData, byte[] nonce, byte[] plaintext) {
            return "fake-ciphertext";
          }

          @Override
          public String decrypt(byte[] associatedData, byte[] nonce, byte[] ciphertext) {
            return DECRYPT_OBJECT_STRING;
          }
        };
    Verifier fakeVerifier = (serialNumber, message, signature) -> false;
    Map<String, Verifier> verifiers = new HashMap<>();
    Map<String, AeadCipher> ciphers = new HashMap<>();
    verifiers.put(SIGN_TYPE, fakeVerifier);
    ciphers.put(verifyAlgorithm, fakeAeadCipher);
    NotificationParser parser = new NotificationParser(verifiers, ciphers);
    RequestParam requestParam =
        new Builder()
            .serialNumber(WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER)
            .nonce(verifyNonce)
            .signature(SIGNATURE)
            .timestamp(String.valueOf(TIMESTAMP))
            .body(notificationJson)
            .build();
    parser.parse(requestParam, Object.class);
  }
}
