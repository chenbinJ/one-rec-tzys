package com.ztgeo.general.biz.service_biz.chenbin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.itextpdf.text.DocumentException;
import com.ztgeo.general.biz.service_biz.chenbin.RecordManagerBiz;
import com.ztgeo.general.component.chenbin.OtherComponent;
import com.ztgeo.general.component.chenbin.PdfDealComponent;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record_Ext;
import com.ztgeo.general.entity.service_data.interface_response.interface_data.InterfaceData;
import com.ztgeo.general.entity.service_data.intface_param.PersonnelUnitReqEntity;
import com.ztgeo.general.entity.service_data.page.PageResponseBean;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;
import com.ztgeo.general.entity.service_data.sys_data.RecordSearchParams;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.chenbin.RecordHandleMapper;
import com.ztgeo.general.mapper.chenbin.SvrManagerMapper;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RecordManagerBizImpl implements RecordManagerBiz {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordHandleMapper recordHandleMapper;
    @Autowired
    private OtherComponent otherComponent;
    @Autowired
    private PdfDealComponent pdfDealComponent;
    @Autowired
    private SvrManagerMapper svrManagerMapper;

    @Override
    public PageResponseBean<Sj_Inquiry_Record> searchExceptionPage(RecordSearchParams params) {
        if(!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())){
            params.setExecutor(UserUtil.checkAndGetUser());
        }
        int pageNum = params.getPageNum();
        int pageSize = params.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        Page<Sj_Inquiry_Record> resp = recordHandleMapper.selectRecords(params);
        return new PageResponseBean<Sj_Inquiry_Record>(resp,resp.getPageSize(),resp.getPageNum(),resp.getTotal());
    }

    @Override
    public Map<String, Object> findRecordInfo(String recordId) {
        if(StringUtils.isBlank(recordId)){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECORD_SEARCH_PARAM_EXECEPTION);
        }
        Sj_Inquiry_Record record = recordHandleMapper.selectRecordById(recordId);
        if(record==null){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECORD_CAN_NOT_FIND);
        }
        if(StringUtils.isBlank(record.getServiceCode())){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECORD_RESULT_EXECEPTION+",记录的serviceCode(服务标识)为空");
        }
        List<Sj_Inquiry_Record_Ext> recordExts = recordHandleMapper.selectRecordExts(recordId);

        //是否按岗位-服务数据加载策略加载数据（如果service同意按岗位-服务加载策略来那就按策略加载，并要求抵押单位不得查看别家）
        SJ_Service service = svrManagerMapper.selectSvrByCode(record.getServiceCode());
        if(service!=null && StringUtils.isNotBlank(service.getPositionServiceShowSetting()) && service.getPositionServiceShowSetting().equals("1")) {
            record = otherComponent.takeCanUseRecord(record.getServiceCode(), record, recordExts);//处理记录信息
        }
        if(record.getRecordExts()==null){
            record.setRecordExts(recordExts);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("serviceData",record);
        return map;
    }

    @Override
    @Transactional
    public Object getRecordPDF(String recordId) {
        Sj_Inquiry_Record record = (Sj_Inquiry_Record)findRecordInfo(recordId).get("serviceData");
        String pdfPath = "";
        try {
            if (StringUtils.isBlank(record.getPdfPath())) {
                pdfPath = pdfDealComponent.initCommonQueryResultPdf(record);
            }
        } catch (IOException e){
            if(StringUtils.isNotBlank(record.getPdfPath())){
                pdfDealComponent.removeFile(record.getPdfPath());
            }
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PDF_EXCEPTION_WITH_IO);
        } catch (DocumentException e){
            if(StringUtils.isNotBlank(record.getPdfPath())){
                pdfDealComponent.removeFile(record.getPdfPath());
            }
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PDF_EXCEPTION_WITH_DOC);
        }
        try {
            if (StringUtils.isNotBlank(pdfPath)) {
                recordHandleMapper.updateRecordForPdf(recordId, pdfPath);
            }
        }catch (Exception e){
            if(StringUtils.isNotBlank(pdfPath)) {
                pdfDealComponent.removeFile(pdfPath);
            }
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PDF_EXCEPTION_WITH_OTHER);
        }
        return pdfPath;
    }

    @Override
    public Object downloadRecordPDF(String recordId) {
        Sj_Inquiry_Record record = (Sj_Inquiry_Record)findRecordInfo(recordId).get("serviceData");
        return null;
    }

    @Override
    public Map<String,Object> getPersonCheckResult(PersonnelUnitReqEntity personnelUnit) {
        System.out.println(JSONObject.toJSONString(personnelUnit));
        Map<String,Object> result = null;
        InterfaceData targetInterfaceData = null;
        List<InterfaceData> interfaceDataList = recordHandleMapper.selectInterfaceDatasWithOuterId(personnelUnit.getOuterId(),personnelUnit.getInterfaceCode());
        for(InterfaceData interfaceData:interfaceDataList){
            String reqParams = interfaceData.getReqParams();
//            String respBody = interfaceData.getRespBody();
            if(StringUtils.isNotBlank(reqParams)){
                PersonnelUnitReqEntity params = JSONObject.parseObject(reqParams,PersonnelUnitReqEntity.class);
//                JSONArray respArray = JSONArray.parseArray(respBody);
//                JSONObject resp = null;
//                if(respArray.size()>0){
//                    resp = respArray.getJSONObject(0);
//                }
                if(params.getMc().equals(personnelUnit.getMc()) && params.getZjhm().equals(personnelUnit.getZjhm())){
                    targetInterfaceData = interfaceData;
                    break;
                }
            }
        }
        if(targetInterfaceData!=null){
            result = new HashMap<String,Object>();
            result.put("interfaceCode",targetInterfaceData.getInterfaceCode());
            result.put("interfaceDatas", JSONArray.parseArray(targetInterfaceData.getRespBody()));
        }
        return result;
    }


}
