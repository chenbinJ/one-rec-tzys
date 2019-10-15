package com.ztgeo.general.controller.penghao;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.penghao.SjPowerStepPositionBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Step_Position;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/jobStepsManager/powerStepManager")
@Api(tags = {"岗位和步骤关联API"})
public class SjPowerStepPositionController {

    @Autowired
    private SjPowerStepPositionBiz sjPowerStepPositionBiz;


    @RequestMapping(value = "/getPositionByStepId", method = RequestMethod.POST)
    @ApiOperation("根据步骤查询岗位")
    public Object getPositionByStepId(String stepId) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(sjPowerStepPositionBiz.getPositionByStepId(stepId));
    }

    @RequestMapping(value = "/insetPositionList", method = RequestMethod.POST)
    @ApiOperation("新增岗位步骤表")
    public Object insetPositionList(@RequestBody List<SJ_Power_Step_Position> list, String stepId) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(sjPowerStepPositionBiz.insetPositionList(list, stepId));
    }


}
