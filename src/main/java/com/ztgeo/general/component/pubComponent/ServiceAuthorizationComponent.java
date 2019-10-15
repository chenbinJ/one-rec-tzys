package com.ztgeo.general.component.pubComponent;

import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Fjtm_Position;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Service_Position;
import com.ztgeo.general.entity.service_data.sys_data.MyTask;
import com.ztgeo.general.entity.service_data.sys_data.MyTaskInst;
import com.ztgeo.general.entity.service_data.sys_data.PermissionLevelResultEntity;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.PositionUserMapper;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.chenbin.PositSvrManagerMapper;
import com.ztgeo.general.mapper.chenbin.SJSpacielPowerMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.mapper.chenbin.SvrManagerMapper;
import com.ztgeo.general.mapper.penghao.SJPowerFjtmPositionMapper;
import com.ztgeo.general.util.chenbin.UserUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("svrAuthComponent")
public class ServiceAuthorizationComponent {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PositionUserMapper positionUserMapper;
    @Autowired
    private ApproveComponent approveComponent;

    @Autowired
    private StepManagerMapper stepManagerMapper;

    @Autowired
    private SJPowerFjtmPositionMapper sjPowerFjtmPositionMapper;

    @Autowired
    private SJSpacielPowerMapper sJSpacielPowerMapper;

    @Autowired
    private SvrManagerMapper svrManagerMapper;

