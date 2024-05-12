// Copyright 2021 Tencent Inc. All rights reserved.
//
// 营销商家券对外API
//
// No description provided (generated by Openapi Generator
// https://github.com/openapitools/openapi-generator)
//
// API version: 0.0.11

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.merchantexclusivecoupon.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** FinderInfo */
public class FinderInfo {
  /**
   * 视频号ID
   * 说明：关联视频号将展示在优惠券详情的顶部右侧，作为跳转去视频号的入口，入参参数请配置视频号id，请前往[视频号助手](https://channels.weixin.qq.com)管理查看视频号ID
   */
  @SerializedName("finder_id")
  private String finderId;

  /**
   * 视频号视频ID
   * 说明：券详情视频内容，支持配置关联视频号下的具体视频内容，入参参数请配置视频id，请前往[视频号助手](https://channels.weixin.qq.com)管理后台复制具体视频ID
   */
  @SerializedName("finder_video_id")
  private String finderVideoId;

  /**
   * 视频号封面图 说明：截取的视频号图片将在券到期提醒消息、券详情中展示。 1.图片尺寸：716像素（宽）\\*402像素（高）；图片大小不超过2M，支持JPG/PNG格式。
   * 2.仅支持通过《[图片上传API](https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter9_0_1.shtml)》接口获取的图片URL地址。
   */
  @SerializedName("finder_video_cover_image_url")
  private String finderVideoCoverImageUrl;

  public String getFinderId() {
    return finderId;
  }

  public void setFinderId(String finderId) {
    this.finderId = finderId;
  }

  public String getFinderVideoId() {
    return finderVideoId;
  }

  public void setFinderVideoId(String finderVideoId) {
    this.finderVideoId = finderVideoId;
  }

  public String getFinderVideoCoverImageUrl() {
    return finderVideoCoverImageUrl;
  }

  public void setFinderVideoCoverImageUrl(String finderVideoCoverImageUrl) {
    this.finderVideoCoverImageUrl = finderVideoCoverImageUrl;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FinderInfo {\n");
    sb.append("    finderId: ").append(toIndentedString(finderId)).append("\n");
    sb.append("    finderVideoId: ").append(toIndentedString(finderVideoId)).append("\n");
    sb.append("    finderVideoCoverImageUrl: ")
        .append(toIndentedString(finderVideoCoverImageUrl))
        .append("\n");
    sb.append("}");
    return sb.toString();
  }
}
