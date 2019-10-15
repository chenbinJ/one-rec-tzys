package com.ztgeo.general.component.penghao;

import com.ztgeo.general.biz.PositionBiz;
import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Step_Position;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.mapper.penghao.SjPowerStepPositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.RollbackException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional(rollbackFor = Exception.class)
public class PositionComponent {

    @Autowired
    private SjPowerStepPositionMapper sjPowerStepPositionMapper;
    @Autowired
    private PositionBiz positionBiz;
    @Autowired
    private StepManagerMapper stepManagerMapper;

    /**
     * 步骤对应职务返回人员
     * @param stepId
     * @return
     */
    public List<String> getUserByPositionAndStepId(String stepId){
        List<String> stringList=new ArrayList<>();
        SJ_Act_Step sjActStep=stepManagerMapper.selectStepByMouldId(stepId);
        if (sjActStep==null){
            throw  new ZtgeoBizException(MsgManager.TASK_STEP_NULL);
        }
        List<SJ_Power_Step_Position> power_step_positions=sjPowerStepPositionMapper.getPositionByStepId(sjActStep.getStepId());
        if (power_step_positions==null || power_step_positions.size()==0){
            throw  new ZtgeoBizException(MsgManager.STEP_POSITION_NULL);
        }
        for (SJ_Power_Step_Position position:power_step_positions) {
            List<User> userList=positionBiz.getPositionUsers(position.getPositionId());
            if (userList==null || userList.size()==0){
                throw new ZtgeoBizException(MsgManager.USER_POSITION_NULL);
            }
            for (User user:userList) {
                stringList.add(user.getUsername());
            }
        }
        List<String> strings=new ArrayList<>();
        for(int i=0;i<stringList.size();i++){
            if(!strings.contains(stringList.get(i))){
                strings.add(stringList.get(i));
            }
        }
        return strings;
    }

    /**
     * 步骤对应职务权限为Write返回人员
     * @param stepId
     * @return
     */
    public List<String> getUserByQxPositionAndStepId(String stepId){
        List<String> stringList=new ArrayList<>();
        SJ_Act_Step sjActStep=stepManagerMapper.selectStepByMouldId(stepId);
        if (sjActStep==null){
            throw  new ZtgeoBizException(MsgManager.TASK_STEP_NULL);
        }
        List<SJ_Power_Step_Position> power_step_positions=sjPowerStepPositionMapper.getPositionByStepId(sjActStep.getStepId());
        if (power_step_positions==null || power_step_positions.size()==0){
            throw  new ZtgeoBizException(MsgManager.STEP_POSITION_NULL);
        }
        for (SJ_Power_Step_Position position:power_step_positions) {
            List<User> userList=positionBiz.getPositionUsers(position.getPositionId());
            if (userList==null || userList.size()==0){
                throw new ZtgeoBizException(MsgManager.USER_POSITION_NULL);
            }
            for (User user:userList) {
                stringList.add(user.getUsername());
            }
        }
        List<String> strings=new ArrayList<>();
        for(int i=0;i<stringList.size();i++){
            if(!strings.contains(stringList.get(i))){
                strings.add(stringList.get(i));
            }
        }
        return strings;
    }


}
