package com.ztgeo.general.entity.service_data.json_data;

import com.ztgeo.general.util.chenbin.TimeUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSONQsxx implements Serializable {
    private String infoId;                          //主键
    private String receiptNumber;                   //收件编号-sj_sjsq表主键
    private String dataJson;              //原始查询数据
    private String dataComeFromMode;                //数据获取方式
    private Date insertTime;            //入库时间
    private String serviceCode;           //服务code
    private String provideUnit;                             //数据提供单位
    private String preservationMan;                 //数据提交人
    private String ext1;                  //扩展字段1
    private String ext2;                  //扩展字段2
    private String ext3;                  //扩展字段3
    private String xtsphm;//系统税票号码

    private String htbh;//合同编号

    private String nsrsbh;

    private String zrfcsfbz;

    private String nsrmc;

    private String dzsphm;//电子税票号码

    private String pzzlDm;//凭证种类代码

    private String pzzgDm;//票证子规代码

    private String pzhm;//票证号码

    private String skssqq;//税款所属期起

    private String skssqz;//税款所属期止

    private String zsxmDm;//征收项目

    private String zspmDm;//征收品目

    private String zszmDm;//征收子目

    private String zsxmMc;

    private String zspmMc;

    private String zszmMc;

    private String jsyj;//计税依据

    private String sl;//税率

    private Long sjje;//实际缴纳金额

    private String zgswskfjDm;//主管税务机关科分局

    private String zsswjgDm;//征收税务机关

    private String skssswjgDm;//税款所属机关

    private String zgswskfjMc;

    private String zsswjgMc;

    private String skssswjgMc;

    private String kjrq;//扣缴日期

    private String bz;//备注

    private String bdcdyh;//不动产单元号

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

    public String getPreservationMan() {
        return preservationMan;
    }

    public void setPreservationMan(String preservationMan) {
        this.preservationMan = preservationMan;
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

    public String getXtsphm() {
        return xtsphm;
    }

    public void setXtsphm(String xtsphm) {
        this.xtsphm = xtsphm;
    }

    public String getZrfcsfbz() {
        return zrfcsfbz;
    }

    public void setZrfcsfbz(String zrfcsfbz) {
        this.zrfcsfbz = zrfcsfbz;
    }


    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getNsrsbh() {
        return nsrsbh;
    }

    public void setNsrsbh(String nsrsbh) {
        this.nsrsbh = nsrsbh;
    }

    public String getNsrmc() {
        return nsrmc;
    }

    public void setNsrmc(String nsrmc) {
        this.nsrmc = nsrmc;
    }

    public String getDzsphm() {
        return dzsphm;
    }

    public void setDzsphm(String dzsphm) {
        this.dzsphm = dzsphm;
    }

    public String getPzzlDm() {
        return pzzlDm;
    }

    public void setPzzlDm(String pzzlDm) {
        this.pzzlDm = pzzlDm;
    }

    public String getPzzgDm() {
        return pzzgDm;
    }

    public void setPzzgDm(String pzzgDm) {
        this.pzzgDm = pzzgDm;
    }

    public String getPzhm() {
        return pzhm;
    }

    public void setPzhm(String pzhm) {
        this.pzhm = pzhm;
    }

    public String getSkssqq() {
        return skssqq;
    }

    public void setSkssqq(String skssqq) throws ParseException {
        this.skssqq = skssqq;
    }

    public String getSkssqz() {
        return skssqz;
    }

    public void setSkssqz(String skssqz) throws ParseException {
        this.skssqz = skssqz;
    }

    public String getZsxmDm() {
        return zsxmDm;
    }

    public void setZsxmDm(String zsxmDm) {
        this.zsxmDm = zsxmDm;
    }

    public String getZspmDm() {
        return zspmDm;
    }

    public void setZspmDm(String zspmDm) {
        this.zspmDm = zspmDm;
    }

    public String getZszmDm() {
        return zszmDm;
    }

    public void setZszmDm(String zszmDm) {
        this.zszmDm = zszmDm;
    }

    public String getZsxmMc() {
        return zsxmMc;
    }

    public void setZsxmMc(String zsxmMc) {
        this.zsxmMc = zsxmMc;
    }

    public String getZspmMc() {
        return zspmMc;
    }

    public void setZspmMc(String zspmMc) {
        this.zspmMc = zspmMc;
    }

    public String getZszmMc() {
        return zszmMc;
    }

    public void setZszmMc(String zszmMc) {
        this.zszmMc = zszmMc;
    }

    public String getJsyj() {
        return jsyj;
    }

    public void setJsyj(String jsyj) {
        this.jsyj = jsyj;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public Long getSjje() {
        return sjje;
    }

    public void setSjje(Long sjje) {
        this.sjje = sjje;
    }

    public String getZgswskfjDm() {
        return zgswskfjDm;
    }

    public void setZgswskfjDm(String zgswskfjDm) {
        this.zgswskfjDm = zgswskfjDm;
    }

    public String getZsswjgDm() {
        return zsswjgDm;
    }

    public void setZsswjgDm(String zsswjgDm) {
        this.zsswjgDm = zsswjgDm;
    }

    public String getSkssswjgDm() {
        return skssswjgDm;
    }

    public void setSkssswjgDm(String skssswjgDm) {
        this.skssswjgDm = skssswjgDm;
    }

    public String getZgswskfjMc() {
        return zgswskfjMc;
    }

    public void setZgswskfjMc(String zgswskfjMc) {
        this.zgswskfjMc = zgswskfjMc;
    }

    public String getZsswjgMc() {
        return zsswjgMc;
    }

    public void setZsswjgMc(String zsswjgMc) {
        this.zsswjgMc = zsswjgMc;
    }

    public String getSkssswjgMc() {
        return skssswjgMc;
    }

    public void setSkssswjgMc(String skssswjgMc) {
        this.skssswjgMc = skssswjgMc;
    }

    public String getKjrq() {
        return kjrq;
    }

    public void setKjrq(String kjrq) throws ParseException {
        this.kjrq = kjrq;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

}
