package com.ztgeo.general.entity.extend;

public class Base64Picture {

    private String zjhm;//身份证号

    private String xm;//姓名

    private String picInSite;//现场图片

    private String picInIdentityCard;//身份证图片

    private String compateResult;//识别度

    private String csrq;//出生日期

    private String mz;//民族

    private String dh;//电话


    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getPicInSite() {
        return picInSite;
    }

    public void setPicInSite(String picInSite) {
        this.picInSite = picInSite;
    }

    public String getPicInIdentityCard() {
        return picInIdentityCard;
    }

    public void setPicInIdentityCard(String picInIdentityCard) {
        this.picInIdentityCard = picInIdentityCard;
    }

    public String getCompateResult() {
        return compateResult;
    }

    public void setCompateResult(String compateResult) {
        this.compateResult = compateResult;
    }
}
