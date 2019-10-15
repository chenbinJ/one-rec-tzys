package com.ztgeo.general.mapper.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.*;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StepManagerMapper{
    public Integer deleteStepSvrByParam(@Param("powerId") String powerId, @Param("stepId")String stepId, @Param("svrId")String svrId);
    public Integer deleteStepIntfByParam(@Param("powerId") String powerId, @Param("stepId")String stepId, @Param("intfId")String intfId);
    public Integer deleteStepPositionByParam(@Param("powerId") String powerId, @Param("stepId")String stepId, @Param("positionId")String positionId);
    public List<SJ_Act_Step>  selectStepByPosition(List<String> list);
    public Integer updateStepStatus(@Param("status")String status,@Param("stepId")String stepId);
    public Integer updateRelationStatusByProsId(@Param("status")String status,@Param("processId")String peocessId);
    public Integer deleteStepRelationByProsId(@Param("processId") String processId);
    public  SJ_Act_Step findStartModuleIdByStep(String mid);
    public List<String> selectStepIdsByProcessId(@Param("processId") String processId);
    public Integer deleteStepByStepId(@Param("stepId") String stepId);
    public Integer insertStep(SJ_Act_Step act);
    public Integer insertStepRelation(SJ_Act_Step_Gl actGl);
    public SJ_Act_Step findStepByModuleId(String moduleId);
    public List<SJ_Act_Step> selectStepsByProcessId(@Param("processId") String processId);
    public SJ_Act_Step selectStepByStepId(@Param("stepId")String stepId);
    public Integer updateStep(SJ_Act_Step step);
    public List<SJ_Act_Step_Gl> selectStepGlsByProcessId(@Param("processId") String processId);
    public List<SJ_Power_Step_Interface> findstepIntfsByStepId(@Param("stepId") String stepId);
    public Integer findInterfaceCountByInterfaceId(@Param("InterfaceId") String InterfaceId);
    public SJ_Act_Step selectStepByMouldId(String moduleId);

    //权限配置
    public List<SJ_Power_Step_Interface> selectStepIntfs(SJ_Power_Step_Interface stepIntf);
    public List<SJ_Power_Step_Service> selectStepSvrs(SJ_Power_Step_Service stepSvr);
    public List<SJ_Power_Step_Position> selectStepPositions(SJ_Power_Step_Position stepPositon);
    public SJ_Power_Step_Interface selectStepIntfByPowerId(@Param("powerId")String powerId);
    public SJ_Power_Step_Service selectStepSvrByPowerId(@Param("powerId")String powerId);
    public SJ_Power_Step_Position selectStepPositionByPowerId(@Param("powerId")String powerId);
    public Integer insertStepIntf(SJ_Power_Step_Interface stepIntf);
    public Integer insertStepSvr(SJ_Power_Step_Service stepSvr);
    public Integer insertStepPosition(SJ_Power_Step_Position stepPosition);
    public Integer updateStepIntf(SJ_Power_Step_Interface stepIntf);
    public Integer updateStepSvr(SJ_Power_Step_Service stepSvr);
    public Integer updateStepPosition(SJ_Power_Step_Position stepPosition);

    public Integer selectCountOfStepIntfSame(SJ_Power_Step_Interface stepIntf);
    public Integer selectCountOfStepSvrSame(SJ_Power_Step_Service stepSvr);
    public Integer selectCountOfStepPositionSame(SJ_Power_Step_Position stepPosition);

    public String selectStepIdByStepMouldId(@Param("stepMouldId")String stepMouldId);

    public String selectPowerLevelWithStepAndPositionId(@Param("stepId")String stepId,@Param("positId")String positId);
    public String selectPowerLevelWithStepAndServiceCode(@Param("stepId")String stepId,@Param("serviceCode")String serviceCode);
    public SJ_Power_Step_Interface selectStepIntfByThemId(@Param("stepId")String stepId,@Param("interfaceId")String interfaceId);
}
