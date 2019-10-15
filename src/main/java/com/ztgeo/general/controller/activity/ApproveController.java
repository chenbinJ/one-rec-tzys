package com.ztgeo.general.controller.activity;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.service_biz.chenbin.impl.ExceptionRecordBiz;
import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.component.penghao.SjFjtmComponent;
import com.ztgeo.general.component.penghao.StepComponent;
import com.ztgeo.general.config.activity.SpringContextHolder;
import com.ztgeo.general.config.webscoket.WebSocket;
import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.extend.Base64Picture;
import com.ztgeo.general.entity.extend.NumberDisplay;
import com.ztgeo.general.entity.service_data.pub_data.SjHandleFlow;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.penghao.DepartUserMapper;
import com.ztgeo.general.service.activity.ApproveService;
import com.ztgeo.general.service.activity.WorkFlowOperateService;
import com.ztgeo.general.util.CommonUtils;
import com.ztgeo.general.util.ReturnMsgUtil;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("api/approve")
@Api(tags = "审批管理")
@Slf4j
public class ApproveController {


    private Logger logger=Logger.getLogger(Approve.class);

    @Autowired
    private ApproveService approveService;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private WorkFlowOperateService workFlowOperateService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private ApproveComponent approveComponent;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private StepComponent stepComponent;
    @Autowired
    private DepartUserMapper departUserMapper;



    @Autowired
    private SjFjtmComponent sjFjtmComponent;





