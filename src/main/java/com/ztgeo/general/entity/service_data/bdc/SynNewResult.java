package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;

public class SynNewResult implements Serializable {
    private String slbh;
    private boolean success;
    private String message;

    public String getSlbh() {
        return slbh;
    }

    public void setSlbh(String slbh) {
        this.slbh = slbh;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
