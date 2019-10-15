package com.ztgeo.general.exception;

import com.github.ag.core.exception.BaseException;
import com.ztgeo.general.dicFinal.FinalStr;
import lombok.Data;

/**
 * Create by Wei on 2018/5/15
 * 没有数据的异常信息
 */
@Data
public class NoDateException extends BaseException {
    public NoDateException(String message) {
        super(message, FinalStr.NO_DATA_RESULT_CODE);
    }
}
