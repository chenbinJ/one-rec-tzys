package com.ztgeo.general.entity.service_data.intface_param;

import java.io.Serializable;

/*
 * 交易合同查询参数类
 */
public class NetSignHTParamBodyEntity implements Serializable {
    private String htqy;
    private String htbah;
    private String htbh;
    private String bdcdyh;
    private String htid;
    private String fwid;
    private String fwzl;
    private String gfrmc;
    private String gfrzjh;
    private String gfrzjlx;

    public String getHtqy() {
        return htqy;
    }

    public void setHtqy(String htqy) {
        this.htqy = htqy;
    }

    public String getHtbah() {
        return htbah;
    }

    public void setHtbah(String htbah) {
        this.htbah = htbah;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getGfrmc() {
        return gfrmc;
    }

    public void setGfrmc(String gfrmc) {
        this.gfrmc = gfrmc;
    }

    public String getGfrzjh() {
        return gfrzjh;
    }

    public void setGfrzjh(String gfrzjh) {
        this.gfrzjh = gfrzjh;
    }

    public String getGfrzjlx() {
        return gfrzjlx;
    }

    public void setGfrzjlx(String gfrzjlx) {
        this.gfrzjlx = gfrzjlx;
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getHtid() {
        return htid;
    }

    public void setHtid(String htid) {
        this.htid = htid;
    }

    public String getFwid() {
        return fwid;
    }

    public void setFwid(String fwid) {
        this.fwid = fwid;
    }

    public String getFwzl() {
        return fwzl;
    }

    public void setFwzl(String fwzl) {
        this.fwzl = fwzl;
    }
}
