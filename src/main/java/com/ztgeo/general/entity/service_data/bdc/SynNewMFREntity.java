package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;

/*
 *  卖方人
 */
public class SynNewMFREntity implements Serializable {
    private String salerName;
    private String salerId;
    private String salerIdType;

    public String getSalerName() {
        return salerName;
    }

    public void setSalerName(String salerName) {
        this.salerName = salerName;
    }

    public String getSalerId() {
        return salerId;
    }

    public void setSalerId(String salerId) {
        this.salerId = salerId;
    }

    public String getSalerIdType() {
        return salerIdType;
    }

    public void setSalerIdType(String salerIdType) {
        this.salerIdType = salerIdType;
    }
}
