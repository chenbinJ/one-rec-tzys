package com.ztgeo.general.biz.service_biz.chenbin;

import com.ztgeo.general.entity.service_data.print.PrintParam;

public interface PrintSettingBiz {
    public PrintParam findPrintSettings(String receiptNumber);
}
