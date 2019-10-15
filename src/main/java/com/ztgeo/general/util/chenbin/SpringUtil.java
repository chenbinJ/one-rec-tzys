package com.ztgeo.general.util.chenbin;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class SpringUtil {
    //静态资源
    private static ApplicationContext applicationContext = null;

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtil.applicationContext == null){
            SpringUtil.applicationContext  = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

}
