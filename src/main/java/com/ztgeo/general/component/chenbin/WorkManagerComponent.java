package com.ztgeo.general.component.chenbin;

import com.alibaba.fastjson.JSONArray;
import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.component.penghao.SjFjtmComponent;
import com.ztgeo.general.entity.service_data.outer_data.ImmovableFile;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Execute_depart;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjinst;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjtm;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.FileManager;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.chenbin.EntryManagerMapper;
import com.ztgeo.general.mapper.penghao.SJ_FjfileMapper;
import com.ztgeo.general.mapper.penghao.SJ_FjinstMapper;
import com.ztgeo.general.mapper.penghao.SJ_FjtmMapper;
import com.ztgeo.general.service.activity.ApproveService;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class WorkManagerComponent {

    @Value("${special.admin}")
    private String admin;


    @Autowired
    private ApproveComponent approveComponent;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private SjFjtmComponent sjFjtmComponent;
    @Autowired
    private SJ_FjtmMapper sj_fjtmMapper;
    @Autowired
    private SJ_FjinstMapper sj_fjinstMapper;
    @Autowired
    private SJ_FjfileMapper sj_fjfileMapper;
    @Autowired
    private EntryManagerMapper entryManagerMapper;

    public void dealFjRecordOuter(String fileVoList,String mid,String processId){
        List<ImmovableFile> bdcFiles = JSONArray.parseArray(fileVoList,ImmovableFile.class);

        List<SJ_Fjtm> sjFjtmList=sj_fjtmMapper.selectByMid(mid);
        List<SJ_Fjinst> list = sjFjtmComponent.getFjtmInst(sjFjtmList,mid,processId);
        List<SJ_Fjfile> fileList = new ArrayList<>();
        for(ImmovableFile bdcFile:bdcFiles){
            if(StringUtils.isNotBlank(bdcFile.getpName())){
                for(SJ_Fjinst fjInst:list){
                    if(StringUtils.isNotBlank(fjInst.getEntryId())) {
                        SJ_Fjtm fjtm = entryManagerMapper.selectByPrimaryKey(fjInst.getEntryId());
                        if(
                                fjtm!=null &&
                                StringUtils.isNotBlank(fjtm.getMappingName()) &&
                                fjtm.getMappingName().equals(bdcFile.getpName())        //匹配附件条目
                        ){
                            SJ_Fjfile file = new SJ_Fjfile();
                            SJ_Fjinst fjInst_child = new SJ_Fjinst();
                            String finstId = IDUtil.getFinstId();
                            String fileId = IDUtil.getFileId();

                            fjInst_child.setCid(finstId);
                            fjInst_child.setPid(fjInst.getCid());
                            fjInst_child.setCname(bdcFile.getFileName());
                            fjInst_child.setPnode(processId);
                            fjInst_child.setEntryId(fjtm.getEntryId());
                            fjInst_child.setCreateTime(new Date());
                            fjInst_child.setOrderNumber(bdcFile.getFileSequence());
                            fjInst_child.setCreateBy(UserUtil.checkAndGetUser());
                            fjInst_child.setCtype(fjInst.getCtype());
                            fjInst_child.setFileId(fileId);
                            fjInst_child.setPnodeType(FileManager.SJFJINST_PROCESS);
                            fjInst_child.setCkind(FileManager.SJFJINST_WENJIAN);
                            list.add(fjInst_child);

                            file.setFileId(fileId);
                            file.setFileName(
                                bdcFile.getFileName().replaceAll(
                                        bdcFile.getFileName().substring(bdcFile.getFileName().lastIndexOf(".")),
                                        ""
                                )
                            );
                            if(bdcFile.getFileSize()!=null) {
                                file.setFileSize(Double.toString(bdcFile.getFileSize()));
                            }
                            file.setLogicPath(bdcFile.getpName());
                            file.setXh(bdcFile.getFileSequence());
                            file.setFileExt(bdcFile.getFileType());
                            file.setFtpPath(bdcFile.getFileAddress());
                            sj_fjfileMapper.insertSelective(file);
                            fileList.add(file);
                            break;
                        }
                    }
                }
            }
        }
        sj_fjinstMapper.insertList(list);
    }

    public List<Task>  AutoSdq(String taskId, String processId, List<SJ_Execute_depart> execute_departs){
        //提交相关流程--penghao暴露相关组件给我
        List<Task> tasks = approveComponent.SubmittedFlow(
                taskId,
                processId,
                "agree",
                UserUtil.checkAndGetUser(),
                new HashMap<String,String>()
        );
        String username="";
        List<Task> taskList=null;
        for (SJ_Execute_depart excute: execute_departs) {
            for (Task task:tasks) {
                if (task.getTaskDefinitionKey().equals(MsgManager.SHUI)){
                    username=admin;
                }else if (task.getTaskDefinitionKey().equals(MsgManager.DIAN)){
                    username=admin;
                }else if (task.getTaskDefinitionKey().equals(MsgManager.QI)){
                    username=admin;
                }else if (task.getTaskDefinitionKey().equals(MsgManager.YXDS)) {
                    username = admin;
                }
                if (StringUtils.isNotEmpty(excute.getExecuteDepart())) {
                    if (excute.getExecuteDepart().equals(task.getTaskDefinitionKey())) {
                        taskList = approveComponent.SubmittedFlow(
                                task.getId(),
                                processId,
                                "agree",
                                username,
                                new HashMap<String, String>()
                        );
                    }
                }
            }
        }
        return taskList;
    }



    public List<Task> dealSubmitCommon(String taskId,String processInstanceId){
        //提交相关流程--penghao暴露相关组件给我
        List<Task> tasks = approveComponent.SubmittedFlow(
                taskId,
                processInstanceId,
                "agree",
                UserUtil.checkAndGetUser(),
                new HashMap<String,String>()
        );
        return tasks;
    }

    public List<Task> letSomeoneSubmit(String taskId,String processInstanceId,String username){
        //更新approve中候选人，若没有则插入新的

        //提交相关流程--penghao暴露相关组件给我
        List<Task> tasks = approveComponent.SubmittedFlow(
                taskId,
                processInstanceId,
                "agree",
                username,
                new HashMap<String,String>()
        );
        return tasks;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW , isolation = Isolation.DEFAULT
            ,rollbackFor = RuntimeException.class)
    public void dealAuto(List<Task> tasks){
        approveService.checkAndDealThisTasks(tasks);
    }
}
