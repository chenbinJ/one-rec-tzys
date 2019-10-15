package com.ztgeo.general.biz.service_biz.chenbin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.ztgeo.general.biz.service_biz.chenbin.RecieveSvrAndIntfBiz;
import com.ztgeo.general.component.chenbin.InterfaceRequestHandleComponent;
import com.ztgeo.general.component.chenbin.StartProcDataDealComponent;
import com.ztgeo.general.component.chenbin.WorkManagerComponent;
import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.component.penghao.StepComponent;
import com.ztgeo.general.component.pubComponent.ReceiptNumberComponent;
import com.ztgeo.general.component.pubComponent.ServiceAuthorizationComponent;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.entity.service_data.sys_data.PermissionLevelResultEntity;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.PositionUserMapper;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.chenbin.*;
import com.ztgeo.general.service.activity.ApproveService;
import com.ztgeo.general.util.chenbin.JavaBeanUtil;
import com.ztgeo.general.util.chenbin.SysPubDataDealUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("recSvrIntfBiz")
@Transactional(rollbackFor = Exception.class)
public class RecieveSvrAndIntfBizImpl implements RecieveSvrAndIntfBiz {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StepComponent stepComponent;
    @Autowired
    private RecieveServiceMapper recieveServiceMapper;
    @Autowired
    private SvrManagerMapper svrManagerMapper;
    @Autowired
    private IntfManagerMapper intfManagerMapper;
    @Autowired
    private ServiceAuthorizationComponent svrAuthComponent;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private ReceiptNumberComponent receiptNumberComponent;
    @Autowired
    private EntryBusinessMapper entryBusinessMapper;
    @Autowired
    private PositionUserMapper positionUserMapper;
    @Autowired
    private StepManagerMapper stepManagerMapper;
    @Autowired
    private InterfaceRequestHandleComponent interfaceRequestHandleComponent;
    @Autowired
    private StartProcDataDealComponent startProcDataDealComponent;
    @Autowired
    private WorkManagerComponent workManagerComponent;

