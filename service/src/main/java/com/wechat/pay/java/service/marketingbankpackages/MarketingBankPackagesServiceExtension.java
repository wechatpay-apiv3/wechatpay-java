package com.wechat.pay.java.service.marketingbankpackages;

import static com.wechat.pay.java.core.http.UrlEncoder.urlEncode;
import static com.wechat.pay.java.core.util.GsonUtil.toJson;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.FileRequestBody;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.MediaType;
import com.wechat.pay.java.core.util.ShaUtil;
import com.wechat.pay.java.service.marketingbankpackages.model.FileMeta;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskRequest;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskResponse;
import com.wechat.pay.java.service.marketingbankpackages.model.Task;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/** MarketingBankPackagesServiceExtension服务 */
public class MarketingBankPackagesServiceExtension {

  private final MarketingBankPackagesService packagesService;
  private final HttpClient httpClient;
  private final PrivacyEncryptor encryptor;

  private MarketingBankPackagesServiceExtension(
      HttpClient httpClient, HostName hostName, PrivacyEncryptor encryptor) {
    MarketingBankPackagesService.Builder builder = new MarketingBankPackagesService.Builder();
    builder.httpClient(httpClient);
    if (hostName != null) {
      builder.hostName(hostName);
    }
    this.encryptor = requireNonNull(encryptor);
    this.httpClient = requireNonNull(httpClient);
    this.packagesService = builder.build();
  }

  /** MarketingBankPackagesServiceExtension构造器 */
  public static class Builder {

    private HostName hostName;
    private HttpClient httpClient;
    private PrivacyEncryptor encryptor;

    public Builder config(Config config) {
      this.httpClient = new DefaultHttpClientBuilder().config(config).build();
      this.encryptor = config.createEncryptor();
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    public Builder encryptor(PrivacyEncryptor encryptor) {
      this.encryptor = encryptor;
      return this;
    }

    public MarketingBankPackagesServiceExtension build() {
      return new MarketingBankPackagesServiceExtension(httpClient, hostName, encryptor);
    }
  }

  /**
   * 查询上传任务列表
   *
   * @param request 请求参数
   * @return ListTaskResponse
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public ListTaskResponse listTask(ListTaskRequest request) {
    return packagesService.listTask(request);
  }

  /**
   * 导入定向用户协议号(创建上传任务)
   *
   * @param packageId 号码包id
   * @param bankType 银行类型
   * @param filePath 待上传的文件的绝对路径。文件行数建议不超过5500行，如果原始文件过大，商户需先切割为若干小文件再逐个上传
   * @return Task
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public Task uploadPackageByFile(String packageId, String bankType, String filePath)
      throws IllegalArgumentException {
    File file = new File(filePath);
    ArrayList<String> fileContentList = new ArrayList<>();
    try {
      // 读取文件
      BufferedReader reader;
      reader = new BufferedReader(new FileReader(file));
      String line = reader.readLine();
      while (line != null) {
        fileContentList.add(line);
        line = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Upload file, failed to open filePath:" + filePath);
    }
    return uploadPackage(packageId, bankType, file.getName(), fileContentList);
  }

  /**
   * 导入定向用户协议号(创建上传任务)
   *
   * @param packageId 号码包id
   * @param bankType 银行类型
   * @param fileName 文件名，非文件路径
   * @param fileContentList 文件内容，数组中每个元素代表一条用户协议号明文。每个文件行数建议不超过5500行
   * @return Task
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public Task uploadPackage(
      String packageId, String bankType, String fileName, ArrayList<String> fileContentList) {
    if (fileContentList.size() > 5500) {
      throw new IllegalArgumentException(
          "Number of lines should not bigger than 5500. fileContentList.size: "
              + String.valueOf(fileContentList.size()));
    }

    // 1、拼接url
    String uploadPath =
        "https://api.mch.weixin.qq.com/v3/marketing/bank/packages/"
            + urlEncode(packageId)
            + "/tasks";

    // 2、逐行加密文件内容
    StringBuilder sb = new StringBuilder();
    for (String each : fileContentList) {
      sb.append(encryptor.encrypt(each)).append("\n");
    }
    String encryptedFileContent = sb.toString();

    // 3、计算sha256文件摘要
    byte[] fileBytes = encryptedFileContent.getBytes();
    String fileSha256 = ShaUtil.getSha256HexString(fileBytes);

    // 4、组装meta
    FileMeta fileMeta = new FileMeta();
    fileMeta.setFilename(fileName);
    fileMeta.setSha256(fileSha256);
    fileMeta.setBankType(bankType);

    // 5、执行上传
    return uploadFile(uploadPath, toJson(fileMeta), fileName, fileBytes);
  }

  private Task uploadFile(String uploadPath, String meta, String fileName, byte[] fileBytes) {
    HttpRequest request =
        new HttpRequest.Builder()
            .addHeader(Constant.ACCEPT, " */*")
            .addHeader(Constant.WECHAT_PAY_SERIAL, encryptor.getWechatpaySerial())
            .addHeader(Constant.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA.getValue())
            .httpMethod(HttpMethod.POST)
            .url(uploadPath)
            .body(
                new FileRequestBody.Builder().meta(meta).fileName(fileName).file(fileBytes).build())
            .build();
    return httpClient.execute(request, Task.class).getServiceResponse();
  }
}
