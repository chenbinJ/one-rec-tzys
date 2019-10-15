package com.ztgeo.general.controller;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.GateLogBiz;
import com.ztgeo.general.entity.GateLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ${DESCRIPTION}
 *
 * @author weihao
 *
 */
@Controller
@RequestMapping("api/gateLog")
public class GateLogController extends BaseController<GateLogBiz, GateLog> {
}
