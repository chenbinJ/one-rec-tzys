package com.ztgeo.general.entity.service_data.print;

import java.io.Serializable;

public class PrintEntityMortgage extends PrintEntity implements Serializable {
    private String dyqr            ;
    private String dyr             ;
    private String dh              ;

    private String sqr1            ;
    private String dyr1            ;
    private String dh1             ;

    public String getDyqr() {
        return dyqr;
    }

    public void setDyqr(String dyqr) {
        this.dyqr = dyqr;
    }

    public String getDyr() {
        return dyr;
    }

    public void setDyr(String dyr) {
        this.dyr = dyr;
    }

    public String getSqr1() {
        return sqr1;
    }

    public void setSqr1(String sqr1) {
        this.sqr1 = sqr1;
    }

    public String getDyr1() {
        return dyr1;
    }

    public void setDyr1(String dyr1) {
        this.dyr1 = dyr1;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getDh1() {
        return dh1;
    }

    public void setDh1(String dh1) {
        this.dh1 = dh1;
    }
}
