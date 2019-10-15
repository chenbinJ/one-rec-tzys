package com.ztgeo.general.controller.chenbin.business;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.service_biz.chenbin.RecieveDataBusinessBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Info_Bdcqlxgxx;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Sjsq;
import com.ztgeo.general.entity.service_data.pub_data.Sj_Info_Bdcdyxgxx;
import com.ztgeo.general.entity.service_data.resp_data.RespServiceData;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.util.chenbin.SysPubDataDealUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/biz/RecDataBusi")
@Api(tags = {"收件数据保存API"})
public class RecieveDataBusinessController {
    @Autowired
    private RecieveDataBusinessBiz recDataBiz;

    @RequestMapping(value = "/saveData",method = RequestMethod.POST)
    public ObjectRestResponse<String> saveData(HttpServletRequest request){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        String taskId = request.getParameter("taskId");
        String receiptData = request.getParameter("receiptData");
        SJ_Sjsq sjsq = null;
        try {
            sjsq = SysPubDataDealUtil.parseReceiptData(receiptData,null,null,null);//处理收件数据
            System.out.println(JSONObject.toJSONString(sjsq));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DATE_FORMAT_ERROR_MSG);
        } catch (JSONException eeee){
            eeee.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DATE_FORMAT_ERROR_MSG);
        } catch (ZtgeoBizException ee){
            ee.printStackTrace();
            throw ee;
        } catch (Exception eee) {
            eee.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_JSON_FORMAT_ERROR_MSG);
        }
        //保存数据
        return rv.data(recDataBiz.saveServiceData(sjsq,taskId));
    }

    //获取服务数据
    @RequestMapping(value = "/getReceiptData",method = RequestMethod.GET)
    public ObjectRestResponse<SJ_Sjsq> getReceiptData(@RequestParam("sqbh") String sqbh,
                                                      @RequestParam(name = "serviceCode",required = false)String serviceCode,
                                                      @RequestParam(name = "serviceDataTo",required = false)String serviceDataTo) {
        SJ_Sjsq sjsq = recDataBiz.findSjsqServiceDataByCode(sqbh,serviceCode,serviceDataTo);
        return new ObjectRestResponse<SJ_Sjsq>().data(sjsq);
    }

    //删除服务数据
    @RequestMapping(value = "/removeChooseServiceData",method = RequestMethod.POST)
    public ObjectRestResponse<String> removeChooseServiceData(
            @RequestParam("infoId") String infoId,
            @RequestParam("serviceCode") String serviceCode,
            @RequestParam("taskId") String taskId,
            @RequestParam(value = "serviceDataTo",required = false) String serviceDataTo
    ){
        return new ObjectRestResponse<String>().data(recDataBiz.removeServiceData(infoId,serviceCode,taskId,serviceDataTo));
    }
}
