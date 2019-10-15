package com.ztgeo.general.entity.service_data.intface_param;

import java.io.Serializable;

//后期附件参数可以改成一个String value的Map存放
public class CertQueryForOuterServiceEntity implements Serializable {
    private String bdczh;       //不动产证号
    private String zslx;        //证书类型
    private String qlrmc;       //权利人名称
    private String zjlx;        //证件类型
    private String zjhm;        //证件号码
    private String zl;          //坐落
    private String sfzfj;       //身份证及现场照片附件
    private String cxsqbfj;     //查询申请表附件
    private String zszmfj;      //证书或证明附件

    public String getBdczh() {
        return bdczh;
    }

    public void setBdczh(String bdczh) {
        this.bdczh = bdczh;
    }

    public String getZslx() {
        return zslx;
    }

    public void setZslx(String zslx) {
        this.zslx = zslx;
    }

    public String getQlrmc() {
        return qlrmc;
    }

    public void setQlrmc(String qlrmc) {
        this.qlrmc = qlrmc;
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

    public String getZl() {
        return zl;
    }

    public void setZl(String zl) {
        this.zl = zl;
    }

    public String getSfzfj() {
        return sfzfj;
    }

    public void setSfzfj(String sfzfj) {
        this.sfzfj = sfzfj;
    }

    public String getCxsqbfj() {
        return cxsqbfj;
    }

    public void setCxsqbfj(String cxsqbfj) {
        this.cxsqbfj = cxsqbfj;
    }

    public String getZszmfj() {
        return zszmfj;
    }

    public void setZszmfj(String zszmfj) {
        this.zszmfj = zszmfj;
    }
}
