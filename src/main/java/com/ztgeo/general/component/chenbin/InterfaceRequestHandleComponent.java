package com.ztgeo.general.component.chenbin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ztgeo.general.biz.DicItemBiz;
import com.ztgeo.general.biz.service_biz.chenbin.RecieveDataBusinessBiz;
import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.component.penghao.StepComponent;
import com.ztgeo.general.component.pubComponent.ServiceAuthorizationComponent;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record_Ext;
import com.ztgeo.general.entity.service_data.interface_response.interface_data.InterfaceData;
import com.ztgeo.general.entity.service_data.json_data.JSONReceiptData;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.entity.service_data.resp_data.RespServiceData;
import com.ztgeo.general.entity.service_data.sys_data.MyResult;
import com.ztgeo.general.entity.service_data.sys_data.PermissionLevelResultEntity;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.activity.ApproveMapper;
import com.ztgeo.general.mapper.chenbin.*;
import com.ztgeo.general.service.chenbin.InterfaceDataHandleService;
import com.ztgeo.general.service.chenbin.RecordHandleService;
import com.ztgeo.general.util.chenbin.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//参数处理component
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class InterfaceRequestHandleComponent {

    @Autowired
    private ApproveMapper approveMapper;
    @Autowired
    private IntfManagerMapper intfManagerMapper;
    @Autowired
    private SvrManagerMapper svrManagerMapper;
    @Autowired
    private SJInfoManagerMapper sJInfoManagerMapper;
    @Autowired
    private RecieveDataBusinessBiz recDataBiz;
    @Autowired
    private StepComponent stepComponent;
    @Autowired
    private ServiceAuthorizationComponent svrAuthComponent;
    @Autowired
    private ApproveComponent approveComponent;
    @Autowired
    private WorkManagerComponent workManagerComponent;
    @Autowired
    private StepManagerMapper stepManagerMapper;
    @Autowired
    private OtherComponent otherComponent;

    @Autowired
    private FileBusinessMapper fileBusinessMapper;

    @Autowired
    private DicItemBiz dicItemBiz;

    @Autowired
    private RecordHandleService recordHandleService;

    @Autowired
    private InterfaceDataHandleService interfaceDataHandleService;

    @Resource
    private HistoryService historyService;

    public Map<String,Object> requestHandle(Map<String,String> request){
        Map<String,Object> obj = new HashMap<String,Object>();
        Map<String,Object> rv_obj = new HashMap<String,Object>();
        //获取流程相关数据8
        String serviceCode = request.get("serviceCode");         //提送接口的服务标识
        String intfId = request.get("interfaceId");           //接口ID
        String ttaskId = request.get("taskId");
        String recordId = request.get("recordId");
        String taskId = (StringUtils.isNotBlank(ttaskId) && !ttaskId.equals("-1"))?ttaskId:request.get("nextTaskId");
        if(StringUtils.isNotBlank(taskId) && taskId.equals("-1")){
            taskId = null;
        }
        String stepId = request.get("stepId");
        String modelId = request.get("modelId");

        if(StringUtils.isBlank(intfId)){//接口ID为空
            throw new ZtgeoBizException(BizOrBizExceptionConstant.INTF_REQUEST_INTF_ID_NULL);
        }
        SJ_Interface intf = intfManagerMapper.selectIntfById(intfId);
        if(intf==null){//根据接口id没有能查询到接口信息
            throw new ZtgeoBizException(BizOrBizExceptionConstant.INTERFACE_IS_INVALID);
        }
        SJ_Power_Service_Interface svrIntfPower = null;
        boolean isIntfCode = false;
        /*
            鉴权开始
         */
        //鉴权
        if(StringUtils.isNotBlank(serviceCode)){//服务接口鉴权
            if(StringUtils.isBlank(taskId) && StringUtils.isBlank(stepId) && StringUtils.isBlank(modelId)){//外部服务
                //验证用户-岗位-服务权限(包含管理员权限验证)
                if(!checkUserAndService(serviceCode)){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.SERVICE_USER_POWER_OUT);
                }
                //验证服务-接口权限
                if(!checkServiceInterfacePower(serviceCode,intfId)){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.SERVICE_INTERFACE_POWER_OUT);
                }
            }else{  //步骤服务
                if(StringUtils.isBlank(taskId) && StringUtils.isBlank(stepId)){
                    stepId = ((SJ_Act_Step)stepComponent.getStepByStartNode(modelId)).getStepId();
                }
                //验证任务状态是否已完结,是否本人签收提交保存
                checkUserPositAndTaskPower(taskId,stepId);
                //验证步骤-服务权限是否成立
                if(StringUtils.isNotBlank(taskId)){
                    stepId = (String) stepComponent.getSteps(taskId);
                    if(!checkStepAndService(stepId,serviceCode)){
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.STEP_SERVICE_POWER_OUT);
                    }
                }
                //验证服务-接口权限是否成立
                if(!checkServiceInterfacePower(serviceCode,intfId)){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.SERVICE_INTERFACE_POWER_OUT);
                }
            }
            svrIntfPower = svrManagerMapper.selectSvrIntfBySvrCodeAndIntfId(serviceCode,intfId);
            //执行服务中接口
            obj.put("serviceCode",serviceCode);
        }else{//步骤接口鉴权
            if(StringUtils.isBlank(taskId)){
                throw new ZtgeoBizException("接口调用必要参数传入异常");
            }
            System.err.println("执行自动接口："+intf.getInterfaceName());
            stepId = (String) stepComponent.getSteps(taskId);
            //验证步骤-接口权限是否成立，待开发（查询出来就行）
            SJ_Power_Step_Interface stepIntf = stepManagerMapper.selectStepIntfByThemId(stepId,intfId);
            if(stepIntf==null){//步骤接口越权
                throw new ZtgeoBizException(BizOrBizExceptionConstant.STEP_SERVICE_POWER_OUT);
            }
            if(StringUtils.isNotBlank(stepIntf.getSetUp()) && stepIntf.getSetUp().equals("手动")){
                checkUserPositAndTaskPower(taskId,stepId);
            }
        }
        /*
            鉴权结束
         */

        List<SJ_Interface_Params> intfParams = intf.getParamVoList();//查询到的预先配置的接口参数配置
        Map<String,String> httpParams = new HashMap<String,String>();//http请求时携带的参数map集合

        //设置参数集合
        for(SJ_Interface_Params param:intfParams){
            if("否".equals(param.getIsFolder())) {//参数不为查询附件
                httpParams.put(
                        param.getParamCode(),
                        request.get(param.getParamCode())
                );
            }else{
                if(StringUtils.isBlank(request.get(param.getParamCode()))){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.QUERY_PARAM_FILE_IS_NULL);
                }
            }
        }
        System.err.println("此次参数为："+ JSONObject.toJSONString(httpParams));
        //设置接口头
        Map<String,String> header = new HashMap<String,String>();
        String httpResp = HttpClientUtil.sendHttp(MyHttpUtil.getRequestMethod(intf.getReqMethod()),intf.getContentType(),intf.getInterfaceURL(),httpParams,header);
        System.err.println("返回结果为："+httpResp);
        //返回值处理
        JSONObject object = JSONObject.parseObject(httpResp);
        if(object.getInteger("status")==200){
            rv_obj.put("status",200);
            if(StringUtils.isNotBlank(serviceCode)){
                //返回是对象的情况
//                List<Object> data = new ArrayList<Object>();
//                data.add(object.get("data"));
                //可以使用log记录具体data
                if(svrIntfPower!=null){
                    //填入对应数据
                    if(StringUtils.isNotBlank(svrIntfPower.getDataCode()) && svrIntfPower.getDataCode().equals("InterfaceData")){
                        isIntfCode = true;
                        InterfaceData intfData = null;
//                        obj.remove("serviceCode");
                        obj.put("interfaceCode",intf.getInterfaceCode());
                        obj.put("interfaceDatas",object.get("data"));
                        if((StringUtils.isNotBlank(recordId) && StringUtils.isNotBlank(taskId)) || (StringUtils.isBlank(recordId) && StringUtils.isBlank(taskId))){
                            throw new ZtgeoBizException("接口调用混乱，不清晰的使用者（服务或步骤）");
                        }
                        if(StringUtils.isNotBlank(recordId)){
                            intfData = OtherUtils.initIntfData(recordId,null,intf,serviceCode,httpParams,object.get("data"));
                        }
                        if(StringUtils.isNotBlank(taskId)){
                            HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
                            if(task!=null) {
                                intfData = OtherUtils.initIntfData(null, task.getProcessInstanceId(), intf, serviceCode, httpParams, object.get("data"));
                            }
                        }
                        if(intfData!=null){//写接口数据
                            interfaceDataHandleService.addData(intfData);
                        }
                    } else {
                        obj.put("serviceDataInfos",object.get("data"));
                    }
                }else{
                    throw new ZtgeoBizException("服务接口越权");
                }
                List<Object> serviceDatas = new ArrayList<Object>();
                serviceDatas.add(obj);
                rv_obj.put("serviceDatas",serviceDatas);
                if(isIntfCode)
                    rv_obj = obj;
            } else {
                rv_obj.put("data",object.get("data"));
            }
            //是否保存接口查询记录
            if(StringUtils.isBlank(taskId) && StringUtils.isBlank(stepId) && StringUtils.isBlank(modelId)){//外部服务
                if(!isIntfCode) {       //外部服务接口数据
                    //保存查询条件和结果
                    rv_obj.remove("status");
                    Sj_Inquiry_Record record = OtherUtils.initRecord(intf, serviceCode, recordId, obj, request);
                    List<Sj_Inquiry_Record_Ext> record_exts = OtherUtils.initRecordExts(record, request, intfParams);
                    if ("是".equals(intf.getIsRecord())) {
                        recordHandleService.addRecordInfos(record, record_exts);//执行记录
                    }
                    //是否按岗位-服务数据加载策略加载数据（如果service同意按岗位-服务加载策略来那就按策略加载，并要求抵押单位不得查看别家）
                    SJ_Service service = svrManagerMapper.selectSvrByCode(serviceCode);
                    if(service!=null && StringUtils.isNotBlank(service.getPositionServiceShowSetting()) && service.getPositionServiceShowSetting().equals("1")) {
                        record = otherComponent.takeCanUseRecord(serviceCode, record, record_exts);//处理记录信息
                    }
                    if(record.getRecordExts()==null){
                        record.setRecordExts(record_exts);
                    }
                    rv_obj.remove("serviceDatas");
                    rv_obj.put("serviceData", record);
                }
            }
        }else{
            String msg = StringUtils.isNotBlank((String)object.get("data"))?(String)object.get("data"):(String)object.get("message");
            throw new ZtgeoBizException(StringUtils.isNotBlank(msg)?msg:"接口请求执行异常");
        }
        System.out.println("本次接口处理返回值为："+JSONObject.toJSONString(rv_obj));
        return rv_obj;
    }

    public MyResult DealRecieveFromOuter1(Map<String,String> sjsqMap,boolean isSubmit) throws ParseException {
        boolean isOk = true;
        MyResult<Approve> rv = new MyResult<Approve>();
        /*
         保存数据
        */
        //保存数据
        String obj = JSONObject.toJSONString(sjsqMap);
        JSONReceiptData sjsq_JSON_str = JSON.parseObject(obj,JSONReceiptData.class);//json转实体类(SJSQ)
        String slbh = sjsq_JSON_str.getRegisterNumber();
        if(StringUtils.isBlank(sjsq_JSON_str.getReceiptNumber())) {
            log.error("本次受理编号为："+slbh);
            sjsq_JSON_str.setReceiptNumber(getReceiptNumber(slbh));
        }
        //签收办件
        Approve approve = (Approve)approveComponent.getTaskSettingupSignature(UserUtil.checkAndGetUser(),sjsq_JSON_str.getReceiptNumber());
        if(approve!=null){
            rv.setParams(approve);
        }
        //获得待保存对象
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(sjsq_JSON_str,null,null,null);

        //根据收件编号和当前用户获取当前流程任务节点信息，若不存在则抛出异常
        String receiptNumber = sjsq.getReceiptNumber();
        String taskId = approveComponent.getApproveByPersonOrFkId(receiptNumber).getApproveTaskId();

        //是否预验证
        if(isSubmit){
            //预验证数据是否对应服务
            isOk = recDataBiz.preCheckServicesForAotuSubmitOut(taskId,sjsq);
        }
        //是否可以执行保存
        if(isOk) {
            //保存数据
            recDataBiz.saveServiceData(sjsq, taskId);
            rv.setSuccess(true);
        } else {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_NOT_MATCH);
        }
        return rv;
    }

    @Transactional
    public List<Task> DealRecieveFromOuter2(Map<String,String> sjsq) throws ParseException {
        MyResult<Approve> saveResult = DealRecieveFromOuter1(sjsq,true);
        List<Task> tasks = null;
        String executeDeparts= sjsq.get("executeDeparts");
        if (StringUtils.isNotBlank(executeDeparts) && saveResult.isSuccess()){
            List<SJ_Execute_depart> execute_departs=(List<SJ_Execute_depart>) JSONArray.toCollection(JSONArray.fromObject(executeDeparts),SJ_Execute_depart.class);
           tasks = workManagerComponent.AutoSdq(saveResult.getParams().getApproveTaskId(),saveResult.getParams().getApproveProcessinstanceid(),execute_departs);
           for(Task task:tasks){

           }
        }else if (saveResult.isSuccess()){
        //先进行数据保存
//        (saveResult.isSuccess()) {
            //提交相关流程--penghao暴露相关组件给我
            tasks = workManagerComponent.dealSubmitCommon(saveResult.getParams().getApproveTaskId(),saveResult.getParams().getApproveProcessinstanceid());
        }
        return tasks;
    }

    @Transactional
    public boolean DealRecieveFromOuter3(Map<String,String> sjsq){
        boolean result = false;
        //验证用户有无实例操作权限

        return result;
    }

    public List<SJ_Fjfile> DealRecieveFromOuter4(String receiptNumber){
        List<SJ_Fjfile> files = new ArrayList<SJ_Fjfile>();
        List<Approve> approveList = this.approveMapper.getApproveByApproveFkId(receiptNumber);
        if(approveList==null || approveList.size()<=0){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_INSTANCE_NOT_EXIST);
        }
        //获取实例ID
        String processInstanceId = approveList.get(0).getApproveProcessinstanceid();
        //根据实例ID获取当前办件的办件实例集合（分用户）
        List<SJ_Fjinst> fjinsts = otherComponent.getProcInstEasy(processInstanceId);
        for(SJ_Fjinst fjinst:fjinsts) {
            //查询条目名称并设置为pname
            String pname = fileBusinessMapper.selectEntryNameById(fjinst.getEntryId());
            if(StringUtils.isBlank(pname)){
                fjinsts.remove(fjinst);     //移除已失效条目
            }
            SJ_Fjfile file = fileBusinessMapper.selectFileByFileId(fjinst.getFileId());
            file.setLogicPath(pname);
//            file.setXh(file.getXh());

            String isDue = dicItemBiz.getDicWithPandC("相关控制","附件处理").getItemval();
            if(StringUtils.isNotBlank(isDue) && isDue.equals("是")){
                //下载图片并处理为base64

            }
            files.add(file);
        }
        return files;
    }

    public String getReceiptNumber(String slbh){
        //验证是否有正常的受理编号进入，后续可能会扩展出各个不同的系统标识验证
        if(StringUtils.isBlank(slbh)){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECIEVE_DATA_CAN_NOT_USED);
        }
        //查找办件
        String receiptNumber = sJInfoManagerMapper.selectReceiptNumberByRegisterNumber(slbh);
        //查无此件
        if(StringUtils.isBlank(receiptNumber)){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DO_NOT_HAVE_THIS_RECORDE);
        }
        return receiptNumber;
    }

    private String getRequestAttr(HttpServletRequest request,String key){
        return StringUtils.isBlank(request.getParameter(key))?
                (String) request.getAttribute(key):
                request.getParameter(key);
    }
    //用户-岗位-步骤权限
    private boolean checkUserPositAndTaskPower(String taskId,String stepId){
        //验证任务状态是否已完结,是否本人签收提交保存
        if(StringUtils.isNotBlank(taskId) && !approveComponent.findPowerByTask(taskId)){   //判断任务状态和处理人
            throw new ZtgeoBizException(BizOrBizExceptionConstant.TASK_IS_ALREADY_DEAL);
        }
        //验证用户-岗位-步骤权限是否成立
        if(!checkUserAndStep(taskId,stepId)){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.USER_STEP_POWER_OUT);
        }
        return true;
    }

    //是否有权操作外部服务
    private boolean checkUserAndService(String serviceCode){
        PermissionLevelResultEntity result = svrAuthComponent.checkUserServicePower(serviceCode,UserUtil.getUerId());
        if(result.isResult()){//是否辨别具体权限在if判断中追加确定
            return true;
        }
        return false;
    }

    //是否有权操作流程(能进这里说明两个不可能同时为空)
    private boolean checkUserAndStep(String taskId,String stepId){
        if(StringUtils.isNotBlank(taskId))
            stepId = (String) stepComponent.getSteps(taskId);
        if(StringUtils.isBlank(stepId)){    //未对应任务步骤信息
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SAVED_TASK_MAPPING_STEP_IS_EMPTY);
        }
        //权限限定
        return svrAuthComponent.checkStepUserStepPower(stepId,UserUtil.getUerId());
    }

    //是否具备步骤服务写权限
    private boolean checkStepAndService(String stepId,String serviceCode){
        PermissionLevelResultEntity powerLevel = svrAuthComponent.getStepServicePower(stepId,serviceCode);
        if(powerLevel!=null && powerLevel.isResult()){
            if(powerLevel.getPermissionLevel().contains(BizOrBizExceptionConstant.POWER_LEVEL_WRITE)){
                return true;
            }
        }
        return false;
    }

    //是否服务具备接口处理权限
    private boolean checkServiceInterfacePower(String serviceCode,String interfaceId){
        return svrAuthComponent.checkServiceInterfacePower(serviceCode,interfaceId);
    }

    //通过任务Id获取收件信息
    public SJ_Sjsq getSjsqInfoFromTask(String receiptNumber,String taskId){
        System.out.println("receiptNumber:"+receiptNumber+",taskid:"+taskId);
        String stepId = (String) stepComponent.getSteps(taskId);
        if(StringUtils.isBlank(stepId))
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SAVED_TASK_MAPPING_STEP_IS_EMPTY);

        /*
            加载步骤-服务列表
        */

        //新建接收实体及收件信息集合
        SJ_Sjsq sjsq = new SJ_Sjsq();
        List<RespServiceData> serviceDatas = new ArrayList<RespServiceData>();
        //通过taskId查询sqbh,已解决
        if(StringUtils.isBlank(receiptNumber)) {
            receiptNumber = approveMapper.selectByTaskId(taskId).getApproveFkId();
        }
        if(StringUtils.isBlank(receiptNumber)){
            System.out.println("进入异常："+receiptNumber+"  ==  "+taskId);
            //抛出任务挂接业务信息异常
            throw new ZtgeoBizException(BizOrBizExceptionConstant.CAN_NOT_GET_RECEIPT_FROM_TASK);
        }

        //查询条件实体类
        SJ_Power_Step_Service ssp_param = new SJ_Power_Step_Service();
        //设置查询条件
        ssp_param.setStepId(stepId);
        ssp_param.setStatus("可用");
        //查询当前步骤服务权限列表
        List<SJ_Power_Step_Service> ssps = stepManagerMapper.selectStepSvrs(ssp_param);
        if(ssps!=null && ssps.size()>0) {
            for (int i=0;i<ssps.size();i++) {//遍历权限列表
                SJ_Power_Step_Service ssp = ssps.get(i);
                if (
                        StringUtils.isBlank(ssp.getPermissionLevel())   //默认为读权限
                                || ssp.getPermissionLevel().equals(BizOrBizExceptionConstant.POWER_LEVEL_READ)
                                || ssp.getPermissionLevel().equals(BizOrBizExceptionConstant.POWER_LEVEL_NULL_WRITE)
                                || ssp.getPermissionLevel().equals(BizOrBizExceptionConstant.POWER_LEVEL_WRITE)
                ) {//只读，空写，写权限
                    SJ_Service svr = svrManagerMapper.selectSvrById(ssp.getServiceId());//查询全部服务
                    if (StringUtils.isBlank(svr.getStatus()) || svr.getStatus().equals("可用")) {//筛选可用服务
                        SJ_Sjsq temp = recDataBiz.findSjsqServiceDataByCode(receiptNumber, svr.getServiceCode(), svr.getServiceDataTo());//加载服务数据
                        if(i==0){
                            sjsq = temp;//赋初值
                            System.out.println("第一次的收件申请信息："+JSONObject.toJSONString(sjsq));
                        }
                        List<RespServiceData> serviceDatas_temp = temp.getServiceDatas();
                        for(RespServiceData serviceData:serviceDatas_temp){//赋后续值
                            serviceData.setServiceDataTo(svr.getServiceDataTo());
                            serviceDatas.add(serviceData);
                        }
                    }
                }
            }
            System.out.println("全部收件信息："+JSONObject.toJSONString(sjsq));
        }
        //设置服务信息参数
        sjsq.setServiceDatas(serviceDatas);
        return sjsq;
    }

}
