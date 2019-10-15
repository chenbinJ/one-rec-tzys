package com.ztgeo.general.service.chenbin.impl;

import com.ztgeo.general.entity.service_data.interface_response.interface_data.InterfaceData;
import com.ztgeo.general.mapper.chenbin.InterfaceDataHandleMapper;
import com.ztgeo.general.mapper.chenbin.RecordHandleMapper;
import com.ztgeo.general.service.chenbin.InterfaceDataHandleService;
import com.ztgeo.general.util.chenbin.IDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceDataHandleServiceImpl implements InterfaceDataHandleService {
    @Autowired
    private InterfaceDataHandleMapper interfaceDataHandleMapper;
    @Autowired
    private RecordHandleMapper recordHandleMapper;

    @Override
    public void addData(InterfaceData intfData) {
        List<InterfaceData> interfaceDataList = recordHandleMapper.selectInterfaceDatasWithOuterId(intfData.getOuterIdOrNo(),intfData.getInterfaceCode());
        if(interfaceDataList!=null) {
            for (InterfaceData interfaceData:interfaceDataList){
                String reqParams = interfaceData.getReqParams();
                if(StringUtils.isNotBlank(reqParams) && reqParams.equals(intfData.getReqParams())){
                    intfData.setId(interfaceData.getId());
                    break;
                }
            }
        }
        if(StringUtils.isBlank(intfData.getId())) {
            intfData.setId(IDUtil.getIntfDataId());
            interfaceDataHandleMapper.insertInterfaceData(intfData);
        }else{
            interfaceDataHandleMapper.updateInterfaceData(intfData);
        }
    }
}
