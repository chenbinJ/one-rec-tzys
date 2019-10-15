package com.ztgeo.general.component.chenbin;

import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.ztgeo.general.biz.chenbin.impl.CommonBiz;
import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.component.pubComponent.ServiceAuthorizationComponent;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record_Ext;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.DepartMapper;
import com.ztgeo.general.mapper.PositionMapper;
import com.ztgeo.general.mapper.PositionUserMapper;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.activity.ApproveMapper;
import com.ztgeo.general.mapper.chenbin.FileBusinessMapper;
import com.ztgeo.general.mapper.chenbin.SJInfoManagerMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.mapper.penghao.SjPowerStepPositionMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.OtherUtils;
import com.ztgeo.general.util.chenbin.UserUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class OtherComponent {

    @Resource
    private HistoryService historyService;

    @Autowired
    private StepManagerMapper stepManagerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PositionUserMapper positionUserMapper;

    @Autowired
    private FileBusinessMapper fileBusinessMapper;

    @Autowired
    private ApproveMapper approveMapper;

    @Autowired
    private SjPowerStepPositionMapper sjPowerStepPositionMapper;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private SJInfoManagerMapper sjInfoManagerMapper;

    @Autowired
    private ApproveComponent approveComponent;

    @Autowired
    private DepartMapper departMapper;

    @Autowired
    private CommonBiz commonBiz;

    @Autowired
    private ServiceAuthorizationComponent svrAuthComponent;

    public Object getSteps(String taskId){
        String stepId = null;
        //获得task对象
        HistoricTaskInstance task=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (null == task){
            throw  new ZtgeoBizException(MsgManager.TASK_NULL);
        }
        //查询所对应的stepId
        if(task!=null) {
            SJ_Act_Step sj_act_step = this.stepManagerMapper.selectStepByMouldId(task.getTaskDefinitionKey());
            if (sj_act_step != null) {
                stepId = sj_act_step.getStepId();
            }
        }
        return stepId;
    }

    public List<SJ_Fjinst> getProcInstFjinstPersonCanRead(String processInstanceId){
        List<SJ_Fjinst> fjinsts = null;
        String userId = UserUtil.getUerId();
        if(!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(userId).getIsSuperAdmin())){
            //获取接入用户的岗位信息
            List<Position> posits = positionUserMapper.selectPositionByUid(userId);
        }else{
            fjinsts = getProcInstEasy(processInstanceId);
        }
        /*
            附件相关验证
         */
        //检查登录者条目权限并获取相应附件

        //检查必传项有没传

        //写数据组织算法

        return null;
    }

    public List<SJ_Fjinst> getProcInstEasy(String processInstanceId){
        return fileBusinessMapper.selectAllFjinstByProcInst(processInstanceId);
    }

    public SJ_Exception_Record getNewExceptionRecord(String taskId){
        if(StringUtils.isNotBlank(taskId)) {
            SJ_Exception_Record exc = new SJ_Exception_Record();
            exc.setId(IDUtil.getExceptionId());
            exc.setTaskId(taskId);

            Approve a = null;
            try {
                a = approveMapper.selectByTaskId(taskId);
            } catch (MyBatisSystemException e){
                return exc.excMsgg("任务映射关系异常，一个TASKID存在多个APPROVE与之对应");
            } catch (Exception ee){
                return exc.excMsgg("根据任务ID（taskId）查询approve信息时出现异常，异常信息为："+ ee.getMessage());
            }

            if(a==null){
                exc.setExcMsg("未查询到存在该任务对应的APPROVE信息");
            }else {
                exc.setReceiptNumber(a.getApproveFkId());
                exc.setBusinessName(a.getApproveType());
            }
            exc.setTaskDirection("0");
            return exc;
        }
        return null;
    }

    public List<String> getExeDepartEmp(String stepModelId, String taskId, String processId, DelegateTask delegateTask){
        List<User> users = new ArrayList<>();
        List<User> temp_users = new ArrayList<>();
        //根据步骤取相应的岗位信息,并获得候选者人员列表
        SJ_Act_Step sjActStep=stepManagerMapper.selectStepByMouldId(stepModelId);
        if(sjActStep==null){
            return null;
        }
        List<SJ_Power_Step_Position> power_step_positions=sjPowerStepPositionMapper.getPositionByStepId(sjActStep.getStepId());
        if(power_step_positions!=null){
            for(SJ_Power_Step_Position power_step_position:power_step_positions){
                if(
                        StringUtils.isNotBlank(power_step_position.getPermissionLevel()) &&
                                power_step_position.getPermissionLevel().contains(BizOrBizExceptionConstant.POWER_LEVEL_WRITE)
                ) {
                    List<User> userList = positionMapper.selectPositionUsers(power_step_position.getPositionId());
                    if(userList!=null) {
                        for (User user : userList) {
                            temp_users.add(user);
                        }
                    }
                }
            }
            for(User temp_user:temp_users){
                boolean isPush = true;
                for (User user:users){
                   if(user.getId().equals(temp_user.getId())){
                       isPush = false;
                   }
                }
                if(isPush) {
                    users.add(temp_user);
                }
            }
        }

        List<String> usernames= new ArrayList<String>();
        Approve approve = approveMapper.selectByTaskId(taskId);
        if(approve == null){
            approveComponent.initApproveDataWhenNewTaskInit(delegateTask,processId);
            approve = approveMapper.selectByTaskId(taskId);
        }
        List<SJ_Execute_depart> departs = sjInfoManagerMapper.selectExeDepart(approve.getApproveFkId());
        if(departs!=null) {
            for (SJ_Execute_depart depart : departs) {
                List<Depart> depart_s = departMapper.selectDepartsByName(depart.getExecuteDepart());
                for (Depart depart_:depart_s){
                    List<User> thisUsers = userMapper.selectBelongDepart(depart_.getId());
                    for (User user : users) {
                        for (User thisUser : thisUsers) {
                            if (
                                    user.getId().equals(thisUser.getId()) &&
                                            !usernames.contains(user.getUsername())
                            )
                                usernames.add(user.getUsername());
                        }
                    }
                }
            }
        }
        return usernames;
    }

    //规范接口返回信息
    public Sj_Inquiry_Record takeCanUseRecord(String serviceCode,Sj_Inquiry_Record record,List<Sj_Inquiry_Record_Ext> record_exts){
        List<SJ_Power_Service_Position> sj_power_service_positions = svrAuthComponent.getUserPositionSvrs(serviceCode);
        boolean isAdmin = BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin());

        Depart depart = null;
        if(!isAdmin){
            depart = commonBiz.findBelongDepart();
        }

        record = OtherUtils.dealBelongRecord(sj_power_service_positions,record,isAdmin,depart);
        record.setRecordExts(record_exts);
        return record;
    }
}
