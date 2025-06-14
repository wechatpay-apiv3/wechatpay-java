package com.wechat.pay.java.service.transferbatch.model;

import com.google.gson.annotations.SerializedName;
import com.wechat.pay.java.core.util.GsonUtil;

/** TransferBatchNotification */
public class TransferBatchNotification {
  /** 商户号 说明：微信支付分配的商户号 */
  @SerializedName("mchid")
  private String mchid;

  /** 商家批次单号 说明：商户系统内部的商家批次单号，在商户系统内部唯一 */
  @SerializedName("out_batch_no")
  private String outBatchNo;

  /** 微信批次单号 说明：微信批次单号，微信商家转账系统返回的唯一标识 */
  @SerializedName("batch_id")
  private String batchId;

  /**
   * 批次状态 说明：WAIT_PAY: 待付款确认。需要付款出资商户在商家助手小程序或服务商助手小程序进行付款确认
   * ACCEPTED:已受理。批次已受理成功，若发起批量转账的30分钟后，转账批次单仍处于该状态，可能原因是商户账户余额不足等。商户可查询账户资金流水，若该笔转账批次单的扣款已经发生，则表示批次已经进入转账中，请再次查单确认
   * PROCESSING:转账中。已开始处理批次内的转账明细单 FINISHED:已完成。批次内的所有转账明细单都已处理完成 CLOSED:已关闭。可查询具体的批次关闭原因确认
   */
  @SerializedName("batch_status")
  private String batchStatus;

  /** 批次关闭原因 说明：如果批次单状态为“CLOSED”（已关闭），则有关闭原因 */
  @SerializedName("close_reason")
  private CloseReasonType closeReason;

  /** 转账总金额 说明：转账金额单位为“分” */
  @SerializedName("total_amount")
  private Long totalAmount;

  /** 转账总笔数 说明：一个转账批次单最多发起三千笔转账 */
  @SerializedName("total_num")
  private Integer totalNum;

  /** 转账成功金额 说明：转账成功的金额，单位为“分”。当批次状态为“PROCESSING”（转账中）时，转账成功金额随时可能变化 */
  @SerializedName("success_amount")
  private Long successAmount;

  /** 转账成功笔数 说明：转账成功的笔数。当批次状态为“PROCESSING”（转账中）时，转账成功笔数随时可能变化 */
  @SerializedName("success_num")
  private Integer successNum;

  /** 转账失败金额 说明：转账失败的金额，单位为“分” */
  @SerializedName("fail_amount")
  private Long failAmount;

  /** 转账失败笔数 说明：转账失败的笔数 */
  @SerializedName("fail_num")
  private Integer failNum;

  /**
   * 批次更新时间 说明：遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，
   * yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC
   * 8小时，即北京时间）。 例如：2015-05-20T13:29:35+08:00表示北京时间2015年05月20日13点29分35秒。
   */
  @SerializedName("update_time")
  private String updateTime;

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  public String getOutBatchNo() {
    return outBatchNo;
  }

  public void setOutBatchNo(String outBatchNo) {
    this.outBatchNo = outBatchNo;
  }

  public String getBatchId() {
    return batchId;
  }

  public void setBatchId(String batchId) {
    this.batchId = batchId;
  }

  public String getBatchStatus() {
    return batchStatus;
  }

  public void setBatchStatus(String batchStatus) {
    this.batchStatus = batchStatus;
  }

  public CloseReasonType getCloseReason() {
    return closeReason;
  }

  public void setCloseReason(CloseReasonType closeReason) {
    this.closeReason = closeReason;
  }

  public Long getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Long totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Integer getTotalNum() {
    return totalNum;
  }

  public void setTotalNum(Integer totalNum) {
    this.totalNum = totalNum;
  }

  public Long getSuccessAmount() {
    return successAmount;
  }

  public void setSuccessAmount(Long successAmount) {
    this.successAmount = successAmount;
  }

  public Integer getSuccessNum() {
    return successNum;
  }

  public void setSuccessNum(Integer successNum) {
    this.successNum = successNum;
  }

  public Long getFailAmount() {
    return failAmount;
  }

  public void setFailAmount(Long failAmount) {
    this.failAmount = failAmount;
  }

  public Integer getFailNum() {
    return failNum;
  }

  public void setFailNum(Integer failNum) {
    this.failNum = failNum;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
