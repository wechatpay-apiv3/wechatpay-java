package com.wechat.pay.java.service.file;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.FileRequestBody;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.MediaType;
import com.wechat.pay.java.core.util.IOUtil;
import com.wechat.pay.java.service.file.model.FileUploadResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/** 文件上传服务 */
public class FileUploadService {

  private final HttpClient httpClient;

  private FileUploadService(HttpClient httpClient) {
    this.httpClient = requireNonNull(httpClient);
  }

  /** FileUploadService构造器 */
  public static class Builder {

    private HttpClient httpClient;

    public Builder config(Config config) {
      this.httpClient = new DefaultHttpClientBuilder().config(config).build();
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = requireNonNull(httpClient);
      return this;
    }

    public FileUploadService build() {
      return new FileUploadService(httpClient);
    }
  }

  /**
   * 上传视频
   *
   * @param uploadPath 上传路径
   * @param meta 媒体文件元信息
   * @param videoPath 视频文件的绝对路径
   * @return 上传结果
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   * @throws IOException 读取字节失败、关闭流失败等。
   */
  public FileUploadResponse uploadVideo(String uploadPath, String meta, String videoPath)
      throws IOException {
    File file = new File(videoPath);
    try (FileInputStream inputStream = new FileInputStream(file)) {
      return uploadFile(uploadPath, meta, file.getName(), IOUtil.toByteArray(inputStream));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Upload video, file not found in videoPath:" + videoPath);
    }
  }

  /**
   * 上传视频
   *
   * @param uploadPath 上传路径
   * @param meta 媒体文件元信息
   * @param fileName 文件名
   * @param video 视频字节数组
   * @return 上传结果
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public FileUploadResponse uploadVideo(
      String uploadPath, String meta, String fileName, byte[] video) {
    return uploadFile(uploadPath, meta, fileName, video);
  }

  /**
   * 上传图片
   *
   * @param uploadPath 上传路径
   * @param meta 媒体文件元信息
   * @param imagePath 图片文件的绝对路径
   * @return 上传结果
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   * @throws IOException 读取图片失败、关闭图片流失败。
   */
  public FileUploadResponse uploadImage(String uploadPath, String meta, String imagePath)
      throws IOException {
    File file = new File(imagePath);
    try (FileInputStream inputStream = new FileInputStream(file)) {
      return uploadFile(uploadPath, meta, file.getName(), IOUtil.toByteArray(inputStream));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Upload image, file not found in imagePath:" + imagePath);
    }
  }

  /**
   * 上传图片
   *
   * @param uploadPath 上传路径
   * @param meta 媒体文件元信息
   * @param fileName 文件名
   * @param image 图片字节数组
   * @return 上传结果
   * @throws HttpException 发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException 发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException 发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public FileUploadResponse uploadImage(
      String uploadPath, String meta, String fileName, byte[] image) {
    return uploadFile(uploadPath, meta, fileName, image);
  }

  private FileUploadResponse uploadFile(
      String uploadPath, String meta, String fileName, byte[] file) {
    HttpRequest request =
        new HttpRequest.Builder()
            .addHeader(Constant.ACCEPT, " */*")
            .addHeader(Constant.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA.getValue())
            .httpMethod(HttpMethod.POST)
            .url(uploadPath)
            .body(new FileRequestBody.Builder().meta(meta).fileName(fileName).file(file).build())
            .build();
    return httpClient.execute(request, FileUploadResponse.class).getServiceResponse();
  }
}
