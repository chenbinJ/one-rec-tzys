package com.ztgeo.general.service.chenbin.tz.impl;

import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Bdc_Fw_Info;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Bdc_Gl;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Info_Handle_Result;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Info_Immovable;
import com.ztgeo.general.entity.tz_entity.TZBuilt;
import com.ztgeo.general.entity.tz_entity.TZBusinessData;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.chenbin.SJInfoManagerMapper;
import com.ztgeo.general.mapper.chenbin.tz.SjTzDataMapper;
import com.ztgeo.general.service.chenbin.tz.BuiltService;
import com.ztgeo.general.util.chenbin.IDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("builtService")
public class BuiltServiceImpl implements BuiltService {
    @Autowired
    private SjTzDataMapper sjTzDataMapper;
    @Autowired
    private SJInfoManagerMapper sjInfoManagerMapper;

    @Override
    public List<SJ_Info_Immovable> getImmovs(List<TZBuilt> builts) {
        List<SJ_Info_Immovable> immovs = new ArrayList<SJ_Info_Immovable>();
        if(builts!=null){
            for(TZBuilt built:builts){
                SJ_Info_Immovable immov = new SJ_Info_Immovable();
                immov.setKfqy(built.getKfqy());
                immov.setLpmc(built.getLpmc());
                immov.setLpzl(built.getLpzl());
                immov.setXmmc(built.getXmmc());
                immov.setZh(built.getZh());
                List<SJ_Bdc_Gl> bdcGls = new ArrayList<SJ_Bdc_Gl>();
                for(SJ_Bdc_Fw_Info fwInfo:built.getBdcInfo()){
                    SJ_Bdc_Gl bdcGl = new SJ_Bdc_Gl();
                    bdcGl.setImmovableType(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_FD);
                    bdcGl.setFwInfo(fwInfo);
                    bdcGls.add(bdcGl);
                }
                immov.setGlImmovableVoList(bdcGls);
                immovs.add(immov);
            }
        }
        return immovs;
    }

    @Override
    public List<TZBusinessData> getBusiData(String receiptNumber) {
        return sjTzDataMapper.selectTZBusinessDatas(receiptNumber);
    }

    @Override
    public String saveBusiDatas(List<TZBusinessData> tzDatas) {
        if(tzDatas!=null && tzDatas.size()>0) {
            List<TZBusinessData> tzBusis = sjTzDataMapper.selectTZBusinessDatas(tzDatas.get(0).getReceiptNumber());
            for (TZBusinessData tzData : tzDatas) {
                TZBusinessData tzData_index = null;
                if (tzBusis!=null){
                    for(TZBusinessData tzBusi:tzBusis){
                        if(tzBusi.getBusinessType().equals(tzData.getBusinessType())){
                            tzData_index = tzBusi;
                            break;
                        }
                    }
                }
                if(StringUtils.isNotBlank(tzData.getId())){
                    if(tzData_index != null && !tzData.getId().equals(tzData_index.getId())){
                        tzData.setId(tzData_index.getId());
                    }
                    sjTzDataMapper.updateTzBusinessData(tzData);
                    doMerge(tzData);
                }else{
                    if(tzData_index!=null){
                        tzData.setId(tzData_index.getId());
                        sjTzDataMapper.updateTzBusinessData(tzData);
                        doMerge(tzData);
                    }else{
                        tzData.setId(UUID.randomUUID().toString());
                        sjTzDataMapper.insertTzBusinessData(tzData);
                        doMerge(tzData);
                    }
                }
            }
        }
        return "保存成功";
    }

    private void doMerge(TZBusinessData tzData){
        SJ_Info_Handle_Result handleResult = doGetHandleResult(tzData);
        if(handleResult == null){
            throw new ZtgeoBizException("业务类型超出设定范围");
        }
        if(StringUtils.isBlank(handleResult.getInfoId())){
            handleResult.setInfoId(IDUtil.getInfoId());
            handleResult.setInsertTime(new Date());
            sjInfoManagerMapper.insertHandleResult(handleResult);
        }else{
            sjInfoManagerMapper.updateHandleResult(handleResult);
        }
    }

