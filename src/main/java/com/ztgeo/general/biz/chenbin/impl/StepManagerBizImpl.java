package com.ztgeo.general.biz.chenbin.impl;

import com.ztgeo.general.biz.chenbin.StepManagerBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.PositionUserMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.StepRelationUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("stepBiz")
@Transactional(rollbackFor = Exception.class)
public class StepManagerBizImpl implements StepManagerBiz {
    @Autowired
    private StepManagerMapper stepManagerMapper;
    @Autowired
    private PositionUserMapper positionUserMapper;

    //部署流程时调用
    @Override
    public String addAndMergeSteps(List<SJ_Act_Step> steps, List<SJ_Act_Step_Gl> stepGls) {
        //进行传入数据判断
        if (steps == null || steps.size() < 1) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_STEP_NULL_ERROR_MSG);
        }
        if (stepGls == null || stepGls.size() < 1) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_STEP_RELATION_NULL_ERROR_MSG);
        }
        String processId = stepGls.get(0).getProcessMouldId();
        if (processId == null || processId.length() < 1) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_STEP_PROMOD_NULL_ERROR_MSG);
        }
        //删除原有步骤及相关项
        deleteStepsByProcessId(processId);
        //写入步骤信息
        for (SJ_Act_Step step : steps) {
            String stepId = IDUtil.getStepId();
            String stepMouldId = step.getStepMouldId();
            for (SJ_Act_Step_Gl gl : stepGls) {
                int i = 0;
                if (gl.getParentStepId().equals(stepMouldId)) {
                    gl.setParentStepId(stepId);
                    i++;
                }
                if (gl.getChildStepId().equals(stepMouldId)) {
                    gl.setChildStepId(stepId);
                    i++;
                }
                if (i > 1) {
                    log.error("“" + step.getProcessMouldId() + "”模板的“" + step.getStepMouldName() + "”步骤设置异常，不可以自身提交自身");
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_STEP_SEND_STEPSELF_ERROR_MSG);
                }
            }
            step.setStepId(stepId);
            //显示名称的初始值为步骤名称
            step.setStepShowName(step.getStepMouldName());
            if (StringUtils.isEmpty(step.getStepMouldName())) {
                throw new ZtgeoBizException(MsgManager.MODULE_NAME_NULL);
            }
            stepManagerMapper.insertStep(step);
        }
        //写入步骤关系信息
        for (SJ_Act_Step_Gl stepGl : stepGls) {
            String relationId = IDUtil.getStepGlId();
            stepGl.setRelationId(relationId);
            stepManagerMapper.insertStepRelation(stepGl);
        }
        return "流程步骤保存成功";
    }

    //查询流程步骤，前端使用
    @Override
    public List<SJ_Act_Step> findProcessSteps(String processId) {
        //查全部step
        List<SJ_Act_Step> steps_temp = stepManagerMapper.selectStepsByProcessId(processId);
        //查全部step关系
        List<SJ_Act_Step_Gl> steps_gls = stepManagerMapper.selectStepGlsByProcessId(processId);
        if (steps_temp == null || steps_temp.size() == 0) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_STEP_FOUND_NULL);
        }
        if (steps_gls == null || steps_gls.size() == 0) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_STEP_RELATION_FOUND_NULL);
        }
        //处理step关系，使用util进行子父级配置
        StepRelationUtil.setChildSteps(steps_temp, steps_gls);
        //排序steps
        List<SJ_Act_Step> steps = StepRelationUtil.sortSteps(steps_temp);
        return steps;
    }

    //删除流程-ph调用
    @Override
    public String removeProcessSteps(String processId) {
        if (processId == null) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_STEP_PARAM_NULL);
        }
        deleteStepsByProcessId(processId);
        return "流程步骤删除成功";
    }

    //修改流程步骤，前端使用
    @Override
    public String modifyStep(SJ_Act_Step step) {
        //执行修改
        int count = stepManagerMapper.updateStep(step);
        if (count != 1) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_UPDATE_NOT_SUCCESS_MSG);
        }

        //预留通知彭浩的接口


        return "保存成功";
    }

    /*
     * 流程禁/启用，供ph使用
     */
    @Override
    public String discardSteps(String processId) {
        updateStepStatus("禁用", processId);
        return "禁用成功";
    }

    @Override
    public String enableSteps(String processId) {
        updateStepStatus("可用", processId);
        return "启用成功";
    }

    @Override
    public SJ_Act_Step findStepByStepId(String stepId) {
        return stepManagerMapper.selectStepByStepId(stepId);
    }

    /**
     * 获得职务所创建的模板Id
     *
     * @param
     * @return
     */
    @Override
    public Object selectStepByPosition() {
        //获取登录Id
        String userId = UserUtil.getUerId();
        //根据userId获得职务
        List<String> list = new ArrayList<>();
        //用户可以有多个职务
        List<Position> positionByUid = positionUserMapper.selectPositionByUid(userId);
        if (positionByUid == null || positionByUid.size() == 0) {
            return new ZtgeoBizException(MsgManager.POSITION_USER_NULL);
        }
        for (Position tion : positionByUid) {
            list.add(tion.getId());
        }
        List<String> stringList = new ArrayList<>();
        List<SJ_Act_Step> sj_act_steps = stepManagerMapper.selectStepByPosition(list);
        for (SJ_Act_Step sj : sj_act_steps) {
            stringList.add(sj.getProcessMouldId());
        }
        for (int i = 0; i < stringList.size() - 1; i++) {
            for (int j = stringList.size() - 1; j > i; j--) {
                if (stringList.get(j).equals(stringList.get(i))) {
                    stringList.remove(j);
                }
            }
        }
        return stringList;
    }

    /*
     * 权限配置
     */
    @Override
    public List<SJ_Power_Step_Interface> searchStepIntfByStepId(String stepId) {
        SJ_Power_Step_Interface stepIntf = new SJ_Power_Step_Interface();
        stepIntf.setStepId(stepId);
        return stepManagerMapper.selectStepIntfs(stepIntf);
    }

    @Override
    public List<SJ_Power_Step_Service> searchStepSvrByStepId(String stepId) {
        SJ_Power_Step_Service stepSvr = new SJ_Power_Step_Service();
        stepSvr.setStepId(stepId);
        return stepManagerMapper.selectStepSvrs(stepSvr);
    }

    @Override
    public List<SJ_Power_Step_Position> searchStepPosionsByStepId(String stepId) {
        SJ_Power_Step_Position stepPosion = new SJ_Power_Step_Position();
        stepPosion.setStepId(stepId);
        return stepManagerMapper.selectStepPositions(stepPosion);
    }

    @Override
    public SJ_Power_Step_Interface findStepIntfByPowerId(String powerId) {
        return stepManagerMapper.selectStepIntfByPowerId(powerId);
    }

    @Override
    public SJ_Power_Step_Service findStepSvrByPowerId(String powerId) {
        return stepManagerMapper.selectStepSvrByPowerId(powerId);
    }

    @Override
    public SJ_Power_Step_Position findStepPositionByPowerId(String powerId) {
        return stepManagerMapper.selectStepPositionByPowerId(powerId);
    }

    @Override
    public String addStepIntf(SJ_Power_Step_Interface stepIntf) {
        stepIntf.setPowerId(IDUtil.getPowerId());
        stepIntf.setCreateTime(new Date());
        stepIntf.setCreateBy(UserUtil.checkAndGetUser());
        stepManagerMapper.insertStepIntf(stepIntf);
        //验证添加是否产生重复
        if (stepManagerMapper.selectCountOfStepIntfSame(stepIntf) > 1) {
            log.error("stepId:" + stepIntf.getStepId() + "intfId:" + stepIntf.getInterfaceId() + BizOrBizExceptionConstant.FOUND_SAME_STEP_INTF_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_STEP_INTF_MSG);
        }
        return "添加成功";
    }

    @Override
    public String addStepSvr(SJ_Power_Step_Service stepSvr) {
        stepSvr.setPowerId(IDUtil.getPowerId());
        stepSvr.setCreateTime(new Date());
        stepSvr.setCreateBy(UserUtil.checkAndGetUser());
        stepManagerMapper.insertStepSvr(stepSvr);
        //验证添加是否产生重复
        if (stepManagerMapper.selectCountOfStepSvrSame(stepSvr) > 1) {
            log.error("stepId:" + stepSvr.getServiceId() + "svrId:" + stepSvr.getServiceId() + BizOrBizExceptionConstant.FOUND_SAME_STEP_SVR_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_STEP_SVR_MSG);
        }
        return "添加成功";
    }

    @Override
    public String addStepPosition(SJ_Power_Step_Position stepPosition) {
        stepPosition.setPowerId(IDUtil.getPowerId());
        stepPosition.setCreateTime(new Date());
        stepPosition.setCreateBy(UserUtil.checkAndGetUser());
        stepManagerMapper.insertStepPosition(stepPosition);
        //验证添加是否产生重复
        if (stepManagerMapper.selectCountOfStepPositionSame(stepPosition) > 1) {
            log.error("stepId:" + stepPosition.getStepId() + "positionId:" + stepPosition.getPositionId() + BizOrBizExceptionConstant.FOUND_SAME_POSITION_STEP_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_POSITION_STEP_MSG);
        }
        return "添加成功";
    }

    @Override
    public String modifyStepIntf(SJ_Power_Step_Interface stepIntf) {
        stepManagerMapper.updateStepIntf(stepIntf);
        //验证修改是否产生重复
        if (stepManagerMapper.selectCountOfStepIntfSame(stepIntf) > 1) {
            log.error("stepId:" + stepIntf.getStepId() + "intfId:" + stepIntf.getInterfaceId() + BizOrBizExceptionConstant.FOUND_SAME_STEP_INTF_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_STEP_INTF_MSG);
        }
        return "保存成功";
    }

    @Override
    public String modifyStepSvr(SJ_Power_Step_Service stepSvr) {
        stepManagerMapper.updateStepSvr(stepSvr);
        //验证修改是否产生重复
        if (stepManagerMapper.selectCountOfStepSvrSame(stepSvr) > 1) {
            log.error("stepId:" + stepSvr.getServiceId() + "svrId:" + stepSvr.getServiceId() + BizOrBizExceptionConstant.FOUND_SAME_STEP_SVR_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_STEP_SVR_MSG);
        }
        return "保存成功";
    }

    @Override
    public String modifyStepPosition(SJ_Power_Step_Position stepPosition) {
        stepManagerMapper.updateStepPosition(stepPosition);
        //验证修改是否产生重复
        if (stepManagerMapper.selectCountOfStepPositionSame(stepPosition) > 1) {
            log.error("stepId:" + stepPosition.getStepId() + "positionId:" + stepPosition.getPositionId() + BizOrBizExceptionConstant.FOUND_SAME_POSITION_STEP_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_POSITION_STEP_MSG);
        }
        return "保存成功";
    }

    @Override
    public String removeStepIntf(String powerId) {
        int count = stepManagerMapper.deleteStepIntfByParam(powerId, null, null);
        if (count != 1) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DELETE_NOT_SUCCESS_MSG);
        }
        return "删除成功";
    }

    @Override
    public String removeStepSvr(String powerId) {
        int count = stepManagerMapper.deleteStepSvrByParam(powerId, null, null);
        if (count != 1) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DELETE_NOT_SUCCESS_MSG);
        }
        return "删除成功";
    }

    @Override
    public String removeStepPosition(String powerId) {
        int count = stepManagerMapper.deleteStepPositionByParam(powerId, null, null);
        if (count != 1) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DELETE_NOT_SUCCESS_MSG);
        }
        return "删除成功";
    }

    @Override
    public String findStepIdByStepMouldId(String stepMouldId) {
        return stepManagerMapper.selectStepIdByStepMouldId(stepMouldId);
    }

    //私有方法，供上面方法使用
    private void deleteStepsByProcessId(String processId) {
        //删除原有步骤间关联
        stepManagerMapper.deleteStepRelationByProsId(processId);
        //查属于该processId的全部stepId
        List<String> stepIds = stepManagerMapper.selectStepIdsByProcessId(processId);
        //删除上步查到的stepId对应的step和相关联的权限
        for (String stepId : stepIds) {
            stepManagerMapper.deleteStepSvrByParam(null, stepId, null);//删除服务权限
            stepManagerMapper.deleteStepIntfByParam(null, stepId, null);//删除接口权限
            stepManagerMapper.deleteStepPositionByParam(null, stepId, null);//删除部门权限
            stepManagerMapper.deleteStepByStepId(stepId);//删除步骤
        }
    }

    private void updateStepStatus(String status, String processId) {
        //直接修改掉全部关联
        stepManagerMapper.updateRelationStatusByProsId(status, processId);
        //查属于该processId的全部stepId
        List<String> stepIds = stepManagerMapper.selectStepIdsByProcessId(processId);
        //禁用掉这些步骤和步骤权限
        for (String stepId : stepIds) {
            stepManagerMapper.updateStepStatus(status, stepId);//改步骤状态
        }
    }
}
