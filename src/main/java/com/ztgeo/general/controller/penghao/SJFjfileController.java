package com.ztgeo.general.controller.penghao;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.penghao.SJFjfileBiz;
import com.ztgeo.general.biz.penghao.impl.SJFjfileBizImpl;
import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.component.penghao.SJFjfileComponent;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.util.chenbin.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/enclosureManager/fileManager")
@Api(tags = {"附件相关API"})
public class SJFjfileController {

    @Autowired
    private SJFjfileBiz sjFjfileBiz;
    @Autowired
    private ApproveComponent approveComponent;


    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.POST)
    @ApiOperation("查看附件信息")
    public Object selectByPrimaryKey(String fileId) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return  rv.data(sjFjfileBiz.selectByPrimaryKey(fileId));
    }



    @RequestMapping(value = "/selectByViewAttachment", method = RequestMethod.GET)
    @ApiOperation("查看附件")
    public void selectByViewAttachment(String fileId, byte[] bytes, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            response.setHeader("Content-Type", "image/jpeg");
            byte[] photoByte = sjFjfileBiz.selectByViewAttachment(fileId, bytes);
            outputStream = response.getOutputStream();
            outputStream.write(photoByte);
            outputStream.flush();
        } catch (Exception e) {
            log.error("error", e);
            throw new ZtgeoBizException("FTP下载异常");
        } finally {
            try {
                if(outputStream!=null)
                outputStream.close();
            } catch (IOException e) {
                log.error("error", e);
            }
        }
    }

    @RequestMapping(value = "/deleteByAttachmentsAndEntryProcess", method = RequestMethod.POST)
    @ApiOperation("删除附件")
    public Object deleteByAttachmentsAndEntryProcess(String fjinstEntryId, String cid, String fileId,String taskId) {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        boolean flag=getQxByUsername(taskId);
        if (flag) {
            rv.setMessage(sjFjfileBiz.deleteEntryOrAttachmentsAndEntryProcess(fjinstEntryId, cid, fileId));
            return rv;
        }
        throw  new ZtgeoBizException(MsgManager.FLOW_PROCESSNAME_NOT);
    }

    @RequestMapping(value = "/insertSjFjinst", method = RequestMethod.POST)
    @ApiOperation("新增条目附件")
    public Object insertSjFjinst(String cid, String processId, String cname, String fjinstEntryId,String taskId) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        if (StringUtils.isEmpty(taskId)){
            throw new ZtgeoBizException("taskId为空,请从新传入taskId");
        }
        boolean flag=getQxByUsername(taskId);
        if (flag==true) {
            return rv.data(sjFjfileBiz.insertSjFjinst(fjinstEntryId, cid, processId, cname));
        }
        throw  new ZtgeoBizException(MsgManager.FLOW_PROCESSNAME_NOT);
    }


    @RequestMapping(value = "/insertByAttachmentsAndEntryProcess", method = RequestMethod.POST)
    @ApiOperation("扫描新增附件")
    public Object insertByAttachmentsAndEntryProcess(String fjinstEntryId, @Param("file") MultipartFile file, String finstId, String processId,String taskId) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        if (StringUtils.isEmpty(taskId)){
            throw new ZtgeoBizException("taskId为空,请从新传入taskId");
        }
        boolean flag=getQxByUsername(taskId);
        if (flag==true){
            return rv.data(sjFjfileBiz.insertByAttachmentsAndEntryProcess(fjinstEntryId, file, finstId, processId));
        }
        throw  new ZtgeoBizException(MsgManager.FLOW_PROCESSNAME_NOT);
    }


    private boolean getQxByUsername(String taskId){
        if (approveComponent.findUserByAdministrators(UserUtil.getUerId())==true){
            return  true;
        }
        HistoricTaskInstance historicTaskInstance = ProcessEngines.getDefaultProcessEngine().
                getHistoryService().createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (historicTaskInstance.getAssignee().equals(UserUtil.checkAndGetUser())){
            return  true;
        }
        return  false;
    }

}
