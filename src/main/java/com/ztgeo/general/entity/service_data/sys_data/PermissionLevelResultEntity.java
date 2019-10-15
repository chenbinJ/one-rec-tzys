package com.ztgeo.general.entity.service_data.sys_data;

import java.io.Serializable;

public class PermissionLevelResultEntity implements Serializable {
    private boolean result = false;
    private String permissionLevel;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
