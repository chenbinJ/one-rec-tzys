package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;
import java.util.List;

public class SJ_Info_Immovable extends SJ_Information implements Serializable {
    private String remarks;
    private String ext1;                            //扩展字段1
    private String ext2;                            //扩展字段2
    private String ext3;                            //扩展字段3
    private List<SJ_Bdc_Gl> glImmovableVoList;          //关联的不动产数据

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public List<SJ_Bdc_Gl> getGlImmovableVoList() {
        return glImmovableVoList;
    }

    public void setGlImmovableVoList(List<SJ_Bdc_Gl> glImmovableVoList) {
        this.glImmovableVoList = glImmovableVoList;
    }
}
