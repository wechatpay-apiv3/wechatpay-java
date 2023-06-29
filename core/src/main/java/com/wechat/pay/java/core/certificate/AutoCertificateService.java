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
  private static ScheduledFuture<?> future;

  private static int updateCount;

  public static void register(String merchantId, String type, CertificateDownloader downloader) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    Runnable worker =
        () -> {
          Map<String, X509Certificate> result = downloader.download();
          certificateMap.put(key, result);
        };

    // 如果已经存在，就覆盖
    // 但是，有可能新的配置有问题，导致更新失败
    downloadWorkerMap.replace(key, worker);

    // 第一次时，先下载好证书
    downloadWorkerMap.computeIfAbsent(
        key,
        k -> {
          // 如果失败，会抛出异常
          worker.run();
          return worker;
        });

    start(defaultUpdateInterval);
  }

  public static void unregister(String merchantId, String type) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    downloadWorkerMap.remove(key);
  }

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
          } catch (Exception e) {
            log.error("Download and update wechatpay certificate {} failed", k);
          }
        });
    updateCount++;
  }

  private static String calculateDownloadWorkerMapKey(String merchantId, String type) {
    return merchantId + type;
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

  public static X509Certificate getCertificate(
      String merchantId, String type, String serialNumber) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    return certificateMap.get(key).get(serialNumber);
  }

  public static X509Certificate getAvailableCertificate(String merchantId, String type) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    return getAvailableCertificate(certificateMap.get(key));
  }
}
