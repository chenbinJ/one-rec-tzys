package com.ztgeo.general.exception;

import com.github.ag.core.exception.BaseException;
import com.ztgeo.general.dicFinal.FinalStr;
import org.springframework.http.HttpStatus;
/*
* 未登录异常
* */
public class LoginErrorException extends BaseException {
    public LoginErrorException() {
        super(FinalStr.NO_LOGIN_MSG, HttpStatus.UNAUTHORIZED.value());
    }

    public LoginErrorException(String errorMsg) {
        super(errorMsg, HttpStatus.UNAUTHORIZED.value());
    }
}
