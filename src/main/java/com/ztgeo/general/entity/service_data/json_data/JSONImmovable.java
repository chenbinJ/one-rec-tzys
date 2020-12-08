package com.ztgeo.general.entity.service_data.json_data;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Bdc_Gl;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Qlr_Gl;
import com.ztgeo.general.util.chenbin.TimeUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class JSONImmovable implements Serializable {
    private String infoId;                          //主键
    private String receiptNumber;                   //收件编号-sj_sjsq表主键
    private String serviceCode;                     //服务code
    private String dataJson;                        //原始查询数据
    private String dataComeFromMode;                //数据获取方式
    private String preservationMan;                 //数据提交人
    private String provideUnit;                     //数据提供单位
    private String kfqy;
    private String lpzl;
    private String xmmc;
    private String lpmc;
    private String zh;
    private Date insertTime;                      //入库时间
    private String remarks;
    private String ext1;                            //扩展字段1
    private String ext2;                            //扩展字段2
    private String ext3;                            //扩展字段3
    private String glImmovableVoList;          //关联的不动产数据

    private String glObligeeVoList;            //关联权利人列表

    private String glAgentVoList;               //关联代理人

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public String getDataComeFromMode() {
        return dataComeFromMode;
    }

    public void setDataComeFromMode(String dataComeFromMode) {
        this.dataComeFromMode = dataComeFromMode;
    }

    public String getPreservationMan() {
        return preservationMan;
    }

    public void setPreservationMan(String preservationMan) {
        this.preservationMan = preservationMan;
    }

    public String getProvideUnit() {
        return provideUnit;
    }

    public void setProvideUnit(String provideUnit) {
        this.provideUnit = provideUnit;
    }

    public String getKfqy() {
        return kfqy;
    }

    public void setKfqy(String kfqy) {
        this.kfqy = kfqy;
    }

    public String getLpzl() {
        return lpzl;
    }

    public void setLpzl(String lpzl) {
        this.lpzl = lpzl;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getLpmc() {
        return lpmc;
    }

    public void setLpmc(String lpmc) {
        this.lpmc = lpmc;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) throws ParseException {
        this.insertTime =  TimeUtil.getTimeFromString(insertTime);
    }

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

    public String getGlImmovableVoList() {
        return glImmovableVoList;
    }

    public void setGlImmovableVoList(String glImmovableVoList) {
        this.glImmovableVoList = glImmovableVoList;
    }

    public String getGlObligeeVoList() {
        return glObligeeVoList;
    }

    public void setGlObligeeVoList(String glObligeeVoList) {
        this.glObligeeVoList = glObligeeVoList;
    }

    public String getGlAgentVoList() {
        return glAgentVoList;
    }

    public void setGlAgentVoList(String glAgentVoList) {
        this.glAgentVoList = glAgentVoList;
    }
}
