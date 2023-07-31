package com.wechat.pay.java.service.file;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.file.model.FileUploadResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 文件上传服务使用示例 */
public class FileUploadServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String apiV3Key = "";
  public static FileUploadService fileUploadService;
  private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceExample.class);

  public static void main(String[] args) {
    // 初始化商户配置
    Config config =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util
            // 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .apiV3Key(apiV3Key)
            .build();

    // 初始化文件上传服务
    fileUploadService = new FileUploadService.Builder().config(config).build();
    // ... 调用接口
  }

  /** 图片上传 */
  public static FileUploadResponse uploadImage() {
    String uploadPath = "";
    String imagePath = "";
    String meta = "";
    FileUploadResponse fileUploadResponse = null;
    try {
      fileUploadResponse = fileUploadService.uploadImage(uploadPath, meta, imagePath);
    } catch (IOException e) {
      // ...上报监控和打印日志
      logger.error("UploadImage failed", e);
    }
    return fileUploadResponse;
  }

  /** 视频上传 */
  public static FileUploadResponse uploadVideo() {
    String uploadPath = "";
    String videoPath = "";
    String meta = "";
    FileUploadResponse fileUploadResponse = null;
    try {
      fileUploadResponse = fileUploadService.uploadVideo(uploadPath, meta, videoPath);
    } catch (IOException e) {
      // ...上报监控和打印日志
      logger.error("UploadVideo failed", e);
    }
    return fileUploadResponse;
  }
}
