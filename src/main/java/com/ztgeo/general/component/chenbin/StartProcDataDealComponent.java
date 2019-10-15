package com.ztgeo.general.component.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.ztgeo.general.biz.service_biz.chenbin.RecieveDataBusinessBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Sjsq;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.util.chenbin.SysPubDataDealUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/*
 *  流程启动时引导数据保存
 */
@Component
public class StartProcDataDealComponent {
    @Autowired
    private RecieveDataBusinessBiz recDataBiz;

    //引导结束后保存数据
    public String dealSaveDataOnProcessStart(SJ_Sjsq sjsq,String taskId){
        if(sjsq==null){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_INFO_NULL_ERROR);
        }
        return recDataBiz.saveServiceData(sjsq,taskId);
    }
}
