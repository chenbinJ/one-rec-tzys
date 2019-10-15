package com.ztgeo.general.entity.service_data.sys_data;

import com.ztgeo.general.entity.service_data.page.PageRequestBean;

import java.io.Serializable;

public class ExampleSearchParams extends PageRequestBean implements Serializable {
    private String receiptNumber;
    private String businessType;
    private String approveType;
    private String participant;
    private String receiptDepart;
    private String receiptTimeStart;
    private String receiptTimeEnd;

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getApproveType() {
        return approveType;
    }

    public void setApproveType(String approveType) {
        this.approveType = approveType;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getReceiptDepart() {
        return receiptDepart;
    }

    public void setReceiptDepart(String receiptDepart) {
        this.receiptDepart = receiptDepart;
    }

    public String getReceiptTimeStart() {
        return receiptTimeStart;
    }

    public void setReceiptTimeStart(String receiptTimeStart) {
        if(receiptTimeStart!=null && receiptTimeStart.length()==10) {
            this.receiptTimeStart = receiptTimeStart;
        }
    }

    public String getReceiptTimeEnd() {
        return receiptTimeEnd;
    }

    public void setReceiptTimeEnd(String receiptTimeEnd) {
        if(receiptTimeEnd!=null && receiptTimeEnd.length()==10) {
            this.receiptTimeEnd = receiptTimeEnd;
        }
    }
}
