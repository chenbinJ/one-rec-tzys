package com.ztgeo.general.util.chenbin;

import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.enums.HttpRequestMethedEnum;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;

public class MyHttpUtil {
    public static HttpRequestMethedEnum getRequestMethod(String myMethodCode){
        HttpRequestMethedEnum requestMethod = null;
        switch (myMethodCode.toUpperCase()){
            case "POST":
                requestMethod = HttpRequestMethedEnum.HttpPost;
                break;
            case "GET":
                requestMethod = HttpRequestMethedEnum.HttpGet;
                break;
            case "PUT":
                requestMethod = HttpRequestMethedEnum.HttpPut;
                break;
            case "DELETE":
                requestMethod = HttpRequestMethedEnum.HttpDelete;
                break;
            default:
                throw new ZtgeoBizException(BizOrBizExceptionConstant.REQUEST_METHOD_OUT);
        }
        return requestMethod;
    }
}