    /**
     * 审批流程
     * @param process 实例
     * @param taskId 任务id
     * @param approveResult 审核结果
     * @return
     */
    @RequestMapping(value = "/approvalProcess",method = RequestMethod.POST)
    @ApiOperation("提交审批流程")
    public Object approvalProcess(String process,String taskId,String approveResult,HttpServletRequest request) throws ParseException {
        List<Task> taskCompletes = approveService.approvalProcess(process,taskId,UserUtil.checkAndGetUser(),approveResult,request);
        try {
            ObjectRestResponse<Object> rvv = (ObjectRestResponse<Object>)approveService.checkAndDealThisTasks(taskCompletes);
        } catch (ZtgeoBizException e){
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (Exception e){
            System.out.println();
            e.printStackTrace();
            log.error(ErrorDealUtil.getErrorInfo(e));
        }
        return new ObjectRestResponse<Object>().data("提交成功");
    }


    /**
     *
     * @param taskId 根据taskId和用户判断节点是否已提交状态
     * @return
     */
    @RequestMapping(value = "/findjudgeTaskAnduserName",method = RequestMethod.GET)
    @ApiOperation("根据taskId和用户判断节点是否已提交状态")
    public Object findjudgeTaskAnduserName(String taskId) {
        return approveService.findjudgeTaskAnduserName(taskId);
    }



    /**
     * getAgencyAproveByUname
     * @param request
     * @param rows 行数
     * @param page 页数
     * @return
     */
    @RequestMapping(value = "getAgencyAproveByUname",method = RequestMethod.POST)
    @ApiOperation("查询代办列表")
    public Object getAgencyAproveByUname(HttpServletRequest request,
                                                     Approve approve,
                                                     @RequestParam(name = "rows", required = false) String rows,
                                                     @RequestParam(name = "page", required = false) String page){
        try {
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(approveService.getAgencyAproveByUname(approve,rows,page));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }


    /**
     * 我参与但未完成
     * @param rows 行数
     * @param page 页数
     * @return
     */
    @RequestMapping(value = "getParticipationIncomplete",method = RequestMethod.POST)
    @ApiOperation("我参与但未完成")
    public Object getParticipationIncomplete(@RequestParam(name = "rows", required = false) String rows,
                                             @RequestParam(name = "page", required = false) String page,String approveType){
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(approveService.getParticipationIncomplete(rows,page,approveType));
    }




    /**
     * 历史记录
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "getComments",method = RequestMethod.POST)
    @ApiOperation("历史记录")
    public Object getComments(HttpServletRequest request,
                                                     String processId){
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(this.workFlowOperateService.getComments(processId));

    }

    /**
     * 查询已审批过的节点以及正在审批节点信息
     * @param processId
     * @param
     * @return
     */
    @RequestMapping(value = "getApprovedNodeAndActiveNode",method = RequestMethod.POST)
    @ApiOperation("查询已审批过的节点以及正在审批节点信息")
    public Object getApprovedNodeAndActiveNode(String processId){
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(this.workFlowOperateService.getApprovedNodeAndActiveNode(processId));

    }


    /**
     * 1.通过实例id username查询是否有代办
     * 2.返回最后一步数据
     * @param processId 实例id
     * @return
     */
    @RequestMapping(value = "findLaststepData",method = RequestMethod.POST)
    @ApiOperation("返回最后一步数据")
    public Object findLaststepData(String processId){
        return approveComponent.findLaststepData(processId);
    }




    /**
     * 通过taskId查询
     * @param taskId
     * @param
     * @return
     */
    @RequestMapping(value = "selectByTaskId",method = RequestMethod.POST)
    @ApiOperation("通过taskId查询")
    public Object selectByTaskId(String taskId){
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(this.approveService.selectByTaskId(taskId));
    }






    /**
     * 查询待签收集合
     * @param request
     * @param rows 行数
     * @param page 页数
     * @return
     */
    @RequestMapping(value = "getProcessInstanceIdByWaitingReceipt",method = RequestMethod.POST)
    @ApiOperation("查询待签收集合")
    public Object getProcessInstanceIdByWaitingReceipt(HttpServletRequest request,
                                                     Approve approve,
                                                     @RequestParam(name = "rows", required = false) String rows,
                                                     @RequestParam(name = "page", required = false) String page){
        try {
            String uname= UserUtil.checkAndGetUser();
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(approveService.getProcessInstanceIdByWaitingReceipt(uname,rows,page,approve));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }





    /**
     * 签收
     * @param taskId
     * @return
     */
    @RequestMapping(value = "signUpforTasks",method = RequestMethod.POST)
    @ApiOperation("签收集合")
    public Object signUpforTasks(String taskId){
            if (StringUtils.isEmpty(taskId)){
                throw new ZtgeoBizException(MsgManager.TASK_NULL);
            }
            String [] taskList=taskId.split(",");
            for (String task:taskList){
                taskService.claim(task, UserUtil.checkAndGetUser());
                Task userTask=taskService.createTaskQuery().taskId(task).singleResult();
                userTask.setAssignee(UserUtil.checkAndGetUser());
                Approve approve=new Approve();
                approve.setApproveTaskId(task);
                approve.setApprovePerson(UserUtil.checkAndGetUser());
                //修改审批人信息
                approveService.updateApproveByTaskId(approve);
            }
            //修改approve表审核人设置uname
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(MsgManager.OPERATING_SUCCESS);

    }

    /**
     *  点击查看展示服务接口
     *
     * @param
     * @return
     */
    @RequestMapping(value = "findServiceByTaskId",method = RequestMethod.POST)
    @ApiOperation("点击查看展示服务接口")
    public Object findServiceByTakeId(String taskId){
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(this.approveService.findServiceByTakeId(taskId));
    }


    /**
     * 展示下一节点
     * @param processId
     * @return
     */
    @RequestMapping(value = "getNextTaskInfo",method = RequestMethod.POST)
    @ApiOperation("展示下一节点")
    public Object getNextTaskInfo(String processId){
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(approveService.getNextTaskInfo(processId));

    }






    /**
     *  创建业务-根据用户显示模板名称
     *
     * @param
     * @return
     */
    @RequestMapping(value = "findModelNameAndIdByFlow",method = RequestMethod.POST)
    @ApiOperation("创建业务-根据用户显示模板名称")
    public Object findModelNameAndIdByFlow(){
            String uname= UserUtil.checkAndGetUser();
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(this.approveService.findModelNameAndIdByFlow(null,uname));
    }

    /**
     *  挂起业务数据
     * @param
     * @return
     */
    @RequestMapping(value = "findHangUpFlow",method = RequestMethod.POST)
    @ApiOperation("挂起业务数据")
    public Object findHangUpFlow(HttpServletRequest request,String page,String rows){
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(approveService.findHangUpFlow(rows,page,request));
    }

    @RequestMapping(value = "getWebScoket",method = RequestMethod.POST)
    @ApiOperation("webscoket数据")
    public void getWebScoket(String username,String id){
        webSocket.sendMessage(username,webSocket.SendResultMsagge(username,id));
    }


    /**
     *  挂起业务数据
     * @param
     * @return
     */
    @RequestMapping(value = "getActivityAutoInterface",method = RequestMethod.POST)
    @ApiOperation("自动接口完成")
    public Object getActivityAutoInterface(Map<String,String> map,String taskId){
        try {
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(approveComponent.getActivityAutoInterface(taskId,map));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }



    /**
     *  "管理员查看整个流程,根据用户展示代办,已完成,只读"
     * @param
     * @return
     */
    @RequestMapping(value = "findInstanceQueryandReadOnly",method = RequestMethod.POST)
    @ApiOperation("管理员查看整个流程,根据用户展示代办,已完成,只读")
    public Object findInstanceQueryandReadOnly(String processId) throws IOException {
            return  approveService.findInstanceQueryandReadOnly(processId);
    }


    /**
     *  启动实例
     * @param
     * @return
     */
    @RequestMapping(value = "startFlow",method = RequestMethod.POST)
    @ApiOperation("启动实例")
    public Object startFlow(HttpServletRequest request) throws  IOException{
        String pictures = request.getParameter("base64Pictures");
        List<SjHandleFlow> sjHandleFlows = JSONArray.parseArray(pictures,SjHandleFlow.class);
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(approveService.startFlow(request,sjHandleFlows));
    }





    /** 首页
     *  我参与过但未完成的流程
     *  待我审批节点
     * @param request
     * @param rows 行数
     * @param page 页数
     * @return
     */
    @RequestMapping(value = "findCompletedByNotFinished",method = RequestMethod.POST)
    @ApiOperation("首页参与但未完成和待审批")
    public Object findCompletedByNotFinished(HttpServletRequest request,
                                                     Approve approve,
                                                     @RequestParam(name = "rows", required = false) String rows,
                                                     @RequestParam(name = "page", required = false) String page){
            String uname= UserUtil.checkAndGetUser();
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(approveService.findCompletedByNotFinished(uname,rows,page,approve));

    }





    /**
     * 根据用户名称获取审批单列表
     * @param request
     * @param rows 行数
     * @param page 页数
     * @return
     */
    @RequestMapping(value = "getCurrentUserHadActivityByPageAndRows",method = RequestMethod.POST)
    @ApiOperation("获取当前对象审批完成的节点")
    public Object getCurrentUserHadActivityByPageAndRows(HttpServletRequest request,
                                                     Approve approve,
                                                     @RequestParam(name = "rows", required = false) String rows,
                                                     @RequestParam(name = "page", required = false) String page){
            String uname= UserUtil.checkAndGetUser();
            ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
            return rv.data(approveService.getCurrentUserHadActivityByPageAndRows(uname,rows,page,approve));
    }




}
