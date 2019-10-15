package com.ztgeo.general.controller.penghao;

import com.ztgeo.general.component.penghao.SJFjfileComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("api/enclosureManager/fileAllManager")
@Api(tags = {"附件全部下载API"})
public class FjfileController {

    @Autowired
    private SJFjfileComponent sjFjfileComponent;


    @RequestMapping(value = "/downloadAllAttachments", method = RequestMethod.GET)
    @ApiOperation("下载全部附件")
    public void downloadAllAttachments(String processId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        sjFjfileComponent.downloadAllAttachments(processId,request,response);
    }

}
