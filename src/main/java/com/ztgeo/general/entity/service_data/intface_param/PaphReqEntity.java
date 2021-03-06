package com.ztgeo.general.entity.service_data.intface_param;

import java.io.Serializable;

public class PaphReqEntity implements Serializable {
    private String qlrmc;
    private String qlrzjh;
    private String bdczl;
    private String dyrmc;
    private String dyrzjh;
    private String dyqrmc;
    private String dyqrzjh;
    private String sfzfj;               //身份证附件

    public String getQlrmc() {
        return qlrmc;
    }

    public void setQlrmc(String qlrmc) {
        this.qlrmc = qlrmc;
    }

    public String getQlrzjh() {
        return qlrzjh;
    }

    public void setQlrzjh(String qlrzjh) {
        this.qlrzjh = qlrzjh;
    }

    public String getBdczl() {
        return bdczl;
    }

    public void setBdczl(String bdczl) {
        this.bdczl = bdczl;
    }

    public String getDyrmc() {
        return dyrmc;
    }

    public void setDyrmc(String dyrmc) {
        this.dyrmc = dyrmc;
    }

    public String getDyrzjh() {
        return dyrzjh;
    }

    public void setDyrzjh(String dyrzjh) {
        this.dyrzjh = dyrzjh;
    }

    public String getDyqrmc() {
        return dyqrmc;
    }

    public void setDyqrmc(String dyqrmc) {
        this.dyqrmc = dyqrmc;
    }

    public String getDyqrzjh() {
        return dyqrzjh;
    }

    public void setDyqrzjh(String dyqrzjh) {
        this.dyqrzjh = dyqrzjh;
    }

    public String getSfzfj() {
        return sfzfj;
    }

    public void setSfzfj(String sfzfj) {
        this.sfzfj = sfzfj;
    }
}
