package com.ztgeo.general.util.chenbin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record_Ext;
import com.ztgeo.general.entity.service_data.interface_response.business.InquiryInformation;
import com.ztgeo.general.entity.service_data.interface_response.business.InquiryServiceData;
import com.ztgeo.general.entity.service_data.interface_response.business.information.ImmovableMortgageInquiryInformation;
import com.ztgeo.general.entity.service_data.interface_response.business.information.ImmovableObjectionInquiryInformation;
import com.ztgeo.general.entity.service_data.interface_response.business.information.ImmovableSequestrationInquiryInformation;
import com.ztgeo.general.entity.service_data.interface_response.interface_data.InterfaceData;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface_Params;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Service_Position;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Qlr_Info;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import org.apache.commons.lang3.StringUtils;
import com.ztgeo.general.entity.service_data.interface_response.business.information.ImmovableRightInquiryInformation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OtherUtils {
    public static Sj_Inquiry_Record initRecord(SJ_Interface intf,String serviceCode,String recordId,Object returnData,Map<String,String> req){
        Sj_Inquiry_Record record = new Sj_Inquiry_Record();
        if(StringUtils.isBlank(recordId)) {
            recordId = IDUtil.getRecordId();
        }
        record.setRecordId(recordId);
        record.setRecordTitle(
                UserUtil.checkAndGetUser()+
                        "关于"+intf.getInterfaceName()+
                        "接口的使用记录"
        );
        record.setInterfaceCode(intf.getInterfaceCode());
        record.setServiceCode(serviceCode);
        record.setReturnData(JSONObject.toJSONString(returnData));
        record.setExecutor(UserUtil.checkAndGetUser());
        record.setExecutionTime(new Date());
        if(StringUtils.isNotBlank(req.get("qlrmc"))){
            record.setApplicant(req.get("qlrmc"));
        }
        if(StringUtils.isNotBlank(req.get("qlrzjh")) || StringUtils.isNotBlank(req.get("zjhm"))){
            record.setIDNumber(StringUtils.isNotBlank(req.get("qlrzjh"))?req.get("qlrzjh"):req.get("zjhm"));
        }
        if(StringUtils.isBlank(record.getApplicant())){
            record.setApplicant("***");
        }
        if(StringUtils.isBlank(record.getIDNumber())){
            record.setIDNumber("*********");
        }
        return record;
    }

    public static List<Sj_Inquiry_Record_Ext> initRecordExts(Sj_Inquiry_Record record, Map<String,String> request,List<SJ_Interface_Params> intfParams){
        List<Sj_Inquiry_Record_Ext> recordExts = new ArrayList<Sj_Inquiry_Record_Ext>();
        for(SJ_Interface_Params param:intfParams){
            String extKey = param.getParamCode();
            String data_temp = request.get(extKey);
            if("否".equals(param.getIsFolder())) {//参数不为查询附件
                recordExts.add(new Sj_Inquiry_Record_Ext(
                    IDUtil.getExtId(),
                    record.getRecordId(),
                    param.getParamId(),
                    extKey,
                    param.getParamName(),
                    data_temp,
                    "否",
                    null
                ));
            }else{
                if(StringUtils.isNotBlank(data_temp)) {
                    String[] fptPaths = data_temp.split("\\$");
                    for (int i = 0; i < fptPaths.length; i++) {
                        int ifFileSort = i + 1;
                        String extValue = fptPaths[i];
                        recordExts.add(new Sj_Inquiry_Record_Ext(
                                IDUtil.getExtId(),
                                record.getRecordId(),
                                param.getParamId(),
                                extKey,
                                param.getParamName() + "-" + ifFileSort,
                                extValue,
                                "是",
                                ifFileSort
                        ));
                    }
                }
            }
        }
        return recordExts;
    }

    public static Sj_Inquiry_Record dealBelongRecord(List<SJ_Power_Service_Position> servicePositions, Sj_Inquiry_Record record, boolean isAdmin, Depart depart){
        if(record==null){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RESPONSE_RECORD_IS_NULL);
        }
        InquiryServiceData serviceData = new InquiryServiceData();
        List<InquiryInformation> serviceDataInfos = new ArrayList<InquiryInformation>();

        String serviceData_str = record.getReturnData();
        JSONObject serviceData_json = JSONObject.parseObject(serviceData_str);
        JSONArray serviceDataInfos_json = serviceData_json.getJSONArray("serviceDataInfos");

        //设置他项权检索状态，确定下方是否加载
        boolean isShowMortgage = false;
        boolean isShowSequestration = false;
        boolean isShowObjection = false;
        if(servicePositions==null){
            if(isAdmin){
                isShowMortgage = true;
                isShowSequestration = true;
                isShowObjection = true;
            }else{
                throw new ZtgeoBizException(BizOrBizExceptionConstant.POSITION_IS_NULL);
            }
        }else{
            for(SJ_Power_Service_Position servicePosition:servicePositions){
                if(StringUtils.isNotBlank(servicePosition.getShowMortgage()) && "1".equals(servicePosition.getShowMortgage())){//设置允许显示且权限完备的情况下
                    if(!(StringUtils.isNotBlank(servicePosition.getPermissionLevel()) && BizOrBizExceptionConstant.POWER_LEVEL_NONE.equals(servicePosition.getPermissionLevel()))) {
                        isShowMortgage = isShowMortgage || true;
                    }
                }
                if(StringUtils.isNotBlank(servicePosition.getShowSequestration()) && "1".equals(servicePosition.getShowSequestration())){
                    if(!(StringUtils.isNotBlank(servicePosition.getPermissionLevel()) && BizOrBizExceptionConstant.POWER_LEVEL_NONE.equals(servicePosition.getPermissionLevel()))) {
                        isShowSequestration = isShowSequestration || true;
                    }
                }
                if(StringUtils.isNotBlank(servicePosition.getShowObjection()) && "1".equals(servicePosition.getShowObjection())){
                    if(!(StringUtils.isNotBlank(servicePosition.getPermissionLevel()) && BizOrBizExceptionConstant.POWER_LEVEL_NONE.equals(servicePosition.getPermissionLevel()))) {
                        isShowObjection = isShowObjection || true;
                    }
                }
            }
        }

        boolean isSequestration = false;                    //是否存在查封
        boolean isObjection = false;                        //是否存在异议
        boolean isMortgage = false;                         //是否存在抵押

        //遍历该JSONArray
        if(serviceDataInfos_json.size()>0){
            for(int i=0;i<serviceDataInfos_json.size();i++){
                JSONObject serviceDataInfo_json = serviceDataInfos_json.getJSONObject(i);
                String informationType = serviceDataInfo_json.getString("informationType");
                switch (informationType){
                    case BizOrBizExceptionConstant.IMMOVABLE_RIGHT_INQUIRY_CODE:                    //权属
                        serviceDataInfos.add(JSONObject.toJavaObject(serviceDataInfo_json,ImmovableRightInquiryInformation.class));
                        break;
                    case BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_INQUIRY_CODE:                 //抵押
                        if(isShowMortgage) {
                            ImmovableMortgageInquiryInformation immovableMortgageInquiryInformation = JSONObject.toJavaObject(serviceDataInfo_json, ImmovableMortgageInquiryInformation.class);
                            List<SJ_Qlr_Info> mortgageHolderVoList = immovableMortgageInquiryInformation.getMortgageHolderVoList();
                            if(mortgageHolderVoList==null || mortgageHolderVoList.size()<=0){
                                throw new ZtgeoBizException(BizOrBizExceptionConstant.MORTGAGE_HOLD_MAN);
                            }

                            boolean isBelongSelf = false;
                            for(SJ_Qlr_Info mortgageHolder:mortgageHolderVoList){//查询抵押权人是否为自己
                                String obligeeName = mortgageHolder.getObligeeName();
                                if(depart!=null){
                                    if(depart.getName().equals(obligeeName)){
                                        isBelongSelf = true;
                                        break;
                                    }
                                }
                            }

                            if(isAdmin || isBelongSelf){
                                serviceDataInfos.add(immovableMortgageInquiryInformation);
                            }
                        }
                        isMortgage = isMortgage||true;
                        break;
                    case BizOrBizExceptionConstant.IMMOVABLE_SEQUESTRATION_INQUIRY_CODE:            //查封
                        if(isShowSequestration) {
                            serviceDataInfos.add(JSONObject.toJavaObject(serviceDataInfo_json, ImmovableSequestrationInquiryInformation.class));
                        }
                        isSequestration = isSequestration||true;
                        break;
                    case BizOrBizExceptionConstant.IMMOVABLE_OBJECTION_INQUIRY_CODE:                //异议
                        if(isShowObjection) {
                            serviceDataInfos.add(JSONObject.toJavaObject(serviceDataInfo_json, ImmovableObjectionInquiryInformation.class));
                        }
                        isObjection = isObjection||true;
                        break;
                    default:                                                                        //超出范围
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_RIGHT_OUT);
                }
            }
        }

        //遍历serviceDataInfos，设置抵押查封是否存在
        for(InquiryInformation serviceDataInfo:serviceDataInfos){
            if(BizOrBizExceptionConstant.IMMOVABLE_RIGHT_INQUIRY_CODE.equals(serviceDataInfo.getInformationType())){
                ImmovableRightInquiryInformation rightInfo = (ImmovableRightInquiryInformation)serviceDataInfo;
                rightInfo.setIsSequestration(isSequestration);
                rightInfo.setIsMortgage(isMortgage);
                rightInfo.setIsObjection(isObjection);
            }
        }

        //具备时是否是本单位的抵押可以看

        serviceData.setServiceCode(serviceData_json.getString("serviceCode"));
        serviceData.setServiceDataTo(serviceData_json.getString("serviceDataTo"));
        serviceData.setServiceDataInfos(serviceDataInfos);
        record.setReturnData(JSONObject.toJSONString(serviceData));
        return record;
    }

    public static InterfaceData initIntfData(String recordId,String processInstanceId,SJ_Interface intf,String serviceCode,Map<String,String> httpParams,Object obj){
        InterfaceData interfaceData = new InterfaceData();
        boolean isREC = false;
        boolean isACT = false;
        if(StringUtils.isNotBlank(recordId)){
            interfaceData.setOuterIdOrNo(recordId);
            interfaceData.setWitchOuter(BizOrBizExceptionConstant.INTERFACE_DATA_OUTER_CODE_REC);
            isREC = true;
        }
        if(!isREC && StringUtils.isNotBlank(processInstanceId)){
            interfaceData.setOuterIdOrNo(processInstanceId);
            interfaceData.setWitchOuter(BizOrBizExceptionConstant.INTERFACE_DATA_OUTER_CODE_ACT);
            isACT = true;
        }
        if((isREC && StringUtils.isNotBlank(processInstanceId)) || (!isREC && !isACT)){
            return null;
        }
//        interfaceData.setId(IDUtil.getIntfDataId());
        interfaceData.setInterfaceCode(intf.getInterfaceCode());
        interfaceData.setServiceCode(serviceCode);
        interfaceData.setReqParams(JSONObject.toJSONString(httpParams));
        interfaceData.setRespBody(JSONObject.toJSONString(obj));
        interfaceData.setExecutor(UserUtil.checkAndGetUser());
        interfaceData.setCheckTime(TimeUtil.getTimeString(new Date()));
        return interfaceData;
    }
}
