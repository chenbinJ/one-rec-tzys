package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;
import java.util.List;

public class SynNewRealEstateInfo implements Serializable {
    //不动产权证号
    private String realEstateId;
    //不动产单元信息列表
    private List<SynNewRealEstateUnitInfo> realEstateUnitInfoVoList;
    //不动产权利人列表
    private List<SynNewQLREntity> obligeeInfoVoList;
    //卖方信息列表
    private List<SynNewMFREntity> salerInfoVoList;
    //预告证明号
    private String vormerkungId;

    public String getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(String realEstateId) {
        this.realEstateId = realEstateId;
    }

    public List<SynNewRealEstateUnitInfo> getRealEstateUnitInfoVoList() {
        return realEstateUnitInfoVoList;
    }

    public void setRealEstateUnitInfoVoList(List<SynNewRealEstateUnitInfo> realEstateUnitInfoVoList) {
        this.realEstateUnitInfoVoList = realEstateUnitInfoVoList;
    }

    public List<SynNewQLREntity> getObligeeInfoVoList() {
        return obligeeInfoVoList;
    }

    public void setObligeeInfoVoList(List<SynNewQLREntity> obligeeInfoVoList) {
        this.obligeeInfoVoList = obligeeInfoVoList;
    }

    public List<SynNewMFREntity> getSalerInfoVoList() {
        return salerInfoVoList;
    }

    public void setSalerInfoVoList(List<SynNewMFREntity> salerInfoVoList) {
        this.salerInfoVoList = salerInfoVoList;
    }

    public String getVormerkungId() {
        return vormerkungId;
    }

    public void setVormerkungId(String vormerkungId) {
        this.vormerkungId = vormerkungId;
    }
}
