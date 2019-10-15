package com.ztgeo.general.entity.service_data.intface_param;

import java.io.Serializable;
/*
 * 银行抵押合同信息查询参数类
 */
public class BankDYHTParamBodyEntity implements Serializable {
    private String htid;            //合同id
    private String htbh;            //合同編號
    private String xgzh;            //相关证号
    private String dyrmc;           //抵押人名称
    private String dyrzjlx;         //抵押人证件类型
    private String dyrzjhm;         //抵押人证件号码
    private String fwzl;            //房屋坐落

    public String getHtid() {
        return htid;
    }

    public void setHtid(String htid) {
        this.htid = htid;
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getXgzh() {
        return xgzh;
    }

    public void setXgzh(String xgzh) {
        this.xgzh = xgzh;
    }

    public String getDyrmc() {
        return dyrmc;
    }

    public void setDyrmc(String dyrmc) {
        this.dyrmc = dyrmc;
    }

    public String getDyrzjlx() {
        return dyrzjlx;
    }

    public void setDyrzjlx(String dyrzjlx) {
        this.dyrzjlx = dyrzjlx;
    }

    public String getDyrzjhm() {
        return dyrzjhm;
    }

    public void setDyrzjhm(String dyrzjhm) {
        this.dyrzjhm = dyrzjhm;
    }

    public String getFwzl() {
        return fwzl;
    }

    public void setFwzl(String fwzl) {
        this.fwzl = fwzl;
    }
}
