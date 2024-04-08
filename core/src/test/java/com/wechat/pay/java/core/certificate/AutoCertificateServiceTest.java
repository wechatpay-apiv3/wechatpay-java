package com.wechat.pay.java.core.certificate;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.util.PemUtil;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AutoCertificateServiceTest {

  // 因为每个任务都在后台运行,所以需要mock服务一直存在
  static MockWebServer server = new MockWebServer();

  static BlockingQueue<MockResponse> newUpdateQueue() {
    BlockingQueue<MockResponse> queue = new LinkedBlockingQueue<>();
    queue.add(
        new MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(
                "{\n"
                    + "    \"data\": [\n"
                    + "        {\n"
                    + "            \"serial_no\": \"39FACCC173F3485C76C61FD5C0D662AE4A838AA9\",\n"
                    + "            \"effective_time \": \"2018-06-08T10:34:56+08:00\",\n"
                    + "            \"expire_time \": \"2018-12-08T10:34:56+08:00\",\n"
                    + "            \"encrypt_certificate\": {\n"
                    + "                \"algorithm\": \"AEAD_AES_256_GCM\",\n"
                    + "                \"nonce\": \"61f9c719728a\",\n"
                    + "                \"associated_data\": \"certificate\",\n"
                    + "                \"ciphertext\": "
                    + "\"LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUVoRENDQTJ5Z0F3SUJBZ0lVT2ZyTXdYUHpTRngyeGgvVndOWmlya3FEaXFrd0RRWUpLb1pJaHZjTkFRRUYKQlFBd1JqRWJNQmtHQTFVRUF3d1NWR1Z1Y0dGNUxtTnZiU0JWYzJWeUlFTkJNUkl3RUFZRFZRUUxEQWxEUVNCRApaVzUwWlhJeEV6QVJCZ05WQkFvTUNsUmxibkJoZVM1amIyMHdIaGNOTWpFeE1URXhNREkxT0RRMldoY05Nall4Ck1URXdNREkxT0RRMldqQ0JsVEVZTUJZR0ExVUVBd3dQVkdWdWNHRjVMbU52YlNCemFXZHVNU1V3SXdZSktvWkkKaHZjTkFRa0JGaFp6ZFhCd2IzSjBRSE42YVhSeWRYTXVZMjl0TG1OdU1SMHdHd1lEVlFRTERCUlVaVzV3WVhrdQpZMjl0SUVOQklFTmxiblJsY2pFVE1CRUdBMVVFQ2d3S1ZHVnVjR0Y1TG1OdmJURVJNQThHQTFVRUJ3d0lVMmhsCmJscG9aVzR4Q3pBSkJnTlZCQVlUQWtOT01JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0MKQVFFQXY4Rkhvc0Z1QTJodWxvVHplUHRBV0dkWnE3Q2o0RDgzeE45UURManhGTktuR2h6enZJMVpZL2J1bVVrVwpFRzAxc1Z5dEdaRFlhWUd4ZlJlcTdTSVN0SUlWQ0ZORkdwMGYzMDkwQmp5TDliNlZka0NsVWE5Vk8rU0VTZnJqCldhVmgxczBSb1NHMlAwd3FKYlMrQ240REZrM3JLN1NWZEJnQjJJVTBTY1FBOERBL0J0K2hTK2xscDhld0RwRHkKbkRIVUNaV0xGY29TNWxDa1BkNWxneE9qNzV0K3pDSHhXMkFKMEFPS0FHTVZ5MGRVc3JGMzA4KzZBTHJuR0JLRQp3ckNHc1ZWOGp0eEtEMFZKa05LSXVrUDh4S0Z2emRuMDRIM3hFYTE2YUl0ZmRHemt2WFg4S1FVeVBYWDY1Z28rCk50MmYxVGNRZzVNdE9CRUwxNi92RTlmb3dRSURBUUFCbzRJQkdEQ0NBUlF3Q1FZRFZSMFRCQUl3QURBTEJnTlYKSFE4RUJBTUNCc0F3VHdZSUt3WUJCUVVIQVFFRVF6QkJNRDhHQ0NzR0FRVUZCekFDaGpOdlkzTndMR2gwZEhBNgpMeTlaYjNWeVgxTmxjblpsY2w5T1lXMWxPbEJ2Y25RdlZHOXdRMEV2Ykc5a2NGOUNZWE5sUkU0d2FRWURWUjBmCkJHSXdZREJlb0Z5Z1dvWllhSFIwY0Rvdkx6a3VNVGt1TVRZeExqUTZPREE0TUM5VWIzQkRRUzl3ZFdKc2FXTXYKYVhSeWRYTmpjbXcvUTBFOU16bENORGszUVVKRE9FRkZPRGcxTnpRMVFrWTFOamd4UlRSR01ETkNPRUkyTkRkRwpNamhGUVRBZkJnTlZIU01FR0RBV2dCUk9jODA1dHZ1cEYvak9pWWFwY3ZTa2x2UHJMakFkQmdOVkhRNEVGZ1FVCkdUTEU2YnhudG5obnBobFBOd1FBNkw3SGpMc3dEUVlKS29aSWh2Y05BUUVGQlFBRGdnRUJBR3ovWnRVTlZKWEYKUVZ1TGU5MFFGUCtkNmRQUjhaYmJscTVMcXRMYW1xcmFmclF5R0ZOZDZxdEdrY293YXVJM0xKWldPeitTZ0J6YwpjS2dyOXdYb0g1azNSL0pmYVhYU2F3Q3d1NjZIL3grbFlhcU45TXREYkVoVi9Sa0U3dTRwb2I4ZkJSOC9FNTllCnNOQnFmMVdHODhTcUZPbDZ3VDY5ajR0cHBjRVlldjFISCtaelpScDN4U3NJV3dSMHNtV21IOE9BMWZLSFYramsKY21IaFJwNDFTeTQ4bUREdkRuRGVsTU5UaU1wUjk2a1JONmNLWG1LeXZadkNhTkNUOXpyNExVdStMUHdmTFdiWgpBQzVtL1dHclBKakhWMXAyUCt4NmRYaEJybitWV2tyYzZYZTVqeXFmdnpBRVBvN1lsQVFXamh2T2I5d3U4TzNTCjNJVnZrTzNiSkhZPQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0t\"\n"
                    + "            }\n"
                    + "        }\n"
                    + "    ]\n"
                    + "}"));
    // 删除证书A，新增证书B
    queue.add(
        new MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(
                "{\n"
                    + "    \"data\": [\n"
                    + "        {\n"
                    + "            \"serial_no\": \"699AE9A3A00228BFE133FA9A4A99E31D4D2571B4\",\n"
                    + "            \"effective_time \": \"2018-06-08T10:34:56+08:00\",\n"
                    + "            \"expire_time \": \"2018-12-08T10:34:56+08:00\",\n"
                    + "            \"encrypt_certificate\": {\n"
                    + "                \"algorithm\": \"AEAD_AES_256_GCM\",\n"
                    + "                \"nonce\": \"61f9c719728a\",\n"
                    + "                \"associated_data\": \"certificate\",\n"
                    + "                \"ciphertext\": "
                    + "\"LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUVoRENDQTJ5Z0F3SUJBZ0lVYVpycG82QUNLTC9oTS9xYVNwbmpIVTBsY2JRd0RRWUpLb1pJaHZjTkFRRUYKQlFBd1JqRWJNQmtHQTFVRUF3d1NWR1Z1Y0dGNUxtTnZiU0JWYzJWeUlFTkJNUkl3RUFZRFZRUUxEQWxEUVNCRApaVzUwWlhJeEV6QVJCZ05WQkFvTUNsUmxibkJoZVM1amIyMHdIaGNOTWpFeE1URXhNREkxTmpNMldoY05Nall4Ck1URXdNREkxTmpNMldqQ0JsVEVZTUJZR0ExVUVBd3dQVkdWdWNHRjVMbU52YlNCemFXZHVNU1V3SXdZSktvWkkKaHZjTkFRa0JGaFp6ZFhCd2IzSjBRSE42YVhSeWRYTXVZMjl0TG1OdU1SMHdHd1lEVlFRTERCUlVaVzV3WVhrdQpZMjl0SUVOQklFTmxiblJsY2pFVE1CRUdBMVVFQ2d3S1ZHVnVjR0Y1TG1OdmJURVJNQThHQTFVRUJ3d0lVMmhsCmJscG9aVzR4Q3pBSkJnTlZCQVlUQWtOT01JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0MKQVFFQTdHdk54SGYwTWRUSTBENHNXakw4ZkhjMkhaNTBXenVSd1JJdHQxZTdZbUxwbTRsS2psS05tS0xSdHJKTwp2bVRYd3FPbUpOTEt6NlFITW5SMGF6OWw1cElrOFprenVyZXhVcHlkSHdUS3MvT09ENVpLVHRpYWl3WXkxa1NDCkdUVnpnRUo3RHczUHpxUk1kTTgwK0czMGgrUndJRFFJWE1JWlMzVzBpTGE5cHhNWFpWekQxN042QmlCSXBEdXAKTS95RXJmV3l4QmQ3anExY3J2Qm9IcmJ5UGg1YWc0dWlWNEUwQnB0bVdibjJuT0lNcTF2dVkvTGFjb3poeFBjeApuVVZWUEtMeFd3eHBwdk5RcFdySjBWakN4d2dqaEZVL0R4WnVxcjUwdXlCMGc0T0VHQXZsSmlYNy9sNjI1ZGVkCkFKbWJpWW9KV3JPb2hjcWF1SGRxSmFJWitRSURBUUFCbzRJQkdEQ0NBUlF3Q1FZRFZSMFRCQUl3QURBTEJnTlYKSFE4RUJBTUNCc0F3VHdZSUt3WUJCUVVIQVFFRVF6QkJNRDhHQ0NzR0FRVUZCekFDaGpOdlkzTndMR2gwZEhBNgpMeTlaYjNWeVgxTmxjblpsY2w5T1lXMWxPbEJ2Y25RdlZHOXdRMEV2Ykc5a2NGOUNZWE5sUkU0d2FRWURWUjBmCkJHSXdZREJlb0Z5Z1dvWllhSFIwY0Rvdkx6a3VNVGt1TVRZeExqUTZPREE0TUM5VWIzQkRRUzl3ZFdKc2FXTXYKYVhSeWRYTmpjbXcvUTBFOU16bENORGszUVVKRE9FRkZPRGcxTnpRMVFrWTFOamd4UlRSR01ETkNPRUkyTkRkRwpNamhGUVRBZkJnTlZIU01FR0RBV2dCUk9jODA1dHZ1cEYvak9pWWFwY3ZTa2x2UHJMakFkQmdOVkhRNEVGZ1FVClNHR2Z1bTBsaVNVTEJSbHJUaGtkc0ZlM2F1NHdEUVlKS29aSWh2Y05BUUVGQlFBRGdnRUJBSFFCZE5NUmJMUkEKVGFCbld2azlJblYxUjdXYU81dUlLazNueDQxU3ZCU2lLVHlLTktHVGdybysxUEw5YUhQSENtblBaMHRRV1NYZQpiNzhtRkFtd0NyejdMVzdMOXpRYTJLKzNGay9YNEEzRVNsRHBTNFZZK3h2Rm11aks3WGZtemJxenZSNXovdEZlCkhBTVovTk1xS2M2cmFoOVdjS2ZSbjNFUTBEV2Z1ZlFtcEdQVHVYNVpQbDg0VHVQWkc3TWRBcG4zVno0eGh4R0EKNW9oWUNvQ29CSzhZTkFjTGVITmttYXRiNkdKZlM4VStmVmNOZER6Ym51cklTWXpKdkgxNXlvMWlhR05WQXFqUApGd2I5K24zaFZaVjZKbTFOOVZJRGdTbUFhZUJMajNEbStUMG9nMzdGbUxRMWN6MTQ4T0orU2NWSkZqWjNJKzl2CklRejRCMmpDV0g0PQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0t\"\n"
                    + "            }\n"
                    + "        }\n"
                    + "    ]\n"
                    + "}"));
    return queue;
  }

  static BlockingQueue<MockResponse> newUpdateWith500Queue() {
    BlockingQueue<MockResponse> queue = new LinkedBlockingQueue<>();
    queue.add(
        new MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(
                "{\n"
                    + "    \"data\": [\n"
                    + "        {\n"
                    + "            \"serial_no\": \"39FACCC173F3485C76C61FD5C0D662AE4A838AA9\",\n"
                    + "            \"effective_time \": \"2018-06-08T10:34:56+08:00\",\n"
                    + "            \"expire_time \": \"2018-12-08T10:34:56+08:00\",\n"
                    + "            \"encrypt_certificate\": {\n"
                    + "                \"algorithm\": \"AEAD_AES_256_GCM\",\n"
                    + "                \"nonce\": \"61f9c719728a\",\n"
                    + "                \"associated_data\": \"certificate\",\n"
                    + "                \"ciphertext\": "
                    + "\"LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUVoRENDQTJ5Z0F3SUJBZ0lVT2ZyTXdYUHpTRngyeGgvVndOWmlya3FEaXFrd0RRWUpLb1pJaHZjTkFRRUYKQlFBd1JqRWJNQmtHQTFVRUF3d1NWR1Z1Y0dGNUxtTnZiU0JWYzJWeUlFTkJNUkl3RUFZRFZRUUxEQWxEUVNCRApaVzUwWlhJeEV6QVJCZ05WQkFvTUNsUmxibkJoZVM1amIyMHdIaGNOTWpFeE1URXhNREkxT0RRMldoY05Nall4Ck1URXdNREkxT0RRMldqQ0JsVEVZTUJZR0ExVUVBd3dQVkdWdWNHRjVMbU52YlNCemFXZHVNU1V3SXdZSktvWkkKaHZjTkFRa0JGaFp6ZFhCd2IzSjBRSE42YVhSeWRYTXVZMjl0TG1OdU1SMHdHd1lEVlFRTERCUlVaVzV3WVhrdQpZMjl0SUVOQklFTmxiblJsY2pFVE1CRUdBMVVFQ2d3S1ZHVnVjR0Y1TG1OdmJURVJNQThHQTFVRUJ3d0lVMmhsCmJscG9aVzR4Q3pBSkJnTlZCQVlUQWtOT01JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0MKQVFFQXY4Rkhvc0Z1QTJodWxvVHplUHRBV0dkWnE3Q2o0RDgzeE45UURManhGTktuR2h6enZJMVpZL2J1bVVrVwpFRzAxc1Z5dEdaRFlhWUd4ZlJlcTdTSVN0SUlWQ0ZORkdwMGYzMDkwQmp5TDliNlZka0NsVWE5Vk8rU0VTZnJqCldhVmgxczBSb1NHMlAwd3FKYlMrQ240REZrM3JLN1NWZEJnQjJJVTBTY1FBOERBL0J0K2hTK2xscDhld0RwRHkKbkRIVUNaV0xGY29TNWxDa1BkNWxneE9qNzV0K3pDSHhXMkFKMEFPS0FHTVZ5MGRVc3JGMzA4KzZBTHJuR0JLRQp3ckNHc1ZWOGp0eEtEMFZKa05LSXVrUDh4S0Z2emRuMDRIM3hFYTE2YUl0ZmRHemt2WFg4S1FVeVBYWDY1Z28rCk50MmYxVGNRZzVNdE9CRUwxNi92RTlmb3dRSURBUUFCbzRJQkdEQ0NBUlF3Q1FZRFZSMFRCQUl3QURBTEJnTlYKSFE4RUJBTUNCc0F3VHdZSUt3WUJCUVVIQVFFRVF6QkJNRDhHQ0NzR0FRVUZCekFDaGpOdlkzTndMR2gwZEhBNgpMeTlaYjNWeVgxTmxjblpsY2w5T1lXMWxPbEJ2Y25RdlZHOXdRMEV2Ykc5a2NGOUNZWE5sUkU0d2FRWURWUjBmCkJHSXdZREJlb0Z5Z1dvWllhSFIwY0Rvdkx6a3VNVGt1TVRZeExqUTZPREE0TUM5VWIzQkRRUzl3ZFdKc2FXTXYKYVhSeWRYTmpjbXcvUTBFOU16bENORGszUVVKRE9FRkZPRGcxTnpRMVFrWTFOamd4UlRSR01ETkNPRUkyTkRkRwpNamhGUVRBZkJnTlZIU01FR0RBV2dCUk9jODA1dHZ1cEYvak9pWWFwY3ZTa2x2UHJMakFkQmdOVkhRNEVGZ1FVCkdUTEU2YnhudG5obnBobFBOd1FBNkw3SGpMc3dEUVlKS29aSWh2Y05BUUVGQlFBRGdnRUJBR3ovWnRVTlZKWEYKUVZ1TGU5MFFGUCtkNmRQUjhaYmJscTVMcXRMYW1xcmFmclF5R0ZOZDZxdEdrY293YXVJM0xKWldPeitTZ0J6YwpjS2dyOXdYb0g1azNSL0pmYVhYU2F3Q3d1NjZIL3grbFlhcU45TXREYkVoVi9Sa0U3dTRwb2I4ZkJSOC9FNTllCnNOQnFmMVdHODhTcUZPbDZ3VDY5ajR0cHBjRVlldjFISCtaelpScDN4U3NJV3dSMHNtV21IOE9BMWZLSFYramsKY21IaFJwNDFTeTQ4bUREdkRuRGVsTU5UaU1wUjk2a1JONmNLWG1LeXZadkNhTkNUOXpyNExVdStMUHdmTFdiWgpBQzVtL1dHclBKakhWMXAyUCt4NmRYaEJybitWV2tyYzZYZTVqeXFmdnpBRVBvN1lsQVFXamh2T2I5d3U4TzNTCjNJVnZrTzNiSkhZPQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0t\"\n"
                    + "            }\n"
                    + "        }\n"
                    + "    ]\n"
                    + "}"));
    queue.add(new MockResponse().setResponseCode(500));
    return queue;
  }

  static BlockingQueue<MockResponse> newUpdateWithBadBodyQueue() {
    BlockingQueue<MockResponse> queue = new LinkedBlockingQueue<>();
    queue.add(
        new MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(
                "{\n"
                    + "    \"data\": [\n"
                    + "        {\n"
                    + "            \"serial_no\": \"39FACCC173F3485C76C61FD5C0D662AE4A838AA9\",\n"
                    + "            \"effective_time \": \"2018-06-08T10:34:56+08:00\",\n"
                    + "            \"expire_time \": \"2018-12-08T10:34:56+08:00\",\n"
                    + "            \"encrypt_certificate\": {\n"
                    + "                \"algorithm\": \"AEAD_AES_256_GCM\",\n"
                    + "                \"nonce\": \"61f9c719728a\",\n"
                    + "                \"associated_data\": \"certificate\",\n"
                    + "                \"ciphertext\": "
                    + "\"LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUVoRENDQTJ5Z0F3SUJBZ0lVT2ZyTXdYUHpTRngyeGgvVndOWmlya3FEaXFrd0RRWUpLb1pJaHZjTkFRRUYKQlFBd1JqRWJNQmtHQTFVRUF3d1NWR1Z1Y0dGNUxtTnZiU0JWYzJWeUlFTkJNUkl3RUFZRFZRUUxEQWxEUVNCRApaVzUwWlhJeEV6QVJCZ05WQkFvTUNsUmxibkJoZVM1amIyMHdIaGNOTWpFeE1URXhNREkxT0RRMldoY05Nall4Ck1URXdNREkxT0RRMldqQ0JsVEVZTUJZR0ExVUVBd3dQVkdWdWNHRjVMbU52YlNCemFXZHVNU1V3SXdZSktvWkkKaHZjTkFRa0JGaFp6ZFhCd2IzSjBRSE42YVhSeWRYTXVZMjl0TG1OdU1SMHdHd1lEVlFRTERCUlVaVzV3WVhrdQpZMjl0SUVOQklFTmxiblJsY2pFVE1CRUdBMVVFQ2d3S1ZHVnVjR0Y1TG1OdmJURVJNQThHQTFVRUJ3d0lVMmhsCmJscG9aVzR4Q3pBSkJnTlZCQVlUQWtOT01JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0MKQVFFQXY4Rkhvc0Z1QTJodWxvVHplUHRBV0dkWnE3Q2o0RDgzeE45UURManhGTktuR2h6enZJMVpZL2J1bVVrVwpFRzAxc1Z5dEdaRFlhWUd4ZlJlcTdTSVN0SUlWQ0ZORkdwMGYzMDkwQmp5TDliNlZka0NsVWE5Vk8rU0VTZnJqCldhVmgxczBSb1NHMlAwd3FKYlMrQ240REZrM3JLN1NWZEJnQjJJVTBTY1FBOERBL0J0K2hTK2xscDhld0RwRHkKbkRIVUNaV0xGY29TNWxDa1BkNWxneE9qNzV0K3pDSHhXMkFKMEFPS0FHTVZ5MGRVc3JGMzA4KzZBTHJuR0JLRQp3ckNHc1ZWOGp0eEtEMFZKa05LSXVrUDh4S0Z2emRuMDRIM3hFYTE2YUl0ZmRHemt2WFg4S1FVeVBYWDY1Z28rCk50MmYxVGNRZzVNdE9CRUwxNi92RTlmb3dRSURBUUFCbzRJQkdEQ0NBUlF3Q1FZRFZSMFRCQUl3QURBTEJnTlYKSFE4RUJBTUNCc0F3VHdZSUt3WUJCUVVIQVFFRVF6QkJNRDhHQ0NzR0FRVUZCekFDaGpOdlkzTndMR2gwZEhBNgpMeTlaYjNWeVgxTmxjblpsY2w5T1lXMWxPbEJ2Y25RdlZHOXdRMEV2Ykc5a2NGOUNZWE5sUkU0d2FRWURWUjBmCkJHSXdZREJlb0Z5Z1dvWllhSFIwY0Rvdkx6a3VNVGt1TVRZeExqUTZPREE0TUM5VWIzQkRRUzl3ZFdKc2FXTXYKYVhSeWRYTmpjbXcvUTBFOU16bENORGszUVVKRE9FRkZPRGcxTnpRMVFrWTFOamd4UlRSR01ETkNPRUkyTkRkRwpNamhGUVRBZkJnTlZIU01FR0RBV2dCUk9jODA1dHZ1cEYvak9pWWFwY3ZTa2x2UHJMakFkQmdOVkhRNEVGZ1FVCkdUTEU2YnhudG5obnBobFBOd1FBNkw3SGpMc3dEUVlKS29aSWh2Y05BUUVGQlFBRGdnRUJBR3ovWnRVTlZKWEYKUVZ1TGU5MFFGUCtkNmRQUjhaYmJscTVMcXRMYW1xcmFmclF5R0ZOZDZxdEdrY293YXVJM0xKWldPeitTZ0J6YwpjS2dyOXdYb0g1azNSL0pmYVhYU2F3Q3d1NjZIL3grbFlhcU45TXREYkVoVi9Sa0U3dTRwb2I4ZkJSOC9FNTllCnNOQnFmMVdHODhTcUZPbDZ3VDY5ajR0cHBjRVlldjFISCtaelpScDN4U3NJV3dSMHNtV21IOE9BMWZLSFYramsKY21IaFJwNDFTeTQ4bUREdkRuRGVsTU5UaU1wUjk2a1JONmNLWG1LeXZadkNhTkNUOXpyNExVdStMUHdmTFdiWgpBQzVtL1dHclBKakhWMXAyUCt4NmRYaEJybitWV2tyYzZYZTVqeXFmdnpBRVBvN1lsQVFXamh2T2I5d3U4TzNTCjNJVnZrTzNiSkhZPQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0t\"\n"
                    + "            }\n"
                    + "        }\n"
                    + "    ]\n"
                    + "}"));
    queue.add(
        new MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody("{}"));
    return queue;
  }

  @BeforeAll
  static void initAll() throws Exception {
    final Dispatcher dispatcher =
        new Dispatcher() {

          final BlockingQueue<MockResponse> updateWith500Queue = newUpdateWith500Queue();

          final BlockingQueue<MockResponse> updateQueue = newUpdateQueue();

          final BlockingQueue<MockResponse> updateWithBadBodyQueue = newUpdateWithBadBodyQueue();

          private MockResponse tryRemove(BlockingQueue<MockResponse> queue) {
            try {
              return queue.remove();
            } catch (NoSuchElementException e) {
              return new MockResponse().setResponseCode(404);
            }
          }

          @NotNull
          @Override
          public MockResponse dispatch(RecordedRequest request) {

            switch (Objects.requireNonNull(request.getPath())) {
              case "/testUpdateWith500":
                return tryRemove(updateWith500Queue);
              case "/testUpdate":
                return tryRemove(updateQueue);
              case "/testUpdateWithBadBody":
                return tryRemove(updateWithBadBodyQueue);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    server.setDispatcher(dispatcher);
    server.start();

    // 避免被别的用例影响
    AutoCertificateService.shutdown();
    AutoCertificateService.start(Duration.ofSeconds(3));
  }

  @AfterAll
  static void tearDownAll() throws Exception {
    server.shutdown();
  }

  static class FakeCertificateHandler implements CertificateHandler {
    @Override
    public X509Certificate generateCertificate(String certificate) {
      return PemUtil.loadX509FromString(certificate);
    }

    @Override
    public void validateCertPath(X509Certificate certificate) {}
  }

  static class FakeAeadCiper implements AeadCipher {

    @Override
    public String encrypt(byte[] associatedData, byte[] nonce, byte[] plaintext) {
      throw new UnsupportedOperationException();
    }

    @Override
    public String decrypt(byte[] associatedData, byte[] nonce, byte[] ciphertext) {
      return new String(ciphertext);
    }
  }

  static class FakeCredential implements Credential {

    @Override
    public String getSchema() {
      return "fake-schema";
    }

    @Override
    public String getMerchantId() {
      return "fake-merchant";
    }

    @Override
    public String getAuthorization(URI uri, String httpMethod, String signBody) {
      return "fake-auth";
    }
  }

  static class FakeValidator implements Validator {
    @Override
    public <T> boolean validate(HttpHeaders responseHeaders, String body) {
      return true;
    }
  }

  @Test
  void testUpdate() {
    Credential fakeCredential = new FakeCredential();
    Validator fakeHttpValidator = new FakeValidator();

    HttpClient client =
        new DefaultHttpClientBuilder()
            .credential(fakeCredential)
            .validator(fakeHttpValidator)
            .build();

    CertificateDownloader downloader =
        new CertificateDownloader.Builder()
            .httpClient(client)
            .downloadUrl(server.url("/testUpdate").url().toString())
            .certificateHandler(new FakeCertificateHandler())
            .aeadCipher(new FakeAeadCiper())
            .build();

    AutoCertificateService.register("testUpdate", "test", downloader);

    assertNotNull(AutoCertificateService.getAvailableCertificate("testUpdate", "test"));
    assertNotNull(
        AutoCertificateService.getCertificate(
            "testUpdate", "test", "39FACCC173F3485C76C61FD5C0D662AE4A838AA9"));

    await()
        .atMost(3500, TimeUnit.MILLISECONDS)
        .untilAsserted(
            () ->
                assertNotNull(
                    AutoCertificateService.getCertificate(
                        "testUpdate", "test", "699AE9A3A00228BFE133FA9A4A99E31D4D2571B4")));

    assertNull(
        AutoCertificateService.getCertificate(
            "testUpdate", "test", "39FACCC173F3485C76C61FD5C0D662AE4A838AA9"));

    AutoCertificateService.unregister("testUpdate", "test");
  }

  @Test
  void testUpdateWith500() {
    Credential fakeCredential = new FakeCredential();
    Validator fakeHttpValidator = new FakeValidator();

    HttpClient client =
        new DefaultHttpClientBuilder()
            .credential(fakeCredential)
            .validator(fakeHttpValidator)
            .build();

    CertificateDownloader downloader =
        new CertificateDownloader.Builder()
            .httpClient(client)
            .downloadUrl(server.url("/testUpdateWith500").url().toString())
            .certificateHandler(new FakeCertificateHandler())
            .aeadCipher(new FakeAeadCiper())
            .build();

    AutoCertificateService.register("testUpdateWith500", "test", downloader);

    assertNotNull(AutoCertificateService.getAvailableCertificate("testUpdateWith500", "test"));
    assertNotNull(
        AutoCertificateService.getCertificate(
            "testUpdateWith500", "test", "39FACCC173F3485C76C61FD5C0D662AE4A838AA9"));

    with()
        .pollDelay(3500, TimeUnit.MILLISECONDS)
        .await()
        .untilAsserted(
            () ->
                assertNotNull(
                    AutoCertificateService.getCertificate(
                        "testUpdateWith500", "test", "39FACCC173F3485C76C61FD5C0D662AE4A838AA9")));

    AutoCertificateService.unregister("testUpdateWith500", "test");
  }

  @Test
  void testUpdateWithBadBody() {
    Credential fakeCredential = new FakeCredential();
    Validator fakeHttpValidator = new FakeValidator();

    HttpClient client =
        new DefaultHttpClientBuilder()
            .credential(fakeCredential)
            .validator(fakeHttpValidator)
            .build();

    CertificateDownloader downloader =
        new CertificateDownloader.Builder()
            .httpClient(client)
            .downloadUrl(server.url("/testUpdateWithBadBody").url().toString())
            .certificateHandler(new FakeCertificateHandler())
            .aeadCipher(new FakeAeadCiper())
            .build();

    AutoCertificateService.register("testUpdateWithBadBody", "test", downloader);

    assertNotNull(AutoCertificateService.getAvailableCertificate("testUpdateWithBadBody", "test"));
    assertNotNull(
        AutoCertificateService.getCertificate(
            "testUpdateWithBadBody", "test", "39FACCC173F3485C76C61FD5C0D662AE4A838AA9"));

    with()
        .pollDelay(3500, TimeUnit.MILLISECONDS)
        .await()
        .untilAsserted(
            () ->
                assertNotNull(
                    AutoCertificateService.getCertificate(
                        "testUpdateWithBadBody",
                        "test",
                        "39FACCC173F3485C76C61FD5C0D662AE4A838AA9")));

    AutoCertificateService.unregister("testUpdateWithBadBody", "test");
  }

  @Test
  void testInitWithInvalidBody() throws Exception {
    MockWebServer server = new MockWebServer();
    server.enqueue(
        new MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody("{}"));
    server.start();

    Credential fakeCredential = new FakeCredential();
    Validator fakeHttpValidator = new FakeValidator();

    HttpClient client =
        new DefaultHttpClientBuilder()
            .credential(fakeCredential)
            .validator(fakeHttpValidator)
            .build();

    CertificateDownloader downloader =
        new CertificateDownloader.Builder()
            .httpClient(client)
            .downloadUrl(server.url("/v3/test/path").url().toString())
            .certificateHandler(new FakeCertificateHandler())
            .aeadCipher(new FakeAeadCiper())
            .build();

    assertThrows(
        Exception.class,
        () -> AutoCertificateService.register("testInitWithInvalidBody", "test", downloader));
    AutoCertificateService.unregister("testInitWithInvalidBody", "test");
  }
}
