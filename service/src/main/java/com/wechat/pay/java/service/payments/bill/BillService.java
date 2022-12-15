package com.wechat.pay.java.service.payments.bill;

import static com.wechat.pay.java.core.http.UrlEncoder.urlEncode;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.MediaType;
import com.wechat.pay.java.core.http.QueryParameter;
import com.wechat.pay.java.core.util.ShaUtil;
import com.wechat.pay.java.service.payments.bill.model.ApplyFundFlowBillRequest;
import com.wechat.pay.java.service.payments.bill.model.ApplyFundFlowBillResponse;
import com.wechat.pay.java.service.payments.bill.model.ApplyTradeBillRequest;
import com.wechat.pay.java.service.payments.bill.model.ApplyTradeBillResponse;
import com.wechat.pay.java.service.payments.bill.model.TarType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;

public class BillService {
  private final HttpClient httpClient;
  private final HostName hostName;

  private BillService(HttpClient httpClient, HostName hostName) {
    this.httpClient = requireNonNull(httpClient);
    this.hostName = hostName;
  }

  /** FileDownloadService构造器 */
  public static class Builder {

    private HttpClient httpClient;
    private HostName hostName;

    public Builder config(Config config) {
      this.httpClient =
          new DefaultHttpClientBuilder()
              .credential(requireNonNull(config.createCredential()))
              .validator(requireNonNull(config.createValidator()))
              .build();
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = requireNonNull(httpClient);
      return this;
    }

    public Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    public BillService build() {
      return new BillService(httpClient, hostName);
    }
  }

  /**
   * @param downloadUrl 下载账单的URL
   * @param hashValue 账单的hash值
   * @param tarType 压缩类型
   * @param billPath 账单存储路径，例如：/Users/test/20221213.csv
   * @throws IOException 下载失败、I/O异常
   */
  public void downloadBill(String downloadUrl, String hashValue, TarType tarType, String billPath)
      throws IOException {
    try (InputStream responseBodyStream = httpClient.download(downloadUrl)) {
      File file = new File(billPath);
      if (tarType != null) {
        if (!tarType.equals(TarType.GZIP)) {
          throw new IllegalArgumentException("Unsupported compression type: " + tarType.name());
        }
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(responseBodyStream)) {
          Files.copy(gzipInputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
      } else {
        Files.copy(responseBodyStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
      try (FileInputStream fileInputStream = new FileInputStream(file)) {
        String hash = ShaUtil.getSha1HexString(fileInputStream);
        if (!MessageDigest.isEqual(
            hash.getBytes(StandardCharsets.UTF_8), hashValue.getBytes(StandardCharsets.UTF_8))) {
          file.deleteOnExit();
          throw new ValidationException("Validate bill hashValue failed.");
        }
      }
    }
  }

  /**
   * 申请交易账单
   *
   * @param request request
   * @return response
   */
  public ApplyTradeBillResponse applyTradeBill(ApplyTradeBillRequest request) {
    String requestPath = "https://api.mch.weixin.qq.com/v3/bill/tradebill";
    QueryParameter queryParameter = new QueryParameter();
    if (request.getBillDate() != null) {
      queryParameter.add("bill_date", urlEncode(request.getBillDate()));
    }
    if (request.getBillType() != null) {
      queryParameter.add("bill_type", urlEncode(request.getBillType().name()));
    }
    if (request.getTarType() != null) {
      queryParameter.add("tar_type", urlEncode(request.getTarType().name()));
    }
    requestPath += queryParameter.getQueryStr();
    if (this.hostName != null) {
      requestPath = requestPath.replaceFirst(HostName.API.getValue(), hostName.getValue());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(requestPath)
            .headers(headers)
            .build();
    // 申请交易账单
    return httpClient.execute(httpRequest, ApplyTradeBillResponse.class).getServiceResponse();
  }

  /**
   * 申请资金账单
   *
   * @param request request
   * @return response
   */
  public ApplyFundFlowBillResponse applyFundFlowBill(ApplyFundFlowBillRequest request) {
    String requestPath = "https://api.mch.weixin.qq.com/v3/bill/fundflowbill";
    QueryParameter queryParameter = new QueryParameter();
    if (request.getBillDate() != null) {
      queryParameter.add("bill_date", urlEncode(request.getBillDate()));
    }
    if (request.getAccountType() != null) {
      queryParameter.add("account_type", urlEncode(request.getAccountType().name()));
    }
    if (request.getTarType() != null) {
      queryParameter.add("tar_type", urlEncode(request.getTarType().name()));
    }
    requestPath += queryParameter.getQueryStr();
    if (this.hostName != null) {
      requestPath = requestPath.replaceFirst(HostName.API.getValue(), hostName.getValue());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(requestPath)
            .headers(headers)
            .build();
    return httpClient.execute(httpRequest, ApplyFundFlowBillResponse.class).getServiceResponse();
  }

  /**
   * 申请和下载交易账单，若为压缩类型账单，会解压缩并存放在billPath
   *
   * @param request request
   * @param billPath 账单存储路径，例如：/Users/test/20221213.csv
   * @throws IOException 下载失败、I/O异常
   */
  public void applyAndDownloadTradeBill(ApplyTradeBillRequest request, String billPath)
      throws IOException {
    ApplyTradeBillResponse response = applyTradeBill(request);
    downloadBill(
        response.getDownloadUrl(), response.getHashValue(), request.getTarType(), billPath);
  }
  /**
   * 申请和下载资金账单，若为压缩类型账单，会解压缩并存放在billPath
   *
   * @param request request
   * @param billPath 账单存储路径，例如：/Users/test/20221213.csv
   * @throws IOException 下载失败、I/O异常
   */
  public void applyAndDownloadFundFlowBill(ApplyFundFlowBillRequest request, String billPath)
      throws IOException {
    ApplyFundFlowBillResponse response = applyFundFlowBill(request);
    downloadBill(
        response.getDownloadUrl(), response.getHashValue(), request.getTarType(), billPath);
  }
}
