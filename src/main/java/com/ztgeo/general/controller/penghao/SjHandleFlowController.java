package com.ztgeo.general.controller.penghao;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.penghao.SjHandleFlowBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/witnessManager/handleManager")
@Api(tags = {"人证信息和实列相关API"})
public class SjHandleFlowController {

    @Autowired
    private SjHandleFlowBiz sjHandleFlowBiz;


    @RequestMapping(value = "/findWitnessByProcess", method = RequestMethod.GET)
    @ApiOperation("实例展示人证信息")
    public Object findHandleFlowByProcess(String processId) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(sjHandleFlowBiz.findHandleFlowByProcess(processId));
    }


}
