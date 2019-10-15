package com.ztgeo.general.entity.service_data.net_sign_organization;

import java.io.Serializable;

/*
 * 合同买卖方信息
 */
public class NetSignHTQLRInfoEntity implements Serializable {
    private String qlrid;
    private String htid;
    private String htbah;
    private String fwlx;
    private String qlrmc;
    private String qlrlx; //买卖方类型
    private String zjlb;
    private String zjhm;
    private String qlrxh;


    public String getQlrid() {
        return qlrid;
    }

    public void setQlrid(String qlrid) {
        this.qlrid = qlrid;
    }

    public String getHtid() {
        return htid;
    }

    public void setHtid(String htid) {
        this.htid = htid;
    }

    public String getHtbah() {
        return htbah;
    }

    public void setHtbah(String htbah) {
        this.htbah = htbah;
    }

    public String getFwlx() {
        return fwlx;
    }

    public void setFwlx(String fwlx) {
        this.fwlx = fwlx;
    }

    public String getQlrmc() {
        return qlrmc;
    }

    public void setQlrmc(String qlrmc) {
        this.qlrmc = qlrmc;
    }

    public String getQlrlx() {
        return qlrlx;
    }

    public void setQlrlx(String qlrlx) {
        this.qlrlx = qlrlx;
    }

    public String getZjlb() {
        return zjlb;
    }

    public void setZjlb(String zjlb) {
        this.zjlb = zjlb;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getQlrxh() {
        return qlrxh;
    }

    public void setQlrxh(String qlrxh) {
        this.qlrxh = qlrxh;
    }
}
