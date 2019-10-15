package com.ztgeo.general.entity.service_data.sys_data;

public class ActModelPrint {
    private String id;

    private String modelId;

    private String printPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId == null ? null : modelId.trim();
    }

    public String getPrintPath() {
        return printPath;
    }

    public void setPrintPath(String printPath) {
        this.printPath = printPath == null ? null : printPath.trim();
    }
}