package com.wechat.pay.java.service.marketingbankpackages;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskRequest;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskResponse;
import com.wechat.pay.java.service.marketingbankpackages.model.Task;
import java.util.ArrayList;

public class MarketingBankPackagesServiceExtensionExample {

  /** 商户号 */
  public static String merchantId = "190000****";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";
  /** 商户APIV3密钥 */
  public static String apiV3Key = "...";

  public static MarketingBankPackagesServiceExtension service;

  public static void main(String[] args) {
    // 初始化商户配置
    Config config =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .apiV3Key(apiV3Key)
            .build();

    // 初始化服务
    service = new MarketingBankPackagesServiceExtension.Builder().config(config).build();

    // 如需通过代理调用微信支付API，可构造OkHttpClient来初始化服务，参考以下示例
    // String hostName = "localhost";
    // int port = 1080;
    // OkHttpClient okHttpClient = new OkHttpClient.Builder()
    //         .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostName, port)))
    //         .build();
    // 示例一：
    // HttpClient okHttpClientAdapter = new OkHttpClientAdapter(config.createCredential(),
    // config.createValidator(),
    //         okHttpClient);
    // service = new
    // MarketingBankPackagesServiceExtension.Builder().encryptor(config.createEncryptor()).httpClient(okHttpClientAdapter).build();
    // 示例二：
    // HttpClient httpClient = new
    // DefaultHttpClientBuilder().config(config).okHttpClient(okHttpClient).build();
    // service = new
    // MarketingBankPackagesServiceExtension.Builder().encryptor(config.createEncryptor()).httpClient(httpClient).build();

    // ... 调用接口
    // uploadPackageByFile();
    uploadPackage();
    listTask();
  }

  /** 查询上传任务列表 */
  public static ListTaskResponse listTask() {

    ListTaskRequest request = new ListTaskRequest();
    request.setPackageId("2000");
    request.setOffset(0L);
    request.setLimit(10L);
    // request.setFilename("test.csv");

    ListTaskResponse response = service.listTask(request);
    System.out.println("##### listTask response ##### \n" + response.toString());
    return response;
  }

  // 上传号码包文件
  public static Task uploadPackageByFile() {
    long start = System.currentTimeMillis();
    String packageId = "2000";
    String bankType = "ABC_DEBIT";
    String filePath = "";
    Task fileUploadResponse = null;
    try {
      fileUploadResponse = service.uploadPackageByFile(packageId, bankType, filePath);
      System.out.println("##### upload response ##### \n" + fileUploadResponse.toString());
    } catch (IllegalArgumentException e) {
      // ...上报监控和打印日志
      System.out.println("Upload error:" + e.toString());
    }
    long costTime = System.currentTimeMillis() - start;
    System.out.println("costTime: " + String.valueOf(costTime));
    return fileUploadResponse;
  }

  // 上传号码包
  public static Task uploadPackage() {
    long start = System.currentTimeMillis();
    String packageId = "2000";
    String bankType = "ABC_DEBIT";
    String fileName = "test.csv";
    ArrayList<String> fileContentList = new ArrayList<>();
    fileContentList.add("111111111111111111111111");
    fileContentList.add("222222222222222222222222");
    fileContentList.add("333333333333333333333333");
    fileContentList.add("4444444444444444444444444");
    Task fileUploadResponse = null;
    try {
      fileUploadResponse = service.uploadPackage(packageId, bankType, fileName, fileContentList);
      System.out.println("##### upload response ##### \n" + fileUploadResponse.toString());
    } catch (Exception e) {
      // ...上报监控和打印日志
      System.out.println("Upload error:" + e.toString());
    }
    long costTime = System.currentTimeMillis() - start;
    System.out.println("costTime: " + String.valueOf(costTime));
    return fileUploadResponse;
  }
}