    @Autowired
    private PositSvrManagerMapper positSvrManagerMapper;

    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    //验证管理员
    public Boolean checkAdmin(){
        return BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin());
    }

    //验证任务没有被签收
    public boolean checkUnSign(String taskId){
        MyTask task=getTask(taskId);
        if(task!=null) {
            String assignee = task.getAssignee();
            if (StringUtils.isBlank(assignee)) {
                return true;
            }
        }else{
            throw new ZtgeoBizException(BizOrBizExceptionConstant.TASK_NOT_EXIST);
        }
        return false;
    }
    //获取任务节点信息
    public MyTask getTask(String taskId){
        String history = "now";
        TaskInfo task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
        if(task == null){
            task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            if(task!=null){
                history = "old";
            } else {
                throw new ZtgeoBizException(BizOrBizExceptionConstant.TASK_NOT_EXIST);
            }
        }
        MyTask myTask = new MyTaskInst();
        myTask.setHistory(history);
        myTask.setId(task.getId()==null?taskId:task.getId());
        myTask.setName(task.getName());
        myTask.setAssignee(task.getAssignee());
        myTask.setDueDate(task.getDueDate());
        myTask.setCreateTime(task.getCreateTime());
        myTask.setExecutionId(task.getExecutionId());
        myTask.setProcessDefinitionId(task.getProcessDefinitionId());
        myTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
        myTask.setProcessInstanceId(task.getProcessInstanceId());
        return myTask;
    }

    public boolean checkPositionByFjtm(String fjinstEntryId){
        List<Position> positionList=positionUserMapper.selectPositionByUid(UserUtil.getUerId());
        for (Position position:positionList) {
            SJ_Power_Fjtm_Position powerFjtmPosition=sjPowerFjtmPositionMapper.findLevelByEntryPosition(fjinstEntryId,position.getId());
            //判断权限是否有Write WriteIfNull
            if (powerFjtmPosition!=null){
                if (powerFjtmPosition.getPermissionLevel().equals(BizOrBizExceptionConstant.POWER_LEVEL_NULL_WRITE)
                        || powerFjtmPosition.getPermissionLevel().equals(BizOrBizExceptionConstant.POWER_LEVEL_WRITE) ){
                    return  true;
                }
            }
        }
        if (approveComponent.findUserByAdministrators(UserUtil.getUerId())==true){
            return  true;
        }
        return  false;
    }


    //验写权限(岗位和步骤)
    public boolean checkStepUserStepPower(String stepId,String userId){
        System.out.println("step:"+stepId+"user:"+userId);
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(userId).getIsSuperAdmin())) {//管理员一定有权处理
            return true;
        }
        List<Position> posits = positionUserMapper.selectPositionByUid(userId);
        if(posits==null || posits.size()<=0){
            return false;
        }
        for(Position posit:posits){//若具有权限则可以操作
            String powerLevel = stepManagerMapper.selectPowerLevelWithStepAndPositionId(stepId,posit.getId());
            if(powerLevel!=null && powerLevel.contains(BizOrBizExceptionConstant.POWER_LEVEL_WRITE)){
                return true;
            }
        }
        return false;
    }

    //获取读或写权限(岗位和步骤)
    public PermissionLevelResultEntity checkStepUserStepAllPowers(String stepId){
        PermissionLevelResultEntity level = new PermissionLevelResultEntity();
        String userId = UserUtil.getUerId();
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(userId).getIsSuperAdmin())) {
            level.setResult(true);
            level.setPermissionLevel(BizOrBizExceptionConstant.POWER_LEVEL_WRITE);
            return level;
        }
        List<Position> posits = positionUserMapper.selectPositionByUid(userId);
        if(posits==null || posits.size()<=0){
            level.setResult(false);
        }else {
            for (Position posit : posits) {
                String powerLevel = stepManagerMapper.selectPowerLevelWithStepAndPositionId(stepId, posit.getId());
                if (
                        powerLevel != null &&
                                (powerLevel.contains(BizOrBizExceptionConstant.POWER_LEVEL_WRITE)
                                        || powerLevel.contains(BizOrBizExceptionConstant.POWER_LEVEL_READ)
                                )
                ) {
                    if(StringUtils.isBlank(level.getPermissionLevel())) {
                        level.setResult(true);
                        level.setPermissionLevel(powerLevel);
                        if(powerLevel.equals(BizOrBizExceptionConstant.POWER_LEVEL_WRITE)){//写权限大于一切权限
                            break;
                        }
                    }else{
                        if(level.getPermissionLevel().equals(powerLevel)){
                            continue;
                        }else{
                            if(powerLevel.equals(BizOrBizExceptionConstant.POWER_LEVEL_WRITE)){//写权限大于一切权限
                                level.setResult(true);
                                level.setPermissionLevel(powerLevel);
                                break;
                            }else if(powerLevel.equals(BizOrBizExceptionConstant.POWER_LEVEL_NULL_WRITE)){//空写权限
                                if(level.getPermissionLevel().contains(BizOrBizExceptionConstant.POWER_LEVEL_READ)){
                                    level.setResult(true);
                                    level.setPermissionLevel(powerLevel);
                                }
                            } else {//查出来是读权限(这步可能会进入，但不会修改什么)

                            }
                        }
                    }
                }
            }
        }
        return level;
    }

    //查看越权判断
    public boolean UserStepGetCheck(PermissionLevelResultEntity level,String taskId){
        boolean isWillDo = false;
        if(level.isResult()) {
            //是否管理员
            if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())) {
                isWillDo = true;
            } else {
                //是否未被签收（写权限）
                if (checkUnSign(taskId)) {
                    if(level.getPermissionLevel().contains(BizOrBizExceptionConstant.POWER_LEVEL_WRITE)) {
                        isWillDo = true;
                    }
                } else {
                    MyTask curTask = getTask(taskId);
                    if(curTask.getAssignee().equals(UserUtil.checkAndGetUser())){//本人签收
                        isWillDo = true;
                    } else {
                        if(
                                level.getPermissionLevel().contains(BizOrBizExceptionConstant.POWER_LEVEL_READ) && (
                                        (StringUtils.isNotBlank(curTask.getHistory()) && "old".equals(curTask.getHistory())) ||
                                        curTask.getDueDate()!=null )
                        ){//只读权限且流程已被提交
                            isWillDo = true;
                        }
                    }
                }
            }
        }
        return isWillDo;
    }


    public PermissionLevelResultEntity checkStepServicePowerWrite(String stepId, String serviceCode){
        PermissionLevelResultEntity level = new PermissionLevelResultEntity();
        String powerLevel = stepManagerMapper.selectPowerLevelWithStepAndServiceCode(stepId,serviceCode);
        if(powerLevel!=null && powerLevel.contains(BizOrBizExceptionConstant.POWER_LEVEL_WRITE)){
            level.setResult(true);
            level.setPermissionLevel(powerLevel);
        }
        return level;
    }
    public PermissionLevelResultEntity getStepServicePower(String stepId, String serviceCode){
        PermissionLevelResultEntity level = new PermissionLevelResultEntity();
        String powerLevel = stepManagerMapper.selectPowerLevelWithStepAndServiceCode(stepId,serviceCode);
        if(powerLevel!=null){
            level.setResult(true);
            level.setPermissionLevel(powerLevel);
        }
        return level;
    }

    public boolean checkSpecialUpdatePower(String userId){
        List<Position> positionList=positionUserMapper.selectPositionByUid(userId);
        for (Position position:positionList) {
            if (sJSpacielPowerMapper.selectPowerByPositionId(position.getId())!=null){//是否为特殊修改权限岗位者
                return true;
            }
        }
        return false;
    }

    public boolean checkServiceInterfacePower(String serviceCode,String interfaceId){
        if(svrManagerMapper.selectCountOfSvrIntfPower(serviceCode,interfaceId)>0){
            return true;
        }
        return false;
    }

    public PermissionLevelResultEntity checkUserServicePower(String serviceCode,String userId){
        PermissionLevelResultEntity level = new PermissionLevelResultEntity();
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(userId).getIsSuperAdmin())) {//管理员
            level.setResult(true);
            level.setPermissionLevel(BizOrBizExceptionConstant.POWER_LEVEL_WRITE);
            return level;
        }
        List<Position> positionList=positionUserMapper.selectPositionByUid(userId);
        if(positionList==null || positionList.size()<=0){//未查到用户身份
            level.setResult(false);
            return level;
        }
        for(Position posit:positionList){//若具有权限则可以操作
            String powerLevel = svrManagerMapper.selectPowerLevelWithPositAndServiceCode(posit.getId(),serviceCode);
            if(StringUtils.isNotBlank(powerLevel)){
                level.setResult(true);
                level.setPermissionLevel(powerLevel);
                return level;
            }
        }
        return level;
    }

    public List<SJ_Power_Service_Position> getUserPositionSvrs(String serviceCode){
        if(StringUtils.isBlank(serviceCode)){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SERVICECODE_CAN_NOT_BE_NULL);
        }
        List<SJ_Power_Service_Position> sj_power_service_positions = null;
        if (!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())) {//非管理员
            List<Position> positionList=positionUserMapper.selectPositionByUid(UserUtil.getUerId());
            if(positionList!=null){
                sj_power_service_positions = new ArrayList<SJ_Power_Service_Position>();
                for(Position posit:positionList){
                    SJ_Power_Service_Position positionSvr = positSvrManagerMapper.selectPositionSvr(posit.getId(),serviceCode);
                    if(positionSvr!=null){
                        sj_power_service_positions.add(positionSvr);
                    }
                }
            }
        }
        return sj_power_service_positions;
    }
}
