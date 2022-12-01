package com.wechat.pay.java.service.certificate;

import static com.wechat.pay.java.core.http.Constant.REQUEST_ID;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.cipher.AbstractAeadCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.util.IOUtil;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.util.List;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CertificateServiceTest {

  private static final String RESOURCES_DIR =
      System.getProperty("user.dir") + "/src/test/resources";
  private static final String CERTIFICATE_SERVICE_MERCHANT_ID = "1234567891";
  private static Config certificateServiceConfig;

  @BeforeClass
  public static void init() {
    certificateServiceConfig =
        new Config() {
          @Override
          public PrivacyEncryptor createEncryptor() {
            return new PrivacyEncryptor() {
              @Override
              public String encrypt(String plaintext) {
                return "fake-ciphertext";
              }

              @Override
              public String getWechatpaySerial() {
                return "5157F09EFDC096DE15EBE81A47057A7232F1B8E1";
              }
            };
          }

          @Override
          public PrivacyDecryptor createDecryptor() {
            return ciphertext -> "fake-plaintext";
          }

          @Override
          public Credential createCredential() {
            return new Credential() {
              @Override
              public String getSchema() {
                return "fake-schema";
              }

              @Override
              public String getMerchantId() {
                return CERTIFICATE_SERVICE_MERCHANT_ID;
              }

              @Override
              public String getAuthorization(URI uri, String httpMethod, String signBody) {
                return "fake-authorization";
              }
            };
          }

          @Override
          public Validator createValidator() {
            return new Validator() {
              @Override
              public <T> boolean validate(HttpHeaders responseHeaders, String body) {
                return true;
              }
            };
          }

          @Override
          public Signer createSigner() {
            return null;
          }
        };
  }

  @Test
  public void downloadCertificateTest() {
    OkHttpClient.Builder okHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  Headers headers = chain.request().headers();
                  Assert.assertEquals(chain.request().method(), HttpMethod.GET.name());
                  Assert.assertEquals(
                      "https://" + HostName.APIHK.getValue() + "/v3/certificates",
                      chain.request().url().url().toString());
                  Assert.assertEquals("*/*", headers.get(Constant.ACCEPT));
                  Assert.assertEquals(
                      com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON.getValue(),
                      headers.get(Constant.CONTENT_TYPE));
                  Assert.assertNull(chain.request().body());

                  String mockDownloadCertificateResponsePath =
                      RESOURCES_DIR + "/certificate/download_certificate_response.json";
                  String jsonBody =
                      IOUtil.toString(
                          Files.newInputStream(Paths.get(mockDownloadCertificateResponsePath)));

                  return new Response.Builder()
                      .request(chain.request())
                      .code(200)
                      .header(REQUEST_ID, "fake-request-id")
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              jsonBody,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient httpClient =
        new DefaultHttpClientBuilder()
            .okHttpClient(okHttpClientBuilder.build())
            .credential(certificateServiceConfig.createCredential())
            .validator(certificateServiceConfig.createValidator())
            .build();
    String algorithm = "fake-algorithm";
    String transformation = "fake-transformation";
    int keyLengthBit = 24;
    byte[] key = "key".getBytes(StandardCharsets.UTF_8);
    String mockWechatPayCertificatePath = RESOURCES_DIR + "/certificate/wechat_pay_certificate.pem";

    AeadCipher fakeAeadCipher =
        new AbstractAeadCipher(algorithm, transformation, keyLengthBit, key) {
          @Override
          public String encrypt(byte[] associatedData, byte[] nonce, byte[] plaintext) {
            return "fake-ciphertext";
          }

          @Override
          public String decrypt(byte[] associatedData, byte[] nonce, byte[] ciphertext) {
            try {
              return IOUtil.loadStringFromPath(mockWechatPayCertificatePath);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
        };

    CertificateService certificateService =
        new CertificateService.Builder().httpClient(httpClient).hostName(HostName.APIHK).build();
    List<X509Certificate> certificateList = certificateService.downloadCertificate(fakeAeadCipher);
    Assert.assertNotNull(certificateList);
  }
}
