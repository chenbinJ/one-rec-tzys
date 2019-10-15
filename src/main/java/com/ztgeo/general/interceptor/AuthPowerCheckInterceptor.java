package com.ztgeo.general.interceptor;

import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthPowerCheckInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("------------进入权限检查拦截器---------------");
        String url = request.getRequestURI();
        if(url.contains("sysManager")) {
            if (!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())) {
                throw new ZtgeoBizException("操作越权，您的登录用户不支持该操作");
            }
        }
        return true;
    }
}
