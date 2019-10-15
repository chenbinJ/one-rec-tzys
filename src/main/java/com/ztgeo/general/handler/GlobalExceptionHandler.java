package com.ztgeo.general.handler;

import com.github.wxiaoqi.security.common.msg.BaseResponse;
import com.ztgeo.general.dicFinal.FinalStr;
import com.ztgeo.general.exception.LoginErrorException;
import com.ztgeo.general.exception.NoDateException;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice("com.ztgeo.general")
@ResponseBody
public class GlobalExceptionHandler {

    //    全局500异常 杜绝底层错误详情抛至前台
    @ExceptionHandler(Exception.class)
    public BaseResponse ExceptionHandler(HttpServletResponse response, Exception ex) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //外层status的设置
        log.error(ex.getMessage(),ex);
        return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), FinalStr.INTERNAL_SERVER_ERROR_INFO);
    }

    @ExceptionHandler(LoginErrorException.class)
    public BaseResponse loginErrorExceptionHandler(LoginErrorException ex) {
        return new BaseResponse(ex.getStatus(), ex.getMessage());
    }

  /*  @ExceptionHandler(NoDateException.class) //字典未查询到相关数据*/
    public BaseResponse noDateExceptionHandler(NoDateException ex) {
        return new BaseResponse(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(ZtgeoBizException.class)
    public BaseResponse ztgeoBizExceptionHandler(ZtgeoBizException ex) {//所有业务异常
        return new BaseResponse(ex.getStatus(), ex.getMessage());
    }
}
