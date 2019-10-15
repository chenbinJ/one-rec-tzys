package com.ztgeo.general.controller.chenbin;

import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.chenbin.impl.PositSvrManagerBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Service_Position;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/sysManager/positSvrManager")
@Api(tags = {"岗位与服务权限相关配置API"})
public class PositSvrManagerController extends BaseController<PositSvrManagerBiz, SJ_Power_Service_Position> {

}
