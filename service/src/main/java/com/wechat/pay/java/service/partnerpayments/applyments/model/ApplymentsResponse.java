package com.wechat.pay.java.service.partnerpayments.applyments.model;

import com.google.gson.annotations.SerializedName;

public class ApplymentsResponse {
    /** 微信支付申请单号 */
    @SerializedName("applyment_id")
    private Integer applymentId;
    
    /** 业务申请编号 */
    @SerializedName("out_request_no")
    private String outRequestNo;

    public Integer getApplymentId() {
        return applymentId;
    }

    public void setApplymentId(Integer applymentId) {
        this.applymentId = applymentId;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }
}
