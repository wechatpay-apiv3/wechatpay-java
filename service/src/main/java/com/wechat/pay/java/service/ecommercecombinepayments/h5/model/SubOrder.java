package com.wechat.pay.java.service.ecommercecombinepayments.h5.model;

import com.google.gson.annotations.SerializedName;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

public class SubOrder {

  /** 子商户号 说明：子单发起方商户号，必须与发起方appid有绑定关系。服务商和电商模式下，传服务商商户号。 */
  @SerializedName("mchid")
  private String mchid;

  /** 附加数据 说明：附加数据 */
  @SerializedName("attach")
  private String attach;

  /** amount */
  @SerializedName("amount")
  private Amount amount;

  /** 子单商户订单号 */
  @SerializedName("out_trade_no")
  private String outTradeNo;

  /** 订单优惠标记 说明：商品标记，代金券或立减优惠功能的参数。 */
  @SerializedName("goods_tag")
  private String goodsTag;

  /**
   * 二级商户号
   * 说明：二级商户商户号，由微信支付生成并下发。
   * 服务商子商户的商户号，被合单方。直连商户不用传二级商户号。
   * 注意：仅适用于电商平台,服务商*/
  @SerializedName("sub_mchid")
  private String subMchid;

  /** 商品描述 说明：商品描述 */
  @SerializedName("description")
  private String description;

  /** settleInfo */
  @SerializedName("settle_info")
  private SettleInfo settleInfo;

  /** 子商户应用ID 说明：子商户申请的公众号appid */
  @SerializedName("sub_appid")
  private String subAppid;

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public Amount getAmount() {
    return amount;
  }

  public void setAmount(Amount amount) {
    this.amount = amount;
  }

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  public String getGoodsTag() {
    return goodsTag;
  }

  public void setGoodsTag(String goodsTag) {
    this.goodsTag = goodsTag;
  }

  public String getSubMchid() {
    return subMchid;
  }

  public void setSubMchid(String subMchid) {
    this.subMchid = subMchid;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SettleInfo getSettleInfo() {
    return settleInfo;
  }

  public void setSettleInfo(SettleInfo settleInfo) {
    this.settleInfo = settleInfo;
  }

  public String getSubAppid() {
    return subAppid;
  }

  public void setSubAppid(String subAppid) {
    this.subAppid = subAppid;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubOrder {\n");
    sb.append("    mchid: ").append(toIndentedString(mchid)).append("\n");
    sb.append("    attach: ").append(toIndentedString(attach)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    outTradeNo: ").append(toIndentedString(outTradeNo)).append("\n");
    sb.append("    goodsTag: ").append(toIndentedString(goodsTag)).append("\n");
    sb.append("    subMchid: ").append(toIndentedString(subMchid)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    settleInfo: ").append(toIndentedString(settleInfo)).append("\n");
    sb.append("    subAppid: ").append(toIndentedString(subAppid)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
