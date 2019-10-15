package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;
/*
 * 债务人
 */
public class SynNewZWREntity implements Serializable {
    private String obligorName;
    private String obligorId;
    private String obligorIdType;

    public String getObligorIdType() {
        return obligorIdType;
    }

    public void setObligorIdType(String obligorIdType) {
        this.obligorIdType = obligorIdType;
    }

    public String getObligorName() {
        return obligorName;
    }

    public void setObligorName(String obligorName) {
        this.obligorName = obligorName;
    }

    public String getObligorId() {
        return obligorId;
    }

    public void setObligorId(String obligorId) {
        this.obligorId = obligorId;
    }
}
