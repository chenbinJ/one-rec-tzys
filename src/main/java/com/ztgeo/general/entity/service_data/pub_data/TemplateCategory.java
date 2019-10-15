package com.ztgeo.general.entity.service_data.pub_data;

import java.util.Date;

public class TemplateCategory {
    private String templateId;

    private String templateModel;

    private String templateLargeName;//类型

    private String templateLarge;//value

    private String templateCreateby;

    private Date templateCreatetime;

    private String templateProcess;

    public String getTemplateProcess() {
        return templateProcess;
    }

    public void setTemplateProcess(String templateProcess) {
        this.templateProcess = templateProcess;
    }

    public String getTemplateLargeName() {
        return templateLargeName;
    }

    public void setTemplateLargeName(String templateLargeName) {
        this.templateLargeName = templateLargeName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId == null ? null : templateId.trim();
    }

    public String getTemplateModel() {
        return templateModel;
    }

    public void setTemplateModel(String templateModel) {
        this.templateModel = templateModel == null ? null : templateModel.trim();
    }

    public String getTemplateLarge() {
        return templateLarge;
    }

    public void setTemplateLarge(String templateLarge) {
        this.templateLarge = templateLarge == null ? null : templateLarge.trim();
    }

    public String getTemplateCreateby() {
        return templateCreateby;
    }

    public void setTemplateCreateby(String templateCreateby) {
        this.templateCreateby = templateCreateby == null ? null : templateCreateby.trim();
    }

    public Date getTemplateCreatetime() {
        return templateCreatetime;
    }

    public void setTemplateCreatetime(Date templateCreatetime) {
        this.templateCreatetime = templateCreatetime;
    }
}