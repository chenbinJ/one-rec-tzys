package com.ztgeo.general.entity.service_data.intface_param;

import java.io.Serializable;

public class PersonnelUnitReqEntity implements Serializable {
    private String mc;
    private String zjlx;
    private String zjhm;
    private String fjlj;
    private String outerId;
    private String interfaceCode;

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getFjlj() {
        return fjlj;
    }

    public void setFjlj(String fjlj) {
        this.fjlj = fjlj;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public String getInterfaceCode() {
        return interfaceCode;
    }

    public void setInterfaceCode(String interfaceCode) {
        this.interfaceCode = interfaceCode;
    }
}
