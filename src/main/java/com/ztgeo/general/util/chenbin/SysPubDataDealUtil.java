package com.ztgeo.general.util.chenbin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.json_data.*;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.entity.service_data.resp_data.RespServiceData;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.chenbin.SvrManagerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SysPubDataDealUtil {
    public static SJ_Sjsq parseReceiptData(String receiptData,String serviceCode,String serviceDataTo,String receiptNumber) throws ParseException {
        JSONReceiptData sjsq_JSON_str = JSON.parseObject(receiptData,JSONReceiptData.class);//json转实体类
        return parseReceiptData(sjsq_JSON_str,serviceCode,serviceDataTo,receiptNumber);
    }

    public static SJ_Sjsq parseReceiptData(JSONReceiptData sjsq_JSON_str,String serviceCode,String serviceDataTo,String receiptNumber){
        String serviceCode_start = serviceCode;
        String serviceDataTo_start = serviceDataTo;
        String JSON_serviceDatas = sjsq_JSON_str.getServiceDatas();
        String JSON_executeDeparts = sjsq_JSON_str.getExecuteDeparts();
        System.out.println("目标数据："+JSON_serviceDatas+"执行单位："+JSON_executeDeparts);
        sjsq_JSON_str.setServiceDatas(null);
        sjsq_JSON_str.setExecuteDeparts(null);
        SJ_Sjsq sjsq = copyJSONReceiptDataToSjsq(sjsq_JSON_str);//信息拷贝至收件申请对象中
//        System.out.println("2:"+JSONObject.toJSONString(sjsq));
        if(sjsq==null){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_INFO_NULL_ERROR);
        }
        if(StringUtils.isNotBlank(receiptNumber) && StringUtils.isBlank(sjsq.getReceiptNumber())){//处理收件编号
            sjsq.setReceiptNumber(receiptNumber);
        }
        if(StringUtils.isBlank(sjsq.getReceiptNumber())){//验证收件编号
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_NUMBER_NULL_ERROR);
        }
        if(StringUtils.isNotBlank(JSON_executeDeparts)){//处理指定的执行部门
            List<SJ_Execute_depart> departs = JSONArray.parseArray(JSON_executeDeparts,SJ_Execute_depart.class);
            if(departs!=null){
                for(SJ_Execute_depart depart:departs){
                    if(StringUtils.isBlank(depart.getReceiptNumber())) {
                        depart.setReceiptNumber(sjsq.getReceiptNumber());
                    } else if(!depart.getReceiptNumber().equals(sjsq.getReceiptNumber())){
                        throw new ZtgeoBizException("业务执行部门非法注入");
                    }
                }
            }
            sjsq.setExecuteDeparts(departs);
        }
        if(JSON_serviceDatas!=null&&JSON_serviceDatas.length()>0) {
            List<JSONServiceData> serviceDatas = JSONArray.parseArray(JSON_serviceDatas, JSONServiceData.class);
            for (JSONServiceData serviceData : serviceDatas) {
                String serviceCode_temp = serviceData.getServiceCode();
                String serviceDataTo_temp = serviceData.getServiceDataTo();
                serviceCode = serviceCode_start;
                serviceDataTo = serviceDataTo_start;
                if(serviceCode_temp!=null && serviceCode_temp.length()>0){
                    serviceCode = serviceCode_temp;
                }
                if(serviceDataTo_temp!=null && serviceDataTo_temp.length()>0){
                    serviceDataTo = serviceDataTo_temp;
                }
                System.out.println("serviceCode:"+serviceCode+"serviceDataTo:"+serviceDataTo);
                serviceDataTo = checkServiceInfo(serviceCode,serviceDataTo);//验证这两项不为空
                String JSON_serviceDataInfos = serviceData.getServiceDataInfos();
                switch (serviceDataTo) {
                    //使用操作表作为判断条件
                    case BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE:
                        dealBdcqlxxInfos(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_RECEIPT_SERVICE:
                        dealBdcdyxxInfos(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE:
                        dealJyhtxxInfos(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE:
                        dealDyhtxxInfos(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.TAXATION_RECEIPT_SERVICE:
                        dealQsxxInfos(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.HANDLE_RESULT_SERVICE:
                        dealHandleResults(JSON_serviceDataInfos, sjsq, serviceCode);
                        break;
                    case BizOrBizExceptionConstant.IMMOVEABLE_BUILDING_SERVICE:
                        dealBuildings(JSON_serviceDataInfos, sjsq, serviceCode);
                    default:
                        log.error("入库表标识为：" + serviceDataTo + "的表标识常量未定义");
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_TABLE_ERROR_MSG);
                }
            }
        }
        return sjsq;
    }

    //处理权利信息
    public static void dealBdcqlxxInfos(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode){

        List<JSONBdcqlxgxx> qlxgxxs = JSONArray.parseArray(JSON_serviceDataInfos, JSONBdcqlxgxx.class);//证书权利集合的JSON对象
        List<SJ_Info_Bdcqlxgxx> sj_qlxgxxs = sjsq.getImmovableRightInfoVoList()==null?new ArrayList<SJ_Info_Bdcqlxgxx>():sjsq.getImmovableRightInfoVoList();//证书权利集合

        for(JSONBdcqlxgxx qlxgxx:qlxgxxs){
            boolean HaveOrNot = true;   //是否传入了义务人的情况
            System.out.println("11:"+qlxgxx.getRegistrationDate());
            String JSON_glImmovableVoList = qlxgxx.getGlImmovableVoList();
            String JSON_glObligeeVoList = qlxgxx.getGlObligeeVoList();
            String JSON_glObligorVoList = qlxgxx.getGlObligorVoList();

            if(JSON_glObligorVoList==null || JSON_glObligorVoList.length()<=0){
                HaveOrNot = false;  //判断义务人是否必须
            }

            qlxgxx.setGlImmovableVoList(null);
            qlxgxx.setGlObligeeVoList(null);
            qlxgxx.setGlObligorVoList(null);

            //反转出不动产权利信息
            SJ_Info_Bdcqlxgxx sj_qlxgxx = JSON.parseObject(JSON.toJSONString(qlxgxx), SJ_Info_Bdcqlxgxx.class);
            baseSetting(sj_qlxgxx,serviceCode,sjsq.getReceiptNumber());

            /*
             *  不动产图属关联信息处理
             *  证书中关联的不动产
             */
            List<SJ_Bdc_Gl> sj_bdcgls = copyJSONBdcToSJBdc(JSON_glImmovableVoList,BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE,sj_qlxgxx,sjsq);

            /*
             *  不动产权利人关联信息处理
             */
            List<SJ_Qlr_Gl> sj_qlrgls = copyJSONQlrToSJQlr(JSON_glObligeeVoList,BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR);

            /*
             *  不动产义务人关联信息处理
             */
            if(HaveOrNot) {
                List<SJ_Qlr_Gl> sj_ywrgls = copyJSONQlrToSJQlr(JSON_glObligorVoList,BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_YWR);
                sj_qlxgxx.setGlObligorVoList(sj_ywrgls);
            }
            sj_qlxgxx.setGlImmovableVoList(sj_bdcgls);
            sj_qlxgxx.setGlObligeeVoList(sj_qlrgls);

            sj_qlxgxxs.add(sj_qlxgxx);
        }
        sjsq.setImmovableRightInfoVoList(sj_qlxgxxs);
    }

    //处理抵押信息
    public static void dealBdcdyxxInfos(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode){
        List<JSONBdcdyxgxx> dyxgxxs = JSONArray.parseArray(JSON_serviceDataInfos, JSONBdcdyxgxx.class);
        List<Sj_Info_Bdcdyxgxx> sj_dyxgxxs = sjsq.getImmovableCurrentMortgageInfoVoList()==null ? new ArrayList<Sj_Info_Bdcdyxgxx>():sjsq.getImmovableCurrentMortgageInfoVoList();
        for(JSONBdcdyxgxx dyxgxx:dyxgxxs){
            System.out.println("11:"+dyxgxx.getRegistrationDate());
            String JSON_glImmovableVoList = dyxgxx.getGlImmovableVoList();//不动产json
            String JSON_glMortgagorVoList = dyxgxx.getGlMortgagorVoList();//抵押人json
            String JSON_glMortgageHolderVoList = dyxgxx.getGlMortgageHolderVoList();//抵押权人json

            dyxgxx.setGlImmovableVoList(null);
            dyxgxx.setGlMortgagorVoList(null);
            dyxgxx.setGlMortgageHolderVoList(null);

            //反转出不动产抵押信息
            Sj_Info_Bdcdyxgxx sj_dyxgxx = JSON.parseObject(JSON.toJSONString(dyxgxx), Sj_Info_Bdcdyxgxx.class);
            baseSetting(sj_dyxgxx,serviceCode,sjsq.getReceiptNumber());

            /*
             *  不动产抵押关联信息处理
             *  证明中不动产关联集合对象
             */
            List<SJ_Bdc_Gl> sj_bdcgls = copyJSONBdcToSJBdc(JSON_glImmovableVoList,BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_RECEIPT_SERVICE,sj_dyxgxx,sjsq);

            /*
             *  不动产抵押权人关联信息处理
             */
            List<SJ_Qlr_Gl> sj_dyqrgls = copyJSONQlrToSJQlr(JSON_glMortgageHolderVoList,BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYQR);

            /*
             *  不动产抵押人关联信息处理
             */
            List<SJ_Qlr_Gl> sj_dyrgls = copyJSONQlrToSJQlr(JSON_glMortgagorVoList,BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYR);

            sj_dyxgxx.setGlImmovableVoList(sj_bdcgls);
            sj_dyxgxx.setGlMortgageHolderVoList(sj_dyqrgls);
            sj_dyxgxx.setGlMortgagorVoList(sj_dyrgls);

            sj_dyxgxxs.add(sj_dyxgxx);
        }
        sjsq.setImmovableCurrentMortgageInfoVoList(sj_dyxgxxs);
    }

    public static void dealJyhtxxInfos(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode){
        List<JSONJyhtxx> jyhtxxs = JSONArray.parseArray(JSON_serviceDataInfos,JSONJyhtxx.class);
        List<Sj_Info_Jyhtxx> sj_jyhtxxs = new ArrayList<Sj_Info_Jyhtxx>();
        for(JSONJyhtxx jyhtxx:jyhtxxs){
            String JSON_glImmovableVoList = jyhtxx.getGlImmovableVoList();
            String JSON_glHouseBuyerVoList = jyhtxx.getGlHouseBuyerVoList();
            String JSON_glHouseSellerVoList = jyhtxx.getGlHouseSellerVoList();
            jyhtxx.setGlHouseBuyerVoList(null);
            jyhtxx.setGlHouseSellerVoList(null);
            jyhtxx.setGlImmovableVoList(null);
            //反转出合同信息
            Sj_Info_Jyhtxx sj_jyhtxx = JSON.parseObject(JSON.toJSONString(jyhtxx), Sj_Info_Jyhtxx.class);
            baseSetting(sj_jyhtxx,serviceCode,sjsq.getReceiptNumber());
            //不动产关联信息处理
            List<SJ_Bdc_Gl> sj_bdcgls = copyJSONBdcToSJBdc(JSON_glImmovableVoList,BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE,sj_jyhtxx,sjsq);
            //买房人处理
            List<SJ_Qlr_Gl> sj_mfrgls = copyJSONQlrToSJQlr(JSON_glHouseBuyerVoList,BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_GFZ);
            //卖方人处理
            List<SJ_Qlr_Gl> sj_sellergls = copyJSONQlrToSJQlr(JSON_glHouseSellerVoList,BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_SFZ);

            sj_jyhtxx.setGlImmovableVoList(sj_bdcgls);
            sj_jyhtxx.setGlHouseBuyerVoList(sj_mfrgls);
            sj_jyhtxx.setGlHouseSellerVoList(sj_sellergls);

            sj_jyhtxxs.add(sj_jyhtxx);
        }
        //应该只有一个合同
        if(sj_jyhtxxs!=null && sj_jyhtxxs.size()>1){
            log.error("交易合同数目异常");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_TRANSACTION_CONTRACT_COUNT_ERROR);
        }
        if(sj_jyhtxxs!=null && sj_jyhtxxs.size()==1) {
            sjsq.setTransactionContractInfo(sj_jyhtxxs.get(0));
        }
    }
    public static void dealDyhtxxInfos(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode){
        List<JSONDyhtxx> dyhtxxs = JSONArray.parseArray(JSON_serviceDataInfos,JSONDyhtxx.class);
        List<Sj_Info_Dyhtxx> sj_dyhtxxs = new ArrayList<Sj_Info_Dyhtxx>();
        for(JSONDyhtxx dyhtxx:dyhtxxs){
            String JSON_glImmovableVoList = dyhtxx.getGlImmovableVoList();
            String JSON_glMortgageHolderVoList = dyhtxx.getGlMortgageHolderVoList();//抵押权人
            String JSON_glMortgagorVoList = dyhtxx.getGlMortgagorVoList();//抵押人
            dyhtxx.setGlMortgagorVoList(null);
            dyhtxx.setGlMortgageHolderVoList(null);
            dyhtxx.setGlImmovableVoList(null);
            //反转出合同信息
            Sj_Info_Dyhtxx sj_dyhtxx = JSON.parseObject(JSON.toJSONString(dyhtxx), Sj_Info_Dyhtxx.class);
            baseSetting(sj_dyhtxx,serviceCode,sjsq.getReceiptNumber());
            //不动产关联信息处理
            List<SJ_Bdc_Gl> sj_bdcgls = copyJSONBdcToSJBdc(JSON_glImmovableVoList,BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE,sj_dyhtxx,sjsq);
            //抵押权人处理
            List<SJ_Qlr_Gl> sj_dyqrgls = copyJSONQlrToSJQlr(JSON_glMortgageHolderVoList,BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYQR);
            //抵押人处理
            List<SJ_Qlr_Gl> sj_dyrgls = copyJSONQlrToSJQlr(JSON_glMortgagorVoList,BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYR);

            sj_dyhtxx.setGlImmovableVoList(sj_bdcgls);
            sj_dyhtxx.setGlMortgageHolderVoList(sj_dyqrgls);
            sj_dyhtxx.setGlMortgagorVoList(sj_dyrgls);

            sj_dyhtxxs.add(sj_dyhtxx);
        }
        //应该只有一个合同
        if(sj_dyhtxxs!=null && sj_dyhtxxs.size()>1){
            log.error("抵押合同数目异常");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_MORTGAGE_CONTRACT_COUNT_ERROR);
        }
        if(sj_dyhtxxs!=null && sj_dyhtxxs.size()==1) {
            sjsq.setMortgageContractInfo(sj_dyhtxxs.get(0));
        }
    }

    public static void dealQsxxInfos(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode){
        List<JSONQsxx> qsxxs = JSONArray.parseArray(JSON_serviceDataInfos,JSONQsxx.class);
        List<Sj_Info_Qsxx> sj_qsxxs = new ArrayList<Sj_Info_Qsxx>();
        for(JSONQsxx qsxx:qsxxs){
            Sj_Info_Qsxx sj_qsxx = JSON.parseObject(JSON.toJSONString(qsxx), Sj_Info_Qsxx.class);
            baseSetting(sj_qsxx,serviceCode,sjsq.getReceiptNumber());
            sj_qsxxs.add(sj_qsxx);
        }
        //只有一条地税信息
//        if(sj_qsxxs!=null && sj_qsxxs.size()>1){
//            log.error("地税信息条数异常");
//            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_TAXATION_COUNT_ERROR);
//        }
        if(sj_qsxxs!=null){
            sjsq.setTaxInfoVoList(sj_qsxxs);
        }
    }
    public static void dealHandleResults(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode){
        List<JSONHandleResult> handleResults = JSONArray.parseArray(JSON_serviceDataInfos,JSONHandleResult.class);
        List<SJ_Info_Handle_Result> sj_handleResults = sjsq.getHandleResultVoList()==null?new ArrayList<SJ_Info_Handle_Result>():sjsq.getHandleResultVoList();
        for(JSONHandleResult handleResult:handleResults){
            SJ_Info_Handle_Result sj_handleResult = JSON.parseObject(JSON.toJSONString(handleResult), SJ_Info_Handle_Result.class);
            baseSetting(sj_handleResult,serviceCode,sjsq.getReceiptNumber());
            sj_handleResults.add(sj_handleResult);
        }
        sjsq.setHandleResultVoList(sj_handleResults);
    }

    //处理楼盘信息
    public static void dealBuildings(String JSON_serviceDataInfos, SJ_Sjsq sjsq, String serviceCode){
        List<JSONImmovable> json_bdcs = JSONArray.parseArray(JSON_serviceDataInfos,JSONImmovable.class);
        List<SJ_Info_Immovable> sj_bdcs = new ArrayList<SJ_Info_Immovable>();
        for(JSONImmovable json_bdc:json_bdcs){
            String JSON_glImmovableVoList = json_bdc.getGlImmovableVoList();
            json_bdc.setGlImmovableVoList(null);
            //反转出不动产信息服务数据
            SJ_Info_Immovable sj_bdc = JSON.parseObject(JSON.toJSONString(json_bdc), SJ_Info_Immovable.class);
            baseSetting(sj_bdc,serviceCode,sjsq.getReceiptNumber());
            //不动产关联信息处理
            List<SJ_Bdc_Gl> sj_bdcgls = copyJSONBdcToSJBdc(JSON_glImmovableVoList,BizOrBizExceptionConstant.IMMOVEABLE_BUILDING_SERVICE,sj_bdc,sjsq);
            sj_bdc.setGlImmovableVoList(sj_bdcgls);
            sj_bdcs.add(sj_bdc);
        }
        //应该只有一个合同
        if(sj_bdcs!=null && sj_bdcs.size()>1){
            log.error("不动产数目异常");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_COUNT_ERROR);
        }
        if(sj_bdcs!=null && sj_bdcs.size()==1) {
            sjsq.setImmovableSelf(sj_bdcs.get(0));
        }
    }

    private static SJ_Sjsq copyJSONReceiptDataToSjsq(JSONReceiptData sjsq_JSON_str){
        SJ_Sjsq sjsq = JSON.parseObject(JSON.toJSONString(sjsq_JSON_str),SJ_Sjsq.class);
//        System.out.println("1:"+JSONObject.toJSONString(sjsq));
        return sjsq;
    }

    //关联不动产信息反转
    private static List<SJ_Bdc_Gl> copyJSONBdcToSJBdc(String JSON_glImmovableVoList,String dataFrom,SJ_Information sj_info,SJ_Sjsq sjsq){
        List<SJ_Bdc_Gl> sj_bdcgls = new ArrayList<SJ_Bdc_Gl>();//证书中不动产关联集合对象
        if(StringUtils.isNotBlank(JSON_glImmovableVoList)){
            List<JSONGLBdc> bdcgls = JSONArray.parseArray(JSON_glImmovableVoList,JSONGLBdc.class);//证书中不动产关联集合JSON对象
            for(JSONGLBdc bdcgl:bdcgls){
                bdcgl.setInfoTableIdentification(dataFrom);
                SJ_Bdc_Fw_Info sj_bdc_fw_info = null;
                SJ_Bdc_Zd_Info sj_bdc_zd_info = null;
                if(StringUtils.isBlank(bdcgl.getImmovableType())){
                    if(StringUtils.isBlank(sjsq.getImmovableType())) {
                        log.error(sj_info.getInfoId() + "号证书/证明/合同关联的不动产的类型为空，已终止保存并记录错误信息");
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_TYPE_NOT_CLEAR);
                    }
                    bdcgl.setImmovableType(sjsq.getImmovableType());
                }
                if(bdcgl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_FD)){
                    if(StringUtils.isNotBlank(bdcgl.getZdInfo())){
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_TYPE_NOT_MATCH_ON_FD);
                    }
                    String fwInfo = bdcgl.getFwInfo();
                    if(StringUtils.isNotBlank(fwInfo)) {
                        sj_bdc_fw_info = JSON.parseObject(fwInfo, SJ_Bdc_Fw_Info.class);
                    }
                    bdcgl.setFwInfo(null);
                }else if(bdcgl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)){
                    if(StringUtils.isNotBlank(bdcgl.getFwInfo())){
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_TYPE_NOT_MATCH_ON_JD);
                    }
                    String zdInfo = bdcgl.getZdInfo();
                    if(StringUtils.isNotBlank(zdInfo)) {
                        sj_bdc_zd_info = JSON.parseObject(zdInfo, SJ_Bdc_Zd_Info.class);
                    }
                    bdcgl.setZdInfo(null);
                }else{
                    log.error(sj_info.getInfoId()+"号证书/证明/合同/building关联的不动产的类型未定义，传入为："+bdcgl.getImmovableType()+"，已终止保存并记录错误信息");
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_TYPE_NOT_CLEAR);
                }
                SJ_Bdc_Gl sj_bdc_gl = JSON.parseObject(JSON.toJSONString(bdcgl),SJ_Bdc_Gl.class);
                sj_bdc_gl.setFwInfo(sj_bdc_fw_info);
                sj_bdc_gl.setZdInfo(sj_bdc_zd_info);
                sj_bdcgls.add(sj_bdc_gl);
            }
        }

        return sj_bdcgls;
    }

    private static List<SJ_Qlr_Gl> copyJSONQlrToSJQlr(String JSON_glQlrVoList,String dataFrom,String qlrlx){
        List<SJ_Qlr_Gl> sj_qlrgls = new ArrayList<SJ_Qlr_Gl>();//证书中权利人关联集合对象
        if(StringUtils.isNotBlank(JSON_glQlrVoList)) {
            List<JSONGLQlr> qlrgls = JSONArray.parseArray(JSON_glQlrVoList, JSONGLQlr.class);
            for (JSONGLQlr qlrgl : qlrgls) {
                SJ_Qlr_Gl sj_qlr_gl = null;
                qlrgl.setInfoTableIdentification(dataFrom);
                String relatedPerson = qlrgl.getRelatedPerson();
                SJ_Qlr_Info sj_qlr_info = null;
                if (StringUtils.isNotBlank(relatedPerson)) {
                    sj_qlr_info = JSON.parseObject(relatedPerson, SJ_Qlr_Info.class);
                }
                qlrgl.setRelatedPerson(null);
                sj_qlr_gl = JSON.parseObject(JSON.toJSONString(qlrgl), SJ_Qlr_Gl.class);
                sj_qlr_gl.setObligeeType(qlrlx);
                sj_qlr_gl.setRelatedPerson(sj_qlr_info);
                sj_qlrgls.add(sj_qlr_gl);
            }
        }
        return sj_qlrgls;
    }

    //服务基础数据设置
    private static void baseSetting(SJ_Information sj_info,String serviceCode,String receiptNumber){
        //inserttime在这里将不做补充
//        if(sj_info.getInsertTime()==null) {
//            sj_info.setInsertTime(new Date());
//        }
        sj_info.setServiceCode(serviceCode);
        sj_info.setReceiptNumber(receiptNumber);
    }

    public static String checkServiceInfo(String serviceCode,String serviceDataTo){
        //输入异常
        if (serviceCode==null || serviceCode.length()<=0){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.GET_DATA_BUT_SVRCODE_IS_NULL);
        }
        String serviceDataTo_temp = "";
        //装载svrManagerMapper
        SvrManagerMapper svrManagerMapper = (SvrManagerMapper)SpringUtil.getApplicationContext().getBean("svrManagerMapper");
        if(svrManagerMapper!=null){
            serviceDataTo_temp = svrManagerMapper.selectServiceDataToByServiceCode(serviceCode);
        }else{
            //不合理的托管使用
        }
        if(StringUtils.isBlank(serviceDataTo)){
            serviceDataTo = serviceDataTo_temp;
        }
        if(StringUtils.isBlank(serviceDataTo)){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.GET_DATA_BUT_SVRDATATO_IS_NULL);
        }
        if(!serviceDataTo.equals(serviceDataTo_temp)){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.IMPUT_SVRDATATO_IS_NOT_MATCH);
        }
        return serviceDataTo;
    }

    public static SJ_Sjsq getRespSjsq(SJ_Sjsq sjsq){

        List<RespServiceData> serviceDatas = new ArrayList<RespServiceData>();

        List<SJ_Info_Bdcqlxgxx> qlxxs = sjsq.getImmovableRightInfoVoList();
        if(qlxxs!=null && qlxxs.size()>0){
            RespServiceData bdcqlServiceData = new RespServiceData();
            bdcqlServiceData.setServiceDataInfos(qlxxs);
            bdcqlServiceData.setServiceCode(qlxxs.get(0).getServiceCode());
            serviceDatas.add(bdcqlServiceData);
        }


        List<Sj_Info_Bdcdyxgxx> dyxxs = sjsq.getImmovableCurrentMortgageInfoVoList();
        if(dyxxs!=null && dyxxs.size()>0){
            RespServiceData bdcdyServiceData = new RespServiceData();
            bdcdyServiceData.setServiceCode(dyxxs.get(0).getServiceCode());
            bdcdyServiceData.setServiceDataInfos(dyxxs);
            serviceDatas.add(bdcdyServiceData);
        }

        Sj_Info_Dyhtxx mortgageContractInfo = sjsq.getMortgageContractInfo();
        if(mortgageContractInfo!=null){
            RespServiceData dyhtServiceData = new RespServiceData();
            List<Sj_Info_Dyhtxx> dyhts = new ArrayList<Sj_Info_Dyhtxx>();
            dyhtServiceData.setServiceCode(mortgageContractInfo.getServiceCode());
            dyhts.add(mortgageContractInfo);
            dyhtServiceData.setServiceDataInfos(dyhts);
            serviceDatas.add(dyhtServiceData);
        }

        Sj_Info_Jyhtxx transactionContractInfo = sjsq.getTransactionContractInfo();
        if(transactionContractInfo!=null){
            RespServiceData jyhtServiceData = new RespServiceData();
            List<Sj_Info_Jyhtxx> jyhts = new ArrayList<Sj_Info_Jyhtxx>();
            jyhtServiceData.setServiceCode(transactionContractInfo.getServiceCode());
            jyhts.add(transactionContractInfo);
            jyhtServiceData.setServiceDataInfos(jyhts);
            serviceDatas.add(jyhtServiceData);
        }

        List<Sj_Info_Qsxx> taxInfos = sjsq.getTaxInfoVoList();
        if(taxInfos!=null && taxInfos.size()>0){
            RespServiceData qsxxServiceData = new RespServiceData();
            qsxxServiceData.setServiceCode(taxInfos.get(0).getServiceCode());
            qsxxServiceData.setServiceDataInfos(taxInfos);
            serviceDatas.add(qsxxServiceData);
        }

        List<SJ_Info_Handle_Result> handleResultVoList = sjsq.getHandleResultVoList();
        if(handleResultVoList!=null && handleResultVoList.size()>0){
            RespServiceData handleResultServiceData = new RespServiceData();
            handleResultServiceData.setServiceDataInfos(handleResultVoList);
            handleResultServiceData.setServiceCode(handleResultVoList.get(0).getServiceCode());
            serviceDatas.add(handleResultServiceData);
        }
        sjsq.setImmovableRightInfoVoList(null);
        sjsq.setImmovableCurrentMortgageInfoVoList(null);
        sjsq.setTransactionContractInfo(null);
        sjsq.setMortgageContractInfo(null);
        sjsq.setTaxInfoVoList(null);
        sjsq.setHandleResultVoList(null);

        sjsq.setServiceDatas(serviceDatas);
        return sjsq;
    }


}
