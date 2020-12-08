package com.ztgeo.general.entity.tz_entity;

import java.io.Serializable;

public class TZBusinessData implements Serializable {
    private String id;
    private String receiptNumber;
    private String oldBusinessNumber;
    private String canDoDepart;
    private String businessType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getOldBusinessNumber() {
        return oldBusinessNumber;
    }

    public void setOldBusinessNumber(String oldBusinessNumber) {
        this.oldBusinessNumber = oldBusinessNumber;
    }

    public String getCanDoDepart() {
        return canDoDepart;
    }

    public void setCanDoDepart(String canDoDepart) {
        this.canDoDepart = canDoDepart;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
