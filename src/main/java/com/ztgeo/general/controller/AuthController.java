package com.ztgeo.general.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.AdminBiz;
import com.ztgeo.general.vo.JwtAuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
* 鉴权相关包
* */
@RestController
@RequestMapping("jwt")
public class AuthController {

    @Autowired
    private AdminBiz adminBiz;

    @RequestMapping(value = "token", method = RequestMethod.POST)
    public ObjectRestResponse<String> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {
        final String token = adminBiz.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return new ObjectRestResponse<String>().data(token);
    }

    @RequestMapping(value = "invalid", method = RequestMethod.POST)
    public ObjectRestResponse<Boolean> invalid(@RequestParam("token") String token) throws Exception {
        adminBiz.invalid(token);
        return new ObjectRestResponse<Boolean>().data(true);
    }

}
