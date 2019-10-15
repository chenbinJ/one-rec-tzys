package com.ztgeo.general.rest;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.service_biz.chenbin.PrintSettingBiz;
import com.ztgeo.general.entity.service_data.print.PrintParam;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = {"模板与打印数据组织相关API"})
@RequestMapping(value = "/api")
public class PrintSettingRest {
    @Autowired
    private PrintSettingBiz printBiz;

    @RequestMapping(value = "/getPrintInfo", method = RequestMethod.GET)
    public ObjectRestResponse<PrintParam> getPrintInfo(@RequestParam("receiptNumber") String receiptNumber){
        ObjectRestResponse<PrintParam> rv = new ObjectRestResponse<PrintParam>();
        return rv.data(printBiz.findPrintSettings(receiptNumber));
    }
}
