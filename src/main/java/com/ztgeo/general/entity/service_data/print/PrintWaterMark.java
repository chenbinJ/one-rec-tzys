package com.ztgeo.general.entity.service_data.print;

import java.io.Serializable;

public class PrintWaterMark implements Serializable {
    private String watermark = "禁止外传";

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }
}
