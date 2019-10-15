package com.ztgeo.general.entity.service_data.sys_data;

import java.io.Serializable;

public class MyResult<T> implements Serializable {
    private boolean isSuccess;
    private T params;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }
}
