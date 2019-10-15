package com.ztgeo.general.biz.service_biz.chenbin;

import com.ztgeo.general.entity.service_data.busi_data.FileEntryAndPowerEntity;
import com.ztgeo.general.entity.service_data.busi_data.FileEntryRespEntity;

import java.util.List;

public interface EntryBusinessBiz {
    public List<FileEntryAndPowerEntity> getFlieEntrysByProcessMouldId(String processMouldId);//流程发起时获取流程条目及针对条目的权限
    public String createProcnstFileEntry(String processInstanceId);//创建流程实例的条目树，要求先保存后编辑附件树，保存时触发创建
    public FileEntryRespEntity getProcnstFileEntryAndInstance(String processInstanceId);//获取流程实例的附件树数据
    public String addFolderOrFileToFileInstance();//添加附件或附件菜单(子條目)
}
