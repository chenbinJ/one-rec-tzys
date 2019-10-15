package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;

public class SynNewDYQREntity implements Serializable {
    private String mortgageeName;
    private String mortgageeId;
    private String mortgageeIdType;

    public String getMortgageeName() {
        return mortgageeName;
    }

    public void setMortgageeName(String mortgageeName) {
        this.mortgageeName = mortgageeName;
    }

    public String getMortgageeId() {
        return mortgageeId;
    }

    public void setMortgageeId(String mortgageeId) {
        this.mortgageeId = mortgageeId;
    }

    public String getMortgageeIdType() {
        return mortgageeIdType;
    }

    public void setMortgageeIdType(String mortgageeIdType) {
        this.mortgageeIdType = mortgageeIdType;
    }
}
