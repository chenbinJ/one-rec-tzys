package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;
import java.util.List;

/*
 * 预告业务实体类
 */
public class SynNewBizInfoEntityAdvance implements Serializable {
    private String htbh;
    private String applyDate;
    private List<SynNewRealEstateInfo> realEstateInfoVoList;

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }


    public List<SynNewRealEstateInfo> getRealEstateInfoVoList() {
        return realEstateInfoVoList;
    }

    public void setRealEstateInfoVoList(List<SynNewRealEstateInfo> realEstateInfoVoList) {
        this.realEstateInfoVoList = realEstateInfoVoList;
    }
}
