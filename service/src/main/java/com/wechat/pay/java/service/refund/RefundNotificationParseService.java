package com.wechat.pay.java.service.refund;

import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.refund.model.RefundNotification;

import static java.util.Objects.requireNonNull;

/**
 * 退款回调通知解析服务
 *
 * <p>它用于对 退款回调 进行验签并解析回调报文。
 */
public class RefundNotificationParseService {
  private final NotificationParser parser;

  private RefundNotificationParseService(NotificationParser parser) {
    this.parser = requireNonNull(parser);
  }

  /** RefundNotificationParseService构造器 */
  public static class Builder {

    private NotificationConfig config;
    private NotificationParser parser;

    public RefundNotificationParseService.Builder config(NotificationConfig config) {
      this.config = config;
      return this;
    }

    public RefundNotificationParseService.Builder parser(NotificationParser parser) {
      this.parser = parser;
      return this;
    }

    public RefundNotificationParseService build() {
      if (parser == null && config != null) {
        return new RefundNotificationParseService(new NotificationParser(config));
      }
      return new RefundNotificationParseService(parser);
    }
  }

  /**
   * 退款回调通知解析
   *
   * @param requestParam 解析通知所需要的请求参数
   * @return 解析后的回调报文
   * @throws MalformedMessageException 回调通知参数不正确、解析通知数据失败
   * @throws ValidationException 签名验证失败
   */
  public RefundNotification refund(RequestParam requestParam) {
    return parser.parse(requestParam, RefundNotification.class);
  }
}
