package com.ztgeo.general.entity.service_data.intface_param;

import java.io.Serializable;

/*
 * 不动产查询参数类
 */
public class BDCQueryParamBodyEntity implements Serializable {
    private String bdczh;
    private String fczh;
    private String tdzh;
    private String dyzmh;
    private String ygzmh;
    private String qlrmc;
    private String qlrzjh;
    private String qlrzjlx;
    private String bdclx;
    private String bdczl;
    private String bdctybm;         //不动产表主键
    private String bdcdyh;          //不动产单元号

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

    public String getDyzmh() {
        return dyzmh;
    }

    public void setDyzmh(String dyzmh) {
        this.dyzmh = dyzmh;
    }

    public String getYgzmh() {
        return ygzmh;
    }

    public void setYgzmh(String ygzmh) {
        this.ygzmh = ygzmh;
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

    public String getBdclx() {
        return bdclx;
    }

    public void setBdclx(String bdclx) {
        this.bdclx = bdclx;
    }

    public String getBdczl() {
        return bdczl;
    }

    public void setBdczl(String bdczl) {
        this.bdczl = bdczl;
    }

    public String getBdctybm() {
        return bdctybm;
    }

    public void setBdctybm(String bdctybm) {
        this.bdctybm = bdctybm;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }
}
