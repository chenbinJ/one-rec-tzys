package com.ztgeo.general.exception.chenbin;

import com.github.ag.core.exception.BaseException;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;

public class ZtgeoBizException extends BaseException {
    public ZtgeoBizException(String message) {
        super(message, BizOrBizExceptionConstant.ZTGEO_BIZ_ERROR_CODE);
    }
}
