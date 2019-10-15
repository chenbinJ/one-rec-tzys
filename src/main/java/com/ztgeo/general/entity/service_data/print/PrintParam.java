package com.ztgeo.general.entity.service_data.print;

import java.io.Serializable;

public class PrintParam implements Serializable {
    private String templatePath;
    private String fillContent;
    private boolean isPrintPreview = true;

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getFillContent() {
        return fillContent;
    }

    public void setFillContent(String fillContent) {
        this.fillContent = fillContent;
    }

    public boolean getIsPrintPreview() {
        return isPrintPreview;
    }

    public void setIsPrintPreview(boolean printPreview) {
        isPrintPreview = printPreview;
    }
}
