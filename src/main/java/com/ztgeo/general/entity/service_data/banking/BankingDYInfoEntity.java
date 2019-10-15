package com.ztgeo.general.entity.service_data.banking;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BankingDYInfoEntity implements Serializable {
    private String ywbh;
    private String dyhtbh;
    private Double dyje;                                //抵押金额
    private Double dymj;                                //抵押面积
    private String dyqx;                                //抵押期限，终止日期，格式：yyyy-MM-dd HH24:mm:ss
    private String dyfs;                                //抵押方式，设置字典值
    private String txzh;                                //抵押他项证号（办理一般抵押时）
    private String mortgageApplyDate;                   //抵押业务申请日期
    private BankingDYQRInfoEntity dyqr;
    private List<BankingDYRInfoEntity> dyrVoList;
    private List<BankingDYHouseInfoEntity> houseVoList;
    private List<BankingDYFJEntity> dyfjVoList;

    public String getYwbh() {
        return ywbh;
    }

    public void setYwbh(String ywbh) {
        this.ywbh = ywbh;
    }

    public String getDyhtbh() {
        return dyhtbh;
    }

    public void setDyhtbh(String dyhtbh
    ) {
        this.dyhtbh = dyhtbh;
    }

    public Double getDyje() {
        return dyje;
    }

    public void setDyje(Double dyje) {
        this.dyje = dyje;
    }

    public Double getDymj() {
        return dymj;
    }

    public void setDymj(Double dymj) {
        this.dymj = dymj;
    }

    public String getDyqx() {
        return dyqx;
    }

    public void setDyqx(Date dyqx) {
        this.dyqx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dyqx);
    }

    public String getDyfs() {
        return dyfs;
    }

    public void setDyfs(String dyfs) {
        this.dyfs = dyfs;
    }

    public BankingDYQRInfoEntity getDyqr() {
        return dyqr;
    }

    public void setDyqr(BankingDYQRInfoEntity dyqr) {
        this.dyqr = dyqr;
    }

    public List<BankingDYRInfoEntity> getDyrVoList() {
        return dyrVoList;
    }

    public void setDyrVoList(List<BankingDYRInfoEntity> dyrVoList) {
        this.dyrVoList = dyrVoList;
    }

    public List<BankingDYHouseInfoEntity> getHouseVoList() {
        return houseVoList;
    }

    public void setHouseVoList(List<BankingDYHouseInfoEntity> houseVoList) {
        this.houseVoList = houseVoList;
    }

    public List<BankingDYFJEntity> getDyfjVoList() {
        return dyfjVoList;
    }

    public void setDyfjVoList(List<BankingDYFJEntity> dyfjVoList) {
        this.dyfjVoList = dyfjVoList;
    }

    public String getTxzh() {
        return txzh;
    }

    public void setTxzh(String txzh) {
        this.txzh = txzh;
    }
}
