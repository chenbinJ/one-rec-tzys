package com.ztgeo.general.entity.tz_entity;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Bdc_Fw_Info;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Bdc_Gl;

import java.io.Serializable;
import java.util.List;

public class TZBuilt implements Serializable {
    private String zh;                                                  //幢号
    private String lpmc;                                                //楼盘名称
    private String xmmc;                                                //项目名称
    private String lpzl;                                                //楼盘坐落
    private String kfqy;                                                //开发企业
    private List<SJ_Bdc_Fw_Info> bdcInfo;                               //关联的不动产信息

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getLpmc() {
        return lpmc;
    }

    public void setLpmc(String lpmc) {
        this.lpmc = lpmc;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getLpzl() {
        return lpzl;
    }

    public void setLpzl(String lpzl) {
        this.lpzl = lpzl;
    }

    public String getKfqy() {
        return kfqy;
    }

    public void setKfqy(String kfqy) {
        this.kfqy = kfqy;
    }

    public List<SJ_Bdc_Fw_Info> getBdcInfo() {
        return bdcInfo;
    }

    public void setBdcInfo(List<SJ_Bdc_Fw_Info> bdcInfo) {
        this.bdcInfo = bdcInfo;
    }
}
