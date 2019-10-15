package com.ztgeo.general.controller.penghao;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.component.penghao.SjFjtmComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/entryManager/fjinstManager")
@Api(tags = {"条目相关API"})
public class SJFjinstController {

    @Autowired
    private SjFjtmComponent sjFjtmComponent;


    @RequestMapping(value = "/findFjtmByPosition", method = RequestMethod.POST)
    @ApiOperation("根据实例模板展示条目附件")
    public Object findFjtmByPosition(String processId) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(sjFjtmComponent.findFjtmByPosition(processId));
    }

}
