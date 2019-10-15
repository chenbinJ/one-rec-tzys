package com.ztgeo.general.mapper.activity;


import com.ztgeo.general.entity.activity.Approve;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ApproveMapper {
    int deleteByPrimaryKey(@Param("id") String id);

    Integer insert(Approve record);

    List<Approve> getUnfinishedProcess(@Param("approveType") String approveType,@Param("approveStatus") String approveStatus,@Param("username") String username,@Param("list") List<String> list);//完成过得节点判断是否已结束

    List<Approve> findHangUpFlow(@Param("username") String username,@Param("approveType") String approveType);//挂起业务查询

    Integer insertSelective(Approve record);

    List<Approve> getApproveByProcessId(@Param("uname") String uname,@Param("processId") String processId);

    Approve selectByPrimaryKey(@Param("id") String id);

    Approve selectByTaskId(@Param("taskId") String taskId);

    List<Approve> selectApproveByYwcOrName(@Param("name") String name,@Param("processId") String processId);

    Approve selectByProcessinstanceId(@Param("approveProcessinstanceid") String approveProcessinstanceid);

    Integer updateByPrimaryKeySelective(Approve record);

    Integer updateApproveByTaskId(Approve approve);

    Integer updateByPrimaryKey(Approve record);

    /**
     * 查询自己审批已完成
     * @param username
     * @return
     */
    List<Approve> findCompletedByOwn( @Param("username") String username, @Param("approve") Approve approve);

    List<Approve> selectByIds(@Param("list") List<String> list);

    List<Approve> findPrintList(Map<String, Object> map)throws Exception;


    /**
     * 查看流程的启动时间和名称
     * @param processId
     * @return
     */
    List<Approve> findQdTimeByName(String processId);

    /**
     * 我参与过但未完成的流程
     * @param username
     * @return
     */
    List<Approve> findCompletedByNotFinished(String username);

    List<Approve> findPrintApplyList(Map<String, Object> map)throws Exception;

    List<Approve> selectByIds2(@Param("list") List<String> list, @Param("approve") Approve approve);

    List<Approve> selectByUserDb(String userName,@Param("approve") Approve approve);

    Approve findApproveByTask(@Param("username") String username,@Param("taskId") String taskId);

    Approve getApproveByPersonOrFkId(@Param("approveFkId") String approveFkId,@Param("username") String username);

    List<Approve> getApproveByApproveFkId(String approveFkId);

    List<Approve> findReceiptRegisterList(Map<String, Object> map)throws Exception;

    List<Approve> findPostRegisterList(Map<String, Object> map)throws Exception;

    List<Approve> findOaLeaveList(Map<String, Object> map)throws Exception;

    List<Approve> findRoomLaunchList(Map<String, Object> map)throws Exception;

    List<Approve> findMonitorProcess(Approve record);
}