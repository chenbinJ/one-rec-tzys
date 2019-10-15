package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;

public class SynNewBizInfoEntityRevoke implements Serializable {
    private String revokeApplyDate;
    private String warrantId; //他项权证明号

    public String getRevokeApplyDate() {
        return revokeApplyDate;
    }

    public void setRevokeApplyDate(String revokeApplyDate) {
        this.revokeApplyDate = revokeApplyDate;
    }

    public String getWarrantId() {
        return warrantId;
    }

    public void setWarrantId(String warrantId) {
        this.warrantId = warrantId;
    }
}
