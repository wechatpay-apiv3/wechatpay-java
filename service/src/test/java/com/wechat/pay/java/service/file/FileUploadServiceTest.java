package com.wechat.pay.java.service.file;

import static com.wechat.pay.java.core.http.Constant.REQUEST_ID;

import com.google.gson.Gson;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.util.GsonUtil;
import com.wechat.pay.java.core.util.IOUtil;
import com.wechat.pay.java.core.util.ShaUtil;
import com.wechat.pay.java.service.file.model.FileUploadResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
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

public class FileUploadServiceTest {

  private static final String FILE_SERVICE_MOCK_DATA_DIR =
      System.getProperty("user.dir") + "/src/test/resources/file";
  private static final String FILE_UPLOAD_SERVICE_MERCHANT_ID = "1234567891";
  private static Config fileUploadServiceConfig;

  @BeforeClass
  public static void init() {
    fileUploadServiceConfig =
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
                return FILE_UPLOAD_SERVICE_MERCHANT_ID;
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
  public void testUploadImage() throws IOException {
    String imagePath = FILE_SERVICE_MOCK_DATA_DIR + "/test_image.jpg";
    String imageUploadPath = "https://api.mch.weixin.qq.com/v3/merchant/media/upload";
    File image = new File(imagePath);
    byte[] imageBytes = IOUtil.toByteArray(Files.newInputStream(image.toPath()));
    String imageSha256 = ShaUtil.getSha256HexString(imageBytes);
    String imageMeta =
        String.format("{\"filename\":\"%s\",\"sha256\":\"%s\"}", image.getName(), imageSha256);
    String mockUploadImageResponsePath = FILE_SERVICE_MOCK_DATA_DIR + "/upload_image_response.json";
    String responseBodyJson =
        IOUtil.toString(Files.newInputStream(Paths.get(mockUploadImageResponsePath)));

    OkHttpClient.Builder builder =
        new Builder()
            .addInterceptor(
                chain -> {
                  Headers headers = chain.request().headers();
                  Assert.assertEquals(chain.request().method(), HttpMethod.POST.name());
                  Assert.assertEquals(imageUploadPath, chain.request().url().url().toString());
                  Assert.assertEquals("*/*", headers.get(Constant.ACCEPT));
                  Assert.assertEquals(
                      com.wechat.pay.java.core.http.MediaType.MULTIPART_FORM_DATA.getValue(),
                      headers.get(Constant.CONTENT_TYPE));

                  return new Response.Builder()
                      .request(chain.request())
                      .code(200)
                      .header(REQUEST_ID, "fake-request-id")
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              responseBodyJson,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient httpClient =
        new DefaultHttpClientBuilder()
            .credential(fileUploadServiceConfig.createCredential())
            .validator(fileUploadServiceConfig.createValidator())
            .okHttpClient(builder.build())
            .build();
    FileUploadService fileService = new FileUploadService.Builder().httpClient(httpClient).build();
    FileUploadResponse fileUploadResponse =
        fileService.uploadImage(imageUploadPath, imageMeta, imagePath);
    Gson gson = GsonUtil.getGson();
    Assert.assertEquals(
        gson.fromJson(responseBodyJson, FileUploadResponse.class).toString(),
        fileUploadResponse.toString());
  }

  @Test
  public void testUploadVideo() throws IOException {
    String videoPath = FILE_SERVICE_MOCK_DATA_DIR + "/test_video.mp4";
    String videoUploadPath = "https://api.mch.weixin.qq.com/v3/merchant/media/video_upload";
    File video = new File(videoPath);
    byte[] videoBytes = IOUtil.toByteArray(Files.newInputStream(video.toPath()));
    String videoSha256 = ShaUtil.getSha256HexString(videoBytes);
    String videoMeta =
        String.format("{\"filename\":\"%s\",\"sha256\":\"%s\"}", video.getName(), videoSha256);
    String mockUploadImageResponsePath = FILE_SERVICE_MOCK_DATA_DIR + "/upload_image_response.json";
    String responseBodyJson =
        IOUtil.toString(Files.newInputStream(Paths.get(mockUploadImageResponsePath)));

    OkHttpClient.Builder uploadVideoOkHttpClientBuilder =
        new Builder()
            .addInterceptor(
                chain -> {
                  Headers headers = chain.request().headers();
                  Assert.assertEquals(chain.request().method(), HttpMethod.POST.name());
                  Assert.assertEquals(videoUploadPath, chain.request().url().url().toString());
                  Assert.assertEquals("*/*", headers.get(Constant.ACCEPT));
                  Assert.assertEquals(
                      com.wechat.pay.java.core.http.MediaType.MULTIPART_FORM_DATA.getValue(),
                      headers.get(Constant.CONTENT_TYPE));

                  return new Response.Builder()
                      .request(chain.request())
                      .code(200)
                      .header(REQUEST_ID, "fake-request-id")
                      .message("ok")
                      .protocol(Protocol.HTTP_1_1)
                      .body(
                          ResponseBody.create(
                              responseBodyJson,
                              MediaType.parse(
                                  com.wechat.pay.java.core.http.MediaType.APPLICATION_JSON
                                      .getValue())))
                      .build();
                });

    HttpClient httpClient =
        new DefaultHttpClientBuilder()
            .credential(fileUploadServiceConfig.createCredential())
            .validator(fileUploadServiceConfig.createValidator())
            .okHttpClient(uploadVideoOkHttpClientBuilder.build())
            .build();
    FileUploadService fileService = new FileUploadService.Builder().httpClient(httpClient).build();
    FileUploadResponse fileUploadResponse =
        fileService.uploadVideo(videoUploadPath, videoMeta, videoPath);
    Gson gson = GsonUtil.getGson();
    Assert.assertEquals(
        gson.fromJson(responseBodyJson, FileUploadResponse.class).toString(),
        fileUploadResponse.toString());
  }
}
