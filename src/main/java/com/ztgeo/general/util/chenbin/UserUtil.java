package com.ztgeo.general.util.chenbin;

import com.github.ag.core.context.BaseContextHandler;
import com.ztgeo.general.dicFinal.FinalStr;
import com.ztgeo.general.exception.LoginErrorException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserUtil {
    public static String checkAndGetUser(){
        String username = BaseContextHandler.getUsername();
        if(username==null || username.length()<=0){
            log.error("用户登录信息异常");
            throw new LoginErrorException(FinalStr.NO_USER_IN_LOGIN);
        }
        return username;
    }
    public static String getUerId(){
        checkAndGetUser();
        return BaseContextHandler.getUserID();
    }
}