    @Override
    public SJ_Act_Step findStepServices(String taskId) {
        String stepId = (String) stepComponent.getSteps(taskId);
        if(StringUtils.isBlank(stepId)){//stepId未查到
            throw new ZtgeoBizException(BizOrBizExceptionConstant.TASK_STEP_EXCEPTION);
        }
        //获取岗位步骤权限
        PermissionLevelResultEntity level = svrAuthComponent.checkStepUserStepAllPowers(stepId);

        if(!svrAuthComponent.UserStepGetCheck(level,taskId)){//验证是否在权限内
            throw new ZtgeoBizException(BizOrBizExceptionConstant.STEP_USER_OUT_POWER);
        }
        SJ_Act_Step act = findStepServicesByStepId(stepId);
        if(act == null){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FIND_ACT_NULL);
        }
        return act;
    }

    @Override
    public SJ_Act_Step findStepServicesByStepId(String stepId) {
        System.out.println(stepId);
        SJ_Act_Step act = recieveServiceMapper.selectActInfoByStepId(stepId);
        System.out.println(JSON.toJSONString(act));
        List<SJ_Power_Step_Service> stepSvrs = act.getServicePowerVoList();
        List<SJ_Power_Step_Interface> stepIntfs = act.getInterfacePowerVoList();
        for(SJ_Power_Step_Service stepSvr:stepSvrs){
            SJ_Service svr = stepSvr.getService();
            if(stepSvr.getPermissionLevel()!=null && stepSvr.getPermissionLevel().contains("Write")) {
                svr.setInterfacerVoList(svrManagerMapper.selectSvrIntfInfoById(svr.getServiceId()));
            }
        }
        for(SJ_Power_Step_Interface stepIntf:stepIntfs){
            SJ_Interface intf = stepIntf.getInterfacer();
            intf.setParamVoList(intfManagerMapper.selectParamsByIntfid(intf.getInterfaceId()));
        }
        return act;
    }

    //查询岗位服务
    @Override
    public SJ_Service findOuterServiceById(String serviceId) {
        SJ_Service service = svrManagerMapper.selectSvrById(serviceId);
        if(service==null){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FIND_SERVICE_NULL);
        }
        PermissionLevelResultEntity level = svrAuthComponent.checkUserServicePower(service.getServiceCode(),UserUtil.getUerId());
        if(!level.isResult()){
            return null;
        }
        service.setInterfacerVoList(svrManagerMapper.selectSvrIntfInfoById(serviceId));
        return service;
    }

    /*
     *  param: 模版ID
     *  return: 映射名称集合
     */
    @Override
    public List<String> findCanWriteEntry(String mid) {
        String userId = UserUtil.getUerId();
        List<String> mappingNames = new ArrayList<String>();
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(userId).getIsSuperAdmin())) {//管理员
            List<String> mappingNameChildren = entryBusinessMapper.selectEntryByLevel(mid,null,null);
            for(String mappingNameChild:mappingNameChildren) {//去空
                if(StringUtils.isNotBlank(mappingNameChild))
                    mappingNames.add(mappingNameChild);
            }
        }else {
            List<Position> posits = positionUserMapper.selectPositionByUid(userId);
            for (Position posit : posits){
                List<String> mappingNameChildren = entryBusinessMapper.selectEntryByLevel(mid,BizOrBizExceptionConstant.POWER_LEVEL_WRITE,posit.getId());
                for(String mappingNameChild:mappingNameChildren) {//去空去重
                    if(StringUtils.isBlank(mappingNameChild))
                        continue;
                    boolean isPush = true;
                    for (String mappingName : mappingNames) {
                        if(mappingNameChildren.equals(mappingName)){
                            isPush = false;
                            break;
                        }
                    }
                    if(isPush){
                        mappingNames.add(mappingNameChild);
                    }
                }
            }
        }
        return mappingNames;
    }

    @Override
    @Transactional
    public List<Task> DealRecieveFromOuter5(Map<String, String> sjsq) throws ParseException {
        List<Task> tasks = null;
        String fileVoList = sjsq.get("fileVoList");
        sjsq.remove("fileVoList");
        String modelId = sjsq.get("modelId");
        sjsq.remove("modelId");
        String subControl = sjsq.get("subControl");
        if(StringUtils.isNotBlank(subControl)) {
            sjsq.remove("subControl");
        }

        //启动流程验证（验证是否有权限进行start操作）
        SJ_Act_Step startStep = stepManagerMapper.findStartModuleIdByStep(modelId);
        if(!svrAuthComponent.checkStepUserStepPower(startStep.getStepId(),UserUtil.getUerId())){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_START_POWER_OUT);
        }
        //获取下一个收件编号，并执行启动流程
        String acceptanceId=receiptNumberComponent.getNextReceiptNumber();
        String obj = sjsq.get("SJ_Sjsq");
        Map<String,Object> map = approveService.runProcess(modelId,obj,acceptanceId);
        Approve approve = (Approve) map.get("approve");
        SJ_Sjsq sjsq_ = (SJ_Sjsq) map.get("sjsq");
        Map<String,String> sjsqMap = JavaBeanUtil.convertSJSQJSONToStringMap(obj);

        //数据补充
        sjsqMap.put("receiptNumber",sjsq_.getReceiptNumber());
        sjsqMap.put("businessType",sjsq_.getBusinessType());

        if(!(StringUtils.isNotBlank(subControl) && subControl.equals("2"))){//保存
            //保存数据,并且提交附件
//            startProcDataDealComponent.dealSaveDataOnProcessStart(sjsq_,approve.getApproveTaskId());//保存数据，选择使用out1(带数据超出服务验证)进行保存
            interfaceRequestHandleComponent.DealRecieveFromOuter1(sjsqMap,true);
            //提交附件
            workManagerComponent.dealFjRecordOuter(fileVoList,modelId,approve.getApproveProcessinstanceid());
        }
        if(StringUtils.isBlank(subControl) || subControl.equals("0")){//提交
            //提交相关流程--penghao暴露相关组件给我
            tasks = workManagerComponent.dealSubmitCommon(approve.getApproveTaskId(),approve.getApproveProcessinstanceid());
        }
        return tasks;
    }
}
