package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;

public class SJ_Spaciel_Position_Power implements Serializable {
    private String powerId;
    private String positionId;
    private String ext;

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
