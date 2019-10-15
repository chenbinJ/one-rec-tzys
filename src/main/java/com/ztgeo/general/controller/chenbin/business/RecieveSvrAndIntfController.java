package com.ztgeo.general.controller.chenbin.business;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.service_biz.chenbin.RecieveSvrAndIntfBiz;
import com.ztgeo.general.component.chenbin.InterfaceRequestHandleComponent;
import com.ztgeo.general.component.chenbin.WorkManagerComponent;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import com.ztgeo.general.util.chenbin.MyMapUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/biz/RecService")
@Api(tags = {"收件步骤权限获取及接口统一处理API"})
public class RecieveSvrAndIntfController {

    @Autowired
    private RecieveSvrAndIntfBiz recSvrIntfBiz;
    @Autowired
    private InterfaceRequestHandleComponent interfaceRequestHandleComponent;
    @Autowired
    private WorkManagerComponent workManagerComponent;

    //获取步骤服务接口配置
    @RequestMapping(value = "/getStepServices",method = RequestMethod.GET)
    public ObjectRestResponse<SJ_Act_Step> getStepServices(@RequestParam("taskId") String taskId){
        ObjectRestResponse<SJ_Act_Step> rv = new ObjectRestResponse<SJ_Act_Step>();
        return rv.data(recSvrIntfBiz.findStepServices(taskId));
    }

    //获取服务接口配置
    @RequestMapping(value = "/getOuterService",method = RequestMethod.GET)
    public ObjectRestResponse<SJ_Service> getServiceInfoById(@RequestParam("serviceId")String serviceId){
        return new ObjectRestResponse<SJ_Service>().data(recSvrIntfBiz.findOuterServiceById(serviceId));
    }

    //请求接口的公共方法
    @RequestMapping(value = "/interfaceHandle", method = RequestMethod.POST)
    public ObjectRestResponse<Object> handleInterface(HttpServletRequest request){
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        Map<String,String[]> req_param_array = request.getParameterMap();
        Map<String,String> req_param = MyMapUtil.ValueArrayToValue(req_param_array);
        Map<String,Object> intfCondition = interfaceRequestHandleComponent.requestHandle(req_param);
        System.out.println("接口处理结果返回："+ JSONObject.toJSONString(intfCondition));
        return rv.data(intfCondition);
    }

    //外部请求统一处理方法（带入库字段）
    @RequestMapping(value = "/DealRecieveFromOuter1", method = RequestMethod.POST)
    public ObjectRestResponse<Object> DealRecieveFromOuter1(@RequestBody Map<String,String> sjsq){
        try {
            interfaceRequestHandleComponent.DealRecieveFromOuter1(sjsq,false);//不提交流程
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
        return new ObjectRestResponse<Object>().data("流程数据保存成功");
    }

    //外部请求统一处理方法（带入库字段）
    @RequestMapping(value = "/DealRecieveFromOuter2", method = RequestMethod.POST)
    public ObjectRestResponse<Object> DealRecieveFromOuter2(@RequestBody Map<String,String> sjsq){
        try {
            List<Task> tasks = interfaceRequestHandleComponent.DealRecieveFromOuter2(sjsq);//提交流程
            try {
                workManagerComponent.dealAuto(tasks);
            } catch (Exception e){
                e.printStackTrace();
                log.error("系统异常，异常信息为："+ErrorDealUtil.getErrorInfo(e));
            }
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
        return new ObjectRestResponse<Object>().data("流程提交成功");
    }

    //外部挂起请求
    @RequestMapping(value = "/DealRecieveFromOuter3", method = RequestMethod.POST)
    public ObjectRestResponse<Object> DealRecieveFromOuter3(@RequestBody Map<String,String> sjsq){
        try {
            interfaceRequestHandleComponent.DealRecieveFromOuter3(sjsq);//挂起流程
        } catch (JSONException eeee){
            eeee.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DATE_FORMAT_ERROR_MSG);
        } catch (ZtgeoBizException ee){
            ee.printStackTrace();
            throw ee;
        } catch (Exception eee) {
            eee.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.HANG_UP_NOT_RIGHT);
        }
        return new ObjectRestResponse<Object>().data("流程挂起成功");
    }

    //外部请求附件
    @RequestMapping(value = "/DealRecieveFromOuter4", method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Fjfile>> DealRecieveFromOuter4(@RequestParam("receiptNumber") String receiptNumber){
        List<SJ_Fjfile> files = null;
        try {
            files = interfaceRequestHandleComponent.DealRecieveFromOuter4(receiptNumber);//获取附件
        }catch (ZtgeoBizException ee){
            ee.printStackTrace();
            throw ee;
        } catch (Exception eee) {
            eee.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FILE_GET_EXCEPTION);
        }
        //附件未出现异常并不阻止流程的持续执行
        if(files == null){
            files = new ArrayList<SJ_Fjfile>();
        }
        return new ObjectRestResponse<List<SJ_Fjfile>>().data(files);
    }

    //外部请求新建实例（可通过字段标识出是否需要进行数据保存和流程的提交）
    @RequestMapping(value = "/DealRecieveFromOuter5", method = RequestMethod.POST)
    public ObjectRestResponse<String> DealRecieveFromOuter5(@RequestBody Map<String,String> sjsq){
        try {
            List<Task> tasks = recSvrIntfBiz.DealRecieveFromOuter5(sjsq);
            try {
                workManagerComponent.dealAuto(tasks);
            } catch (Exception e){
                e.printStackTrace();
                log.error("系统异常，异常信息为："+ErrorDealUtil.getErrorInfo(e));
            }
        }catch (ParseException e){
            e.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DATE_FORMAT_ERROR_MSG);
        }catch(ZtgeoBizException e){
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return new ObjectRestResponse<String>().data("操作成功");
    }

    //外部请求可操作的附件条目信息
    @RequestMapping(value = "/DealRecieveFromOuter6", method = RequestMethod.GET)
    public ObjectRestResponse<List<String>> DealRecieveFromOuter6(@RequestParam("modelId") String modelId){
        return new ObjectRestResponse<List<String>>().data(recSvrIntfBiz.findCanWriteEntry(modelId));
    }
}
