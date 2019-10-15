package com.ztgeo.general.controller.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.chenbin.SpacielPowerBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Spaciel_Position_Power;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/sysManager")
@Api(tags = {"特殊权限配置API"})
public class SpacielPowerController {
    @Autowired
    private SpacielPowerBiz spacielBiz;
    @RequestMapping(value = "/spaciel/find",method = RequestMethod.GET)
    public ObjectRestResponse<SJ_Spaciel_Position_Power> find(@RequestParam("positionId") String positionId){
        return new ObjectRestResponse<SJ_Spaciel_Position_Power>().data(spacielBiz.findOne(positionId));
    }
    @RequestMapping(value = "/spaciel/add",method = RequestMethod.POST)
    public ObjectRestResponse<String> add(@RequestParam("positionId") String positionId){
        return new ObjectRestResponse<String>().data(spacielBiz.insertBody(positionId));
    }
    @RequestMapping(value = "/spaciel/remove",method = RequestMethod.POST)
    public ObjectRestResponse<String> remove(@RequestParam("positionId") String positionId){
        return new ObjectRestResponse<String>().data(spacielBiz.deleteOne(positionId));
    }
}
