package com.wechat.pay.java.core.certificate;

import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 定时更新证书的服务，它是一个由静态函数构成的工具类 */
public class AutoCertificateService {
  private static final Logger log = LoggerFactory.getLogger(AutoCertificateService.class);
  protected static final int UPDATE_INTERVAL_MINUTE = 60;
  private static final Duration defaultUpdateInterval = Duration.ofMinutes(UPDATE_INTERVAL_MINUTE);
  private static final ConcurrentHashMap<String, Map<String, X509Certificate>> certificateMap =
      new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String, Runnable> downloadWorkerMap =
      new ConcurrentHashMap<>();
  private static final ScheduledThreadPoolExecutor serviceExecutor =
      new ScheduledThreadPoolExecutor(
          1,
          new ThreadFactory() {

            private final AtomicInteger threadCount = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
              Thread t =
                  new Thread(r, "auto-certificate-service-daemon-" + threadCount.incrementAndGet());
              // 用户线程执行完成后不会阻止 JVM 退出
              t.setDaemon(true);
              return t;
            }
          });

  static {
    // 取消时立即从工作队列中删除
    serviceExecutor.setRemoveOnCancelPolicy(true);
  }

  private static ScheduledFuture<?> future;

  private static int updateCount;

  private AutoCertificateService() {
    throw new IllegalStateException("this class cannot be instantiated");
  }

  /**
   * 注册证书下载任务 如果是第一次注册，会先下载证书。如果能成功下载，再保存下载器，供定时更新证书使用。如果下载失败，会抛出异常。
   * 如果已经注册过，当前传入的下载器将覆盖之前的下载器。如果当前下载器不能下载证书，定时更新证书会失败。
   *
   * @param merchantId 商户号
   * @param type 调用方自定义的证书类型，例如 RSA/ShangMi
   * @param downloader 证书下载器
   */
  public static void register(String merchantId, String type, CertificateDownloader downloader) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    Runnable worker =
        () -> {
          Map<String, X509Certificate> result = downloader.download();
          certificateMap.put(key, result);
        };

    // 下载证书，以验证配置是正确的
    // 如果错误将抛出异常，fast-fail
    worker.run();
    // 更新配置
    downloadWorkerMap.put(key, worker);

    start(defaultUpdateInterval);
  }

  /**
   * 注销证书下载任务
   *
   * @param merchantId 商户号
   * @param type 调用方自定义的证书类型，应等于 `register()` 时的值
   */
  public static void unregister(String merchantId, String type) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    downloadWorkerMap.remove(key);
  }

  /** 清理所有已注册的下载器和已下载的证书，并取消定时更新证书的动作。 */
  public static void shutdown() {
    downloadWorkerMap.clear();
    certificateMap.clear();
    synchronized (AutoCertificateService.class) {
      if (future != null) {
        future.cancel(false);
        future = null;
      }
    }
  }

  /**
   * 启动更新证书的周期性动作
   *
   * @param updateInterval 更新证书的周期
   */
  public static void start(Duration updateInterval) {
    synchronized (AutoCertificateService.class) {
      if (future == null) {
        future =
            serviceExecutor.scheduleAtFixedRate(
                AutoCertificateService::update,
                updateInterval.toMillis() / TimeUnit.SECONDS.toMillis(1),
                updateInterval.toMillis() / TimeUnit.SECONDS.toMillis(1),
                TimeUnit.SECONDS);
      }
    }
  }

  private static void update() {
    log.info("Begin update Certificates. total updates: {}", updateCount);
    downloadWorkerMap.forEach(
        (k, v) -> {
          try {
            v.run();
            log.info("update wechatpay certificate {} done", k);
          } catch (Exception e) {
            log.error("Download and update wechatpay certificate {} failed", k);
          }
        });
    updateCount++;
  }

  private static String calculateDownloadWorkerMapKey(String merchantId, String type) {
    return merchantId + "-" + type;
  }

  private static X509Certificate getAvailableCertificate(
      Map<String, X509Certificate> certificateMap) {
    // 假设拿到的都是可用的，选取一个能用最久的
    X509Certificate longest = null;
    for (X509Certificate item : certificateMap.values()) {
      if (longest == null || item.getNotAfter().after(longest.getNotAfter())) {
        longest = item;
      }
    }
    return longest;
  }

  // 根据证书序列号获取证书
  public static X509Certificate getCertificate(
      String merchantId, String type, String serialNumber) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    return certificateMap.get(key).get(serialNumber);
  }

  // 获取最新可用的微信支付平台证书
  public static X509Certificate getAvailableCertificate(String merchantId, String type) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    return getAvailableCertificate(certificateMap.get(key));
  }
}
