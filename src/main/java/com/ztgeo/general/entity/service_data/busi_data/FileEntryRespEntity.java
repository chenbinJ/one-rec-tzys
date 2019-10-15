package com.ztgeo.general.entity.service_data.busi_data;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjinst;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjtm;

import java.io.Serializable;
import java.util.List;

public class FileEntryRespEntity implements Serializable {
    private List<FileEntryAndPowerEntity> fileEntryVoList;
    private List<SJ_Fjinst> fileInstanceVoList;
}
