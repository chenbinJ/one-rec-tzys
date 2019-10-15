package com.ztgeo.general.controller.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.chenbin.impl.EntryManagerBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjtm;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/sysManager/entryManager")
@Api(tags = {"流程条目相关配置API"})
public class EntryManagerController extends BaseController<EntryManagerBiz, SJ_Fjtm> {
    @RequestMapping(value = "/removeByProcId",method = RequestMethod.POST)
    public ObjectRestResponse<String> removeByProcId(@RequestParam("processId")String processId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(baseBiz.removeByProcId(processId));
    }
}
