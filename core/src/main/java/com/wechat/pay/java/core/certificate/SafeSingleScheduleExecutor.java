package com.wechat.pay.java.core.certificate;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/** 安全的单线程池 */
public class SafeSingleScheduleExecutor extends ScheduledThreadPoolExecutor {

  private static final int MAX_QUEUE_CAPACITY = 1; // 最大队列数量
  private static final int MAXIMUM_POOL_SIZE = 1; // 最大线程池数量
  private static final int CORE_POOL_SIZE = 1; // 核心线程池大小

  public SafeSingleScheduleExecutor() {
    super(CORE_POOL_SIZE);
    super.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
  }

  @Override
  public ScheduledFuture<?> scheduleAtFixedRate(
      @NotNull Runnable command, long initialDelay, long period, @NotNull TimeUnit unit) {
    if (getQueue().size() < MAX_QUEUE_CAPACITY) {
      return super.scheduleAtFixedRate(command, initialDelay, period, unit);
    } else {
      throw new RejectedExecutionException("当前任务数量超过最大队列最大数量");
    }
  }
}
