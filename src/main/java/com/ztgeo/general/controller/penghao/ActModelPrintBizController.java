package com.ztgeo.general.controller.penghao;

import com.ztgeo.general.biz.penghao.ActModelPrintBiz;
import com.ztgeo.general.entity.service_data.sys_data.ActModelPrint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/templatePrinting/mdPManager")
@Api(tags = {"模板与打印相关API"})
public class ActModelPrintBizController {

    @Autowired
    private ActModelPrintBiz actModelPrintBiz;

    @RequestMapping(value = "/insertModelPrint", method = RequestMethod.POST)
    @ApiOperation("新增模板打印信息")
    public Object insertModelPrint(ActModelPrint actModelPrint) {
        return actModelPrintBiz.insertModelPrint(actModelPrint);
    }

    @RequestMapping(value = "/selectByModelId", method = RequestMethod.GET)
    @ApiOperation("通过模板查询信息")
    public Object selectByModelId(String modelId) {
        return actModelPrintBiz.selectByModelId(modelId);
    }

}
