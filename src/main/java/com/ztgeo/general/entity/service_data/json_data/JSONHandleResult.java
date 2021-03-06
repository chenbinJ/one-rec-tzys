package com.ztgeo.general.entity.service_data.json_data;

import com.ztgeo.general.util.chenbin.TimeUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSONHandleResult implements Serializable {
    private String infoId;                          //主键
    private String receiptNumber;                   //收件编号-sj_sjsq表主键
    private String oldNumber;
    private String acceptanceNumber;
    private String handleResult;
    private String handleText;
    private String remarks;
    private String dataJson;              //原始查询数据
    private String dataComeFromMode;                //数据获取方式
    private String preservationMan;                 //数据提交人
    private Date insertTime;            //入库时间
    private String serviceCode;           //服务code
    private String provideUnit;                             //数据提供单位
    private String ext1;                  //扩展字段1
    private String ext2;                  //扩展字段2
    private String ext3;                  //扩展字段3

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

    public String getOldNumber() {
        return oldNumber;
    }

    public void setOldNumber(String oldNumber) {
        this.oldNumber = oldNumber;
    }

    public String getAcceptanceNumber() {
        return acceptanceNumber;
    }

    public void setAcceptanceNumber(String acceptanceNumber) {
        this.acceptanceNumber = acceptanceNumber;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getHandleText() {
        return handleText;
    }

    public void setHandleText(String handleText) {
        this.handleText = handleText;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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


    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) throws ParseException {
        this.insertTime = TimeUtil.getTimeFromString(insertTime);
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getProvideUnit() {
        return provideUnit;
    }

    public void setProvideUnit(String provideUnit) {
        this.provideUnit = provideUnit;
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
}
