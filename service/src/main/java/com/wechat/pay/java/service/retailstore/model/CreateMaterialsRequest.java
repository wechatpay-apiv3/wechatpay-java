// Copyright 2021 Tencent Inc. All rights reserved.
//
// 营销加价购对外API
//
// 指定服务商可通过该接口报名加价购活动、查询某个区域内的加价购活动列表、锁定加价活动购资格以及解锁加价购活动资格。
//
// API version: 1.3.0

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.retailstore.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/** CreateMaterialsRequest */
public class CreateMaterialsRequest {
  /** 品牌ID 说明：品牌ID */
  @SerializedName("brand_id")
  @Expose(serialize = false)
  private String brandId;
  /** 请求业务单据 说明：商户添加业务代理信息凭据号，商户侧需保持唯一性。可包含英文字母，数字，\\\\|，\\\\_，\\\\*，\\\\-等内容，不允许出现其他不合法符号。 */
  @SerializedName("out_request_no")
  private String outRequestNo;
  /** 生成物料码数量 说明：生成物料码数量 */
  @SerializedName("material_num")
  private Long materialNum;

  public String getBrandId() {
    return brandId;
  }

  public void setBrandId(String brandId) {
    this.brandId = brandId;
  }

  public String getOutRequestNo() {
    return outRequestNo;
  }

  public void setOutRequestNo(String outRequestNo) {
    this.outRequestNo = outRequestNo;
  }

  public Long getMaterialNum() {
    return materialNum;
  }

  public void setMaterialNum(Long materialNum) {
    this.materialNum = materialNum;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateMaterialsRequest {\n");
    sb.append("    brandId: ").append(toIndentedString(brandId)).append("\n");
    sb.append("    outRequestNo: ").append(toIndentedString(outRequestNo)).append("\n");
    sb.append("    materialNum: ").append(toIndentedString(materialNum)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
