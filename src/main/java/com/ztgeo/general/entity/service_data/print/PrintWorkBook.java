package com.ztgeo.general.entity.service_data.print;

import java.io.Serializable;

public class PrintWorkBook implements Serializable {
    private String sheetNum = "1";
    private PrintContent content;
    private PrintWaterMark option = new PrintWaterMark();

    public String getSheetNum() {
        return sheetNum;
    }

    public void setSheetNum(String sheetNum) {
        this.sheetNum = sheetNum;
    }

    public PrintContent getContent() {
        return content;
    }

    public void setContent(PrintContent content) {
        this.content = content;
    }

    public PrintWaterMark getOption() {
        return option;
    }

    public void setOption(PrintWaterMark option) {
        this.option = option;
    }
}
