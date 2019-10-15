package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;

public class SynNewRealEstateUnitInfo implements Serializable {
    //不动产单元号
    private String realEstateUnitId;
    //户编号
    private String householdId;
    //坐落
    private String sit;

    public String getRealEstateUnitId() {
        return realEstateUnitId;
    }

    public void setRealEstateUnitId(String realEstateUnitId) {
        this.realEstateUnitId = realEstateUnitId;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    public String getSit() {
        return sit;
    }

    public void setSit(String sit) {
        this.sit = sit;
    }
}
