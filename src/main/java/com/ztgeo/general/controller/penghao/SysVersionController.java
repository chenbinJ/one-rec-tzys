package com.ztgeo.general.controller.penghao;


import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.penghao.SysVersionBiz;
import com.ztgeo.general.entity.service_data.sys_data.SysVersion;
import com.ztgeo.general.util.DownloadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("api/editionManager/version")
@Api(tags = {"版本相关api"})
public class SysVersionController {

    @Value("${msi.url}")
    private String url;

    @Autowired
    private SysVersionBiz sysVersionBiz;

    @RequestMapping(value = "/findSysVersion", method = RequestMethod.GET)
    @ApiOperation("展示版本号信息")
    @ResponseBody
    public Object findSysVersion() {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(sysVersionBiz.findSysVersion());
    }

    @RequestMapping(value = "/downloadMsi", method = RequestMethod.GET)
    @ApiOperation("下载msi文件")
    public void downloadMsi(HttpServletResponse response) {
        DownloadUtil.downLoad(url, null, "application/octet-stream", response);
    }


}
