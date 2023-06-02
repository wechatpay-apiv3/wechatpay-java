package com.wechat.pay.java.core.http;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** HTTP常量 */
public final class Constant {

  /**
   * User-Agent头部值格式：WechatPay-Java/版本 操作系统/版本 Java/版本 Credential/Credential信息 Validator/Validator信息
   * HttpClient信息 示例： WechatPay-Java/0.0.1 (Linux/3.10.0-957.el7.x86_64) Java/1.8.0_222
   * Crendetial/WechatPay2Crendetial Validator/WechatPay2Validator okhttp3/4.9.3
   */
  public static final String USER_AGENT_FORMAT =
      "WechatPay-Java/%s (%s) Java/%s Credential/%s Validator/%s %s";

  public static final String OS =
      System.getProperty("os.name") + "/" + System.getProperty("os.version");
  public static final String VERSION = System.getProperty("java.version");
  public static final String AUTHORIZATION = "Authorization";
  public static final String REQUEST_ID = "Request-ID";
  public static final String WECHAT_PAY_SERIAL = "Wechatpay-Serial";
  public static final String WECHAT_PAY_SIGNATURE = "Wechatpay-Signature";
  public static final String WECHAT_PAY_TIMESTAMP = "Wechatpay-Timestamp";
  public static final String WECHAT_PAY_NONCE = "Wechatpay-Nonce";
  public static final String USER_AGENT = "User-Agent";
  public static final String ACCEPT = "Accept";
  public static final String CONTENT_TYPE = "Content-Type";

  public static final List<String> PRIMARY_API_DOMAIN =
      Collections.unmodifiableList(Arrays.asList("api.mch.weixin.qq.com", "api.wechatpay.cn"));
  public static final String SECONDARY_API_DOMAIN = "api2.wechatpay.cn";

  private Constant() {}
}
