package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;

public class SynNewQLREntity implements Serializable {
    private String obligeeName;
    private String obligeeId;
    private String obligeeIdType;
    private String commonWay;
    private String sharedShare;

    public String getObligeeName() {
        return obligeeName;
    }

    public void setObligeeName(String obligeeName) {
        this.obligeeName = obligeeName;
    }

    public String getObligeeId() {
        return obligeeId;
    }

    public void setObligeeId(String obligeeId) {
        this.obligeeId = obligeeId;
    }

    public String getObligeeIdType() {
        return obligeeIdType;
    }

    public void setObligeeIdType(String obligeeIdType) {
        this.obligeeIdType = obligeeIdType;
    }

    public String getCommonWay() {
        return commonWay;
    }

    public void setCommonWay(String commonWay) {
        this.commonWay = commonWay;
    }

    public String getSharedShare() {
        return sharedShare;
    }

    public void setSharedShare(String sharedShare) {
        this.sharedShare = sharedShare;
    }
}
