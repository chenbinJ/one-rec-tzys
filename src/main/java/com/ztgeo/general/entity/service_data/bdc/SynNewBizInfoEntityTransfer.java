package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;
import java.util.List;

public class SynNewBizInfoEntityTransfer implements Serializable {
    private String realEstateId;                        //不动产证号
    private String landCertificate;                     //土地证号
    private String htbh;                                //合同编号
    private String djdl;                                //登记大类
    private String dl_val;                              //大类值
    private String commonWay;                           //共有方式
    private List<SynNewQLREntity> obligeeInfoVoList;    //权利人列表
    private List<SynNewDLREntity> agentInfoVoList;      //委托代理人列表

    public String getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(String realEstateId) {
        this.realEstateId = realEstateId;
    }

    public String getLandCertificate() {
        return landCertificate;
    }

    public void setLandCertificate(String landCertificate) {
        this.landCertificate = landCertificate;
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getDjdl() {
        return djdl;
    }

    public void setDjdl(String djdl) {
        this.djdl = djdl;
    }

    public String getDl_val() {
        return dl_val;
    }

    public void setDl_val(String dl_val) {
        this.dl_val = dl_val;
    }

    public String getCommonWay() {
        return commonWay;
    }

    public void setCommonWay(String commonWay) {
        this.commonWay = commonWay;
    }

    public List<SynNewQLREntity> getObligeeInfoVoList() {
        return obligeeInfoVoList;
    }

    public void setObligeeInfoVoList(List<SynNewQLREntity> obligeeInfoVoList) {
        this.obligeeInfoVoList = obligeeInfoVoList;
    }

    public List<SynNewDLREntity> getAgentInfoVoList() {
        return agentInfoVoList;
    }

    public void setAgentInfoVoList(List<SynNewDLREntity> agentInfoVoList) {
        this.agentInfoVoList = agentInfoVoList;
    }
}
