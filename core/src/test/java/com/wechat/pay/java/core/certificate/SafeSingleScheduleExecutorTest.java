package com.wechat.pay.java.core.certificate;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class SafeSingleScheduleExecutorTest {

  @Test
  public void scheduleAtFixRate_success() {
    SafeSingleScheduleExecutor executor = new SafeSingleScheduleExecutor();
    Runnable runnable = () -> System.out.println("hello world");
    executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.MINUTES);
  }
}
