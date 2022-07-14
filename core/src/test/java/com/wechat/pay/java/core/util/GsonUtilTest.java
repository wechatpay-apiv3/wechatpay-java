package com.wechat.pay.java.core.util;

import com.google.gson.Gson;
import com.wechat.pay.java.core.model.TestServiceRequest;
import org.junit.Assert;
import org.junit.Test;

public class GsonUtilTest {

  @Test
  public void testCreateGson() {
    String mchid = "mchid";
    String appid = "appid";
    String outTradeNo = "out_trade_no";
    String testServiceRequestJson =
        "{\n"
            + "  \""
            + mchid
            + "\": \""
            + mchid
            + "\",\n"
            + "  \""
            + appid
            + "\": \""
            + appid
            + "\",\n"
            + "  \""
            + outTradeNo
            + "\": \""
            + outTradeNo
            + "\"\n"
            + "}";
    Gson gson = GsonUtil.getGson();
    TestServiceRequest getTestServiceRequest =
        gson.fromJson(testServiceRequestJson, TestServiceRequest.class);
    TestServiceRequest createTestServiceRequest = new TestServiceRequest();
    createTestServiceRequest.setMchid(mchid);
    createTestServiceRequest.setAppid(appid);
    createTestServiceRequest.setOutTradeNo(outTradeNo);

    Assert.assertEquals(mchid, getTestServiceRequest.getMchid());
    Assert.assertEquals(appid, getTestServiceRequest.getAppid());
    Assert.assertEquals(outTradeNo, getTestServiceRequest.getOutTradeNo());
    Assert.assertEquals(gson.toJson(createTestServiceRequest), gson.toJson(getTestServiceRequest));
  }
}
