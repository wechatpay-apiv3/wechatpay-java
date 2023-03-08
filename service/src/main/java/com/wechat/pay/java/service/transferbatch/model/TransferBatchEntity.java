// Copyright 2021 Tencent Inc. All rights reserved.
//
// 商家转账对外API
//
// * 场景及业务流程：     商户可通过该产品实现同时向多个用户微信零钱进行转账的操作，可用于发放奖金补贴、佣金货款结算、员工报销等场景。
// [https://pay.weixin.qq.com/index.php/public/product/detail?pid=108&productType=0](https://pay.weixin.qq.com/index.php/public/product/detail?pid=108&productType=0) * 接入步骤：     * 商户在微信支付商户平台开通“批量转账到零钱”产品权限，并勾选“使用API方式发起转账”。     * 调用批量转账接口，对多个用户微信零钱发起转账。     * 调用查询批次接口，可获取到转账批次详情及当前状态。     * 调用查询明细接口，可获取到单条转账明细详情及当前状态。
//
// API version: 1.0.4

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.transferbatch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

/** TransferBatchEntity */
public class TransferBatchEntity {
  /** 转账批次单 说明：转账批次单基本信息 */
  @SerializedName("transfer_batch")
  private TransferBatchGet transferBatch;
  /** 转账明细单列表 说明：当批次状态为“FINISHED”（已完成），且成功查询到转账明细单时返回。包括微信明细单号、明细状态信息 */
  @SerializedName("transfer_detail_list")
  private List<TransferDetailCompact> transferDetailList;

  public TransferBatchGet getTransferBatch() {
    return transferBatch;
  }

  public void setTransferBatch(TransferBatchGet transferBatch) {
    this.transferBatch = transferBatch;
  }

  public List<TransferDetailCompact> getTransferDetailList() {
    return transferDetailList;
  }

  public void setTransferDetailList(List<TransferDetailCompact> transferDetailList) {
    this.transferDetailList = transferDetailList;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferBatchEntity {\n");
    sb.append("    transferBatch: ").append(toIndentedString(transferBatch)).append("\n");
    sb.append("    transferDetailList: ").append(toIndentedString(transferDetailList)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
