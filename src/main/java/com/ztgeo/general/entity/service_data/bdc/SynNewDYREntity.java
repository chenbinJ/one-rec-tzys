package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;

public class SynNewDYREntity implements Serializable {
    private String mortgagorName;
    private String mortgagorId;
    private String mortgagorIdType;

    public String getMortgagorName() {
        return mortgagorName;
    }

    public void setMortgagorName(String mortgagorName) {
        this.mortgagorName = mortgagorName;
    }

    public String getMortgagorId() {
        return mortgagorId;
    }

    public void setMortgagorId(String mortgagorId) {
        this.mortgagorId = mortgagorId;
    }

    public String getMortgagorIdType() {
        return mortgagorIdType;
    }

    public void setMortgagorIdType(String mortgagorIdType) {
        this.mortgagorIdType = mortgagorIdType;
    }
}
