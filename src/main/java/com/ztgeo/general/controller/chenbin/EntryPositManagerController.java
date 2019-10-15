package com.ztgeo.general.controller.chenbin;

import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.chenbin.impl.EntryPositManagerBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Fjtm_Position;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/sysManager/entryPositManager")
@Api(tags = {"流程条目与岗位权限相关配置API"})
public class EntryPositManagerController extends BaseController<EntryPositManagerBiz, SJ_Power_Fjtm_Position> {
}
