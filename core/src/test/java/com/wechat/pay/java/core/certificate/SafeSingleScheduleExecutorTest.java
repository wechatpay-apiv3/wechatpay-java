package com.wechat.pay.java.core.certificate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class SafeSingleScheduleExecutorTest {

  @Test
  void scheduleAtFixRate_success() {
    SafeSingleScheduleExecutor executor = SafeSingleScheduleExecutor.getInstance();
    Runnable runnable = () -> System.out.println("hello world");
    executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.MINUTES);
    assertNotNull(executor);
  }
}