    private SJ_Info_Handle_Result doGetHandleResult(TZBusinessData tzData){
        List<SJ_Info_Handle_Result> handleResults = null;
        SJ_Info_Handle_Result handleResult = null;
        String preservationMan = null;
        switch (tzData.getBusinessType()){
            case "水":
                handleResults = sjInfoManagerMapper.selectHandleResults(tzData.getReceiptNumber(),"WaterHandleResultService");
                if(tzData.getCanDoDepart().equals("xx市自来水公司1")){
                    preservationMan = "testzls1";
                }else{
                    preservationMan = "testzls2";
                }
                if(handleResults==null || handleResults.size()<=0){
                    handleResults = getBaseHandleResult(
                            tzData.getReceiptNumber(),
                            "WaterHandleResultService",
                            null,
                            null,
                            "接口",
                            null
                    );
                }
                handleResult = handleResults.get(0);
                handleResult.setOldNumber(tzData.getOldBusinessNumber());
                handleResult.setProvideUnit(tzData.getCanDoDepart());
                handleResult.setPreservationMan(preservationMan);
                break;
            case "电":
                handleResults = sjInfoManagerMapper.selectHandleResults(tzData.getReceiptNumber(),"ElectricHandleResultService");
                preservationMan = "testdlgs";
                if(handleResults==null || handleResults.size()<=0){
                    handleResults = getBaseHandleResult(
                            tzData.getReceiptNumber(),
                            "ElectricHandleResultService",
                            null,
                            null,
                            "接口",
                            null
                    );
                }
                handleResult = handleResults.get(0);
                handleResult.setOldNumber(tzData.getOldBusinessNumber());
                handleResult.setProvideUnit(tzData.getCanDoDepart());
                handleResult.setPreservationMan(preservationMan);
                break;
            case "气":
                handleResults = sjInfoManagerMapper.selectHandleResults(tzData.getReceiptNumber(),"GasHandleResultService");
                preservationMan = "testtrq";
                if(handleResults==null || handleResults.size()<=0){
                    handleResults = getBaseHandleResult(
                            tzData.getReceiptNumber(),
                            "GasHandleResultService",
                            null,
                            null,
                            "接口",
                            null
                    );
                }
                handleResult = handleResults.get(0);
                handleResult.setOldNumber(tzData.getOldBusinessNumber());
                handleResult.setProvideUnit(tzData.getCanDoDepart());
                handleResult.setPreservationMan(preservationMan);
                break;
            case "广":
                handleResults = sjInfoManagerMapper.selectHandleResults(tzData.getReceiptNumber(),"CableTvHandleResultService");
                preservationMan = "testyxds";
                if(handleResults==null || handleResults.size()<=0){
                    handleResults = getBaseHandleResult(
                            tzData.getReceiptNumber(),
                            "CableTvHandleResultService",
                            null,
                            null,
                            "接口",
                            null
                    );
                }
                handleResult = handleResults.get(0);
                handleResult.setOldNumber(tzData.getOldBusinessNumber());
                handleResult.setProvideUnit(tzData.getCanDoDepart());
                handleResult.setPreservationMan(preservationMan);
                break;
        }
        return handleResult;
    }

    private List<SJ_Info_Handle_Result> getBaseHandleResult(
            String receipt_number,
            String serviceCode,
            String oldNumber,
            String provide_unit,
            String data_come_from_mode,
            String preservation_man
    ){
        List<SJ_Info_Handle_Result> handleResults = new ArrayList<SJ_Info_Handle_Result>();
        SJ_Info_Handle_Result handleResult = new SJ_Info_Handle_Result();
        handleResult.setReceiptNumber(receipt_number);
        handleResult.setServiceCode(serviceCode);
        handleResult.setOldNumber(oldNumber);
        handleResult.setProvideUnit(provide_unit);
        handleResult.setDataComeFromMode(data_come_from_mode);
        handleResult.setPreservationMan(preservation_man);
        handleResults.add(handleResult);
        return handleResults;
    }
}
