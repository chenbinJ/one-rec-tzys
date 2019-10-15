package com.ztgeo.general.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.github.ag.core.constants.CommonConstants;
import com.github.ag.core.context.BaseContextHandler;
import com.github.ag.core.util.jwt.IJWTInfo;
import com.github.wxiaoqi.security.common.exception.auth.UserForbiddenException;
import com.github.wxiaoqi.security.common.util.ClientUtil;
import com.ztgeo.general.biz.GateLogBiz;
import com.ztgeo.general.biz.UserBiz;
import com.ztgeo.general.config.UserAuthConfig;
import com.ztgeo.general.util.DBLog;
import com.ztgeo.general.vo.LogInfo;
import com.ztgeo.general.vo.PermissionInfo;
import com.ztgeo.general.exception.LoginErrorException;
import com.ztgeo.general.util.jwt.UserAuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private GateLogBiz gateLogBiz;

    @Autowired
    private UserBiz userBiz;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("------------进入登录检查拦截器---------------");
        System.out.println(request.getRequestURI());
        if(request.getRequestURI().contains("swagger")|| request.getRequestURI().contains("favicon")|| request.getRequestURI().contains("error")){
            return true;
        }
        BaseContextHandler.setToken(null); //清空baseContextHandler
        //取得jwtToken
        IJWTInfo user = null;
        try {
                user = getJWTUser(request);
                log.info(user.getId()+user.getName()+user.getUniqueName());
            } catch (Exception e) {
            log.error("进入异常!!");
            log.error(e.getMessage(), e);
           throw new LoginErrorException();
        }

        //检查操作的url权限是否被允许 是否某个url的操作权限是否分配给了当前的用户
        List<PermissionInfo> permissionIfs = userBiz.getAllPermission();
        // 判断资源是否启用权限约束
        Stream<PermissionInfo> stream = getPermissionIfs(request.getRequestURI(), request.getMethod(), permissionIfs);
        List<PermissionInfo> result = stream.collect(Collectors.toList());
        PermissionInfo[] permissions = result.toArray(new PermissionInfo[]{});
        if (permissions.length > 0) {
            checkUserPermission(permissions,user,request);
        }
        return super.preHandle(request, response, handler);
    }


    private void checkUserPermission(PermissionInfo[] permissions, IJWTInfo user, HttpServletRequest request) {
        List<PermissionInfo> permissionInfos = userBiz.getPermissionByUsername(user.getId());
        PermissionInfo current = null;
        for (PermissionInfo info : permissions) {
            boolean anyMatch = permissionInfos.parallelStream().anyMatch(new Predicate<PermissionInfo>() {
                @Override
                public boolean test(PermissionInfo permissionInfo) {
                    return permissionInfo.getCode().equals(info.getCode());
                }
            });
            if (anyMatch) {
                current = info;
                break;
            }
        }
        if (current == null) {
            throw new UserForbiddenException("越权操作!");
        } else {
            if (!RequestMethod.GET.toString().equals(current.getMethod())) {
                System.out.println("进入日志记载");
                String host = ClientUtil.getClientIp(request);
                LogInfo logInfo = new LogInfo(current.getMenu(), current.getName(), current.getUri(), new Date(), user.getId(), user.getName(), host, user.getOtherInfo().get(CommonConstants.JWT_KEY_TENANT_ID));
                DBLog.getInstance().setLogService(gateLogBiz).offerQueue(logInfo);
            }
        }
    }

    /**
     * 获取目标权限资源
     *
     * @param requestUri
     * @param method
     * @param serviceInfo
     * @return
     */
    private Stream<PermissionInfo> getPermissionIfs(final String requestUri, final String method, List<PermissionInfo> serviceInfo) {
        return serviceInfo.parallelStream().filter(new Predicate<PermissionInfo>() {
            @Override
            public boolean test(PermissionInfo permissionInfo) {
                String url = permissionInfo.getUri();
                String uri = url.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
                String regEx = "^" + uri + "$";
                return (Pattern.compile(regEx).matcher(requestUri).find() || requestUri.startsWith(url + "/"))
                        && method.equals(permissionInfo.getMethod());
            }
        });
    }

    /**
     * 返回session中的jwt用户信息
     *
     * @param request
     * @return
     */
    private IJWTInfo getJWTUser(HttpServletRequest request) throws Exception {
        String authToken = request.getHeader(userAuthConfig.getTokenHeader());
        if (StringUtils.isBlank(authToken)) {
            authToken = request.getParameter("token");
        }
        BaseContextHandler.setToken(authToken);
        //基础属性的设置
        return userAuthUtil.getInfoFromToken(authToken);
    }

}
