package com.ztgeo.general.entity.service_data.banking;

import java.io.Serializable;

/*
 * 抵押查询参数
 */
public class BankingDYParamEntity implements Serializable {
    private BankingDYRInfoEntity dyr;   //抵押人
    private String dyhtbh;              //抵押合同编号
    private String dyywbh;              //抵押业务编号
    private String ext_status;          //扩展字段_状态

    public String getDyhtbh() {
        return dyhtbh;
    }

    public void setDyhtbh(String dyhtbh) {
        this.dyhtbh = dyhtbh;
    }

    public String getDyywbh() {
        return dyywbh;
    }

    public void setDyywbh(String dyywbh) {
        this.dyywbh = dyywbh;
    }

    public String getExt_status() {
        return ext_status;
    }

    public void setExt_status(String ext_status) {
        this.ext_status = ext_status;
    }

    public BankingDYRInfoEntity getDyr() {
        return dyr;
    }

    public void setDyr(BankingDYRInfoEntity dyr) {
        this.dyr = dyr;
    }
}
