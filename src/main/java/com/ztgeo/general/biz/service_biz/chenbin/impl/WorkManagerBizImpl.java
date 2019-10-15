package com.ztgeo.general.biz.service_biz.chenbin.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.ztgeo.general.biz.service_biz.chenbin.WorkManagerBiz;
import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.page.PageResponseBean;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Exception_Record;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface;
import com.ztgeo.general.entity.service_data.sys_data.ExampleRespBody;
import com.ztgeo.general.entity.service_data.sys_data.ExampleSearchParams;
import com.ztgeo.general.entity.service_data.sys_data.ExceptionSearchParams;
import com.ztgeo.general.entity.service_data.sys_data.MySearchQuery;
import com.ztgeo.general.enums.HttpRequestMethedEnum;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.chenbin.ExceptionRecordMapper;
import com.ztgeo.general.mapper.chenbin.WorkManagerMapper;
import com.ztgeo.general.service.activity.WorkFlowOperateService;
import com.ztgeo.general.util.chenbin.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("workManager")
public class WorkManagerBizImpl implements WorkManagerBiz {

    @Autowired
    private WorkFlowOperateService workFlowOperateService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkManagerMapper workManagerMapper;
    @Autowired
    private ApproveComponent approveComponent;
    @Autowired
    private ExceptionRecordMapper ExceptionRecordMapper;

    @Override
    public PageResponseBean<ExampleRespBody> searchExample(ExampleSearchParams params) {
//        MySearchQuery<ExampleSearchParams> searchQuery = new MySearchQuery<ExampleSearchParams>(params);
        if(!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())){
            System.out.println("前"+params.getParticipant());
            params.setReceiptDepart(null);
            params.setParticipant(UserUtil.checkAndGetUser());
            System.out.println("后"+params.getParticipant());
        }
        int pageNum = params.getPageNum();
        int pageSize = params.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        Page<ExampleRespBody> resp = workManagerMapper.selectExample(params);
//        long tt = 0;          共有多少页的判断
//        if(pageSize>0){
//            tt = MyMathUtil.getCeil(resp.getTotal() , pageSize);
//        }
        for(ExampleRespBody r:resp){
            if(StringUtils.isNotBlank(r.getApproveProcessinstanceId())) {
                if (workFlowOperateService.processIsEnd(r.getApproveProcessinstanceId())) {
                    r.setIsDue("1");
                } else {
                    r.setIsDue("0");
                }
            }
        }
        //开始查询
        return new PageResponseBean<ExampleRespBody>(resp,resp.getPageSize(),resp.getPageNum(),resp.getTotal());
    }

    @Override
    public PageResponseBean<SJ_Exception_Record> searchException(ExceptionSearchParams params) {
        if(!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_HANDLE_SUB_NOT_ENOUGH_POWER);
        }
        int pageNum = params.getPageNum();
        int pageSize = params.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        Page<SJ_Exception_Record> resp = workManagerMapper.selectException(params);
        return new PageResponseBean<SJ_Exception_Record>(resp,resp.getPageSize(),resp.getPageNum(),resp.getTotal());
    }

    @Override
    public String handleAutoInterfaceByManager(String taskId) {
        //管理员权限验证
        if(!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_HANDLE_SUB_NOT_ENOUGH_POWER);
        }
        approveComponent.getActivityAutoInterface(taskId,new HashMap<String,String>());
        return "任务二次同步成功";
    }

    @Override
    public String handleSubExceptionByManager(String excId) {
        //管理员权限验证
        if(!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_HANDLE_SUB_NOT_ENOUGH_POWER);
        }
        //查询基础异常信息
        SJ_Exception_Record exc = ExceptionRecordMapper.selectByPrimaryKey(excId);
        if(exc==null){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.EXCEPTION_RECORD_NOT_EXIST);//未查到
        }
        if(StringUtils.isNotBlank(exc.getHandleStatus()) && !exc.getHandleStatus().equals("0")){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.EXCEPTION_RECORD_ALREADY_DEAL);//已处理
        }
        if(StringUtils.isBlank(exc.getTaskDirection())){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.EXCEPTION_DIRECTION_NOT_CLEAR);//方向不明
        }
        if(exc.getTaskDirection().equals("0")) {
            if(StringUtils.isNotBlank(exc.getTaskId())){
                approveComponent.getActivityAutoInterface(exc.getTaskId(), new HashMap<String, String>());//后续加判断是否2次处理成功
                exc = getAlreadyDealException(exc);//获取处理信息
            }else{
                throw new ZtgeoBizException(BizOrBizExceptionConstant.EXCEPTION_DIRECTION_OUTER_NO_TASKID);//向外方向的缺失taskId
            }
        } else if (exc.getTaskDirection().equals("1")) {
            if(StringUtils.isBlank(exc.getNoticeUrl())){
                throw new ZtgeoBizException(BizOrBizExceptionConstant.EXCEPTION_HAVING_NOT_URL);//缺失处理URL
            }
            if(StringUtils.isBlank(exc.getNoticeText())){
                throw new ZtgeoBizException(BizOrBizExceptionConstant.EXCEPTION_HAVING_NOT_NOTICE);//缺失处理通知参数具体信息
            }
            Map<String,String> params = new HashMap<String,String>();
            JSONObject obj = JSONObject.parseObject(exc.getNoticeText());
            params = MyMapUtil.objToStringMap(obj);
            String resp = HttpClientUtil.sendHttp(
                    HttpRequestMethedEnum.HttpPost,
                    "application/json",
                    exc.getNoticeUrl(),
                    params,
                    new HashMap<String,String>()
            );
            if(StringUtils.isNotBlank(resp)){
                //返回值处理
                JSONObject object = JSONObject.parseObject(resp);
                if(object.getInteger("status")==200){
                    exc = getAlreadyDealException(exc);//获取处理信息
                }else{
                    throw new ZtgeoBizException(StringUtils.isNotBlank((String)object.get("data"))?(String)object.get("data"):(String)object.get("message"));
                }
            }
        } else {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.EXCEPTION_DIRECTION_NOT_RIGHT);//不合法的请求方向
        }
        ExceptionRecordMapper.updateByPrimaryKeySelective(exc);
        return "异常处理成功";
    }

    public SJ_Exception_Record getAlreadyDealException(SJ_Exception_Record exc){
        exc.setHandleMan(UserUtil.checkAndGetUser());
        exc.setHandleResult("success");
        exc.setHandleStatus("1");
        exc.setHandleTime(new Date());
        return exc;
    }

    @Override
    public Integer handleExceptionFaild(String excId) {
        SJ_Exception_Record exc = new SJ_Exception_Record();
        exc.setHandleMan(UserUtil.checkAndGetUser());
        exc.setHandleResult("faild");
        exc.setHandleStatus("2");
        exc.setHandleTime(new Date());
        return ExceptionRecordMapper.updateByPrimaryKeySelective(exc);
    }

}
