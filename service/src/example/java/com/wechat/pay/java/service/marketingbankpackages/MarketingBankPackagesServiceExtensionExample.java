package com.wechat.pay.java.service.marketingbankpackages;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskRequest;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskResponse;
import com.wechat.pay.java.service.marketingbankpackages.model.Task;
import java.util.ArrayList;

public class MarketingBankPackagesServiceExtensionExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static MarketingBankPackagesServiceExtension service;

  public static void main(String[] args) {
    // 初始化商户配置
    RSAConfig config =
        new RSAConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();

    // 初始化服务
    service = new MarketingBankPackagesServiceExtension.Builder().config(config).build();

    // 如需通过代理调用微信支付API，可以构造OkHttpClient来初始化服务
    // String hostName = "localhost";
    // int port = 1080;
    // OkHttpClient okHttpClient = new OkHttpClient.Builder()
    //         .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostName, port)))
    //         .build();
    // HttpClient okHttpClientAdapter = new OkHttpClientAdapter(config.createCredential(),
    // config.createValidator(),
    //         okHttpClient);
    // service = new
    // MarketingBankPackagesServiceExtension.Builder().config(config).httpClient(okHttpClientAdapter)
    //         .build();

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
