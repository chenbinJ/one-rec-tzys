package com.ztgeo.general.biz.service_biz.chenbin.impl;

import com.ztgeo.general.biz.service_biz.chenbin.EntryBusinessBiz;
import com.ztgeo.general.entity.service_data.busi_data.FileEntryAndPowerEntity;
import com.ztgeo.general.entity.service_data.busi_data.FileEntryRespEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("entryBusiBiz")
public class EntryBusinessBizImpl implements EntryBusinessBiz {
    
    @Override
    public List<FileEntryAndPowerEntity> getFlieEntrysByProcessMouldId(String processMouldId) {
        return null;
    }

    @Override
    public String createProcnstFileEntry(String processInstanceId) {
        String processMouldId = "";//正常情况下从彭浩处获取
        return null;
    }

    @Override
    public FileEntryRespEntity getProcnstFileEntryAndInstance(String processInstanceId) {
        String processMouldId = "";//正常情况下从彭浩处获取
        return null;
    }

    @Override
    public String addFolderOrFileToFileInstance() {
        return null;
    }
}
