package com.wechat.pay.java.service.transferbatch;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.transferbatch.model.TransferBatchNotification;

/**
 * 商家转账批次回调通知解析服务
 *
 * <p>它用于对 商家转账批次回调 进行验签并解析回调报文。
 */
public class TransferBatchNotificationParseService {
  private final NotificationParser parser;

  private TransferBatchNotificationParseService(NotificationParser parser) {
    this.parser = requireNonNull(parser);
  }

  /** TransferBatchNotificationParseService构造器 */
  public static class Builder {

    private NotificationConfig config;
    private NotificationParser parser;

    public TransferBatchNotificationParseService.Builder config(NotificationConfig config) {
      this.config = config;
      return this;
    }

    public TransferBatchNotificationParseService.Builder parser(NotificationParser parser) {
      this.parser = parser;
      return this;
    }

    public TransferBatchNotificationParseService build() {
      if (parser == null && config != null) {
        return new TransferBatchNotificationParseService(new NotificationParser(config));
      }
      return new TransferBatchNotificationParseService(parser);
    }
  }

  /**
   * 商家转账批次回调通知解析
   *
   * @param requestParam 解析通知所需要的请求参数
   * @return 解析后的回调报文
   * @throws MalformedMessageException 回调通知参数不正确、解析通知数据失败
   * @throws ValidationException 签名验证失败
   */
  public TransferBatchNotification transferBatch(RequestParam requestParam) {
    return parser.parse(requestParam, TransferBatchNotification.class);
  }
}
