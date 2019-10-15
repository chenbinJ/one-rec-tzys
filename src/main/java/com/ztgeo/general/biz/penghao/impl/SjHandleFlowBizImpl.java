package com.ztgeo.general.biz.penghao.impl;

import com.ztgeo.general.biz.penghao.SjHandleFlowBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import com.ztgeo.general.entity.service_data.pub_data.SjHandleFlow;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.penghao.SJ_FjfileMapper;
import com.ztgeo.general.mapper.penghao.SjHandleFlowMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SjHandleFlowBizImpl implements SjHandleFlowBiz {

    @Autowired
    private SjHandleFlowMapper sjHandleFlowMapper;
    @Autowired
    private SJ_FjfileMapper sjFjfileMapper;

    @Override
    public Object findHandleFlowByProcess(String processId) {
        if (StringUtils.isEmpty(processId)) {
            throw new ZtgeoBizException(MsgManager.PROCESS_NULL);
        }
        List<SjHandleFlow> sjHandleFlowList = sjHandleFlowMapper.findHandleFlowByProcess(processId);
        for (SjHandleFlow handleFlow : sjHandleFlowList) {
            List<SJ_Fjfile> fjtmList = this.sjFjfileMapper.selectSjFjfileByNameAndPnode(handleFlow.getHandleName(), processId);
            for (SJ_Fjfile fjfile : fjtmList) {
                if (fjfile.getFileName().equals(handleFlow.getHandleName() + "身份证图片")) {
                    handleFlow.setHandleIdentityFile(fjfile.getFileId());
                }
                if (fjfile.getFileName().equals(handleFlow.getHandleName() + "现场图片")) {
                    handleFlow.setHandleSceneFile(fjfile.getFileId());
                }
                sjHandleFlowMapper.updateByPrimaryKeySelective(handleFlow);
            }
        }
        //修改个人现场图片,身份图片信息
        return sjHandleFlowMapper.findHandleFlowByProcess(processId);
    }
}
