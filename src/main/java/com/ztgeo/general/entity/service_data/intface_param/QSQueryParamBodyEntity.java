package com.ztgeo.general.entity.service_data.intface_param;

import java.io.Serializable;

/*
 * 不动产交易契税缴纳情况查询参数实体
 */
public class QSQueryParamBodyEntity implements Serializable {
    private String qsh;         //契税号
    private String htbah;
    private String htbh;
    private String bdczh;
    private String fczh;
    private String tdzh;
    private String bdcdyh;
    private String bdcyibm;
    private String bdczl;
    private String qlrmc;
    private String qlrzjh;
    private String qlrzjlx;

    public String getQsh() {
        return qsh;
    }

    public void setQsh(String qsh) {
        this.qsh = qsh;
    }

    public String getHtbah() {
        return htbah;
    }

    public void setHtbah(String htbah) {
        this.htbah = htbah;
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getBdczh() {
        return bdczh;
    }

    public void setBdczh(String bdczh) {
        this.bdczh = bdczh;
    }

    public String getFczh() {
        return fczh;
    }

    public void setFczh(String fczh) {
        this.fczh = fczh;
    }

    public String getTdzh() {
        return tdzh;
    }

    public void setTdzh(String tdzh) {
        this.tdzh = tdzh;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getBdcyibm() {
        return bdcyibm;
    }

    public void setBdcyibm(String bdcyibm) {
        this.bdcyibm = bdcyibm;
    }

    public String getBdczl() {
        return bdczl;
    }

    public void setBdczl(String bdczl) {
        this.bdczl = bdczl;
    }

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

    public String getQlrzjlx() {
        return qlrzjlx;
    }

    public void setQlrzjlx(String qlrzjlx) {
        this.qlrzjlx = qlrzjlx;
    }
}
