package com.ztgeo.general.biz.service_biz.chenbin.impl;

import com.alibaba.fastjson.JSONObject;
import com.ztgeo.general.biz.service_biz.chenbin.PrintSettingBiz;
import com.ztgeo.general.component.penghao.ActivitiComponent;
import com.ztgeo.general.component.penghao.SjFjtmComponent;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.extend.Fjtm_Power;
import com.ztgeo.general.entity.service_data.print.*;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.chenbin.PrintSettingMapper;
import com.ztgeo.general.mapper.chenbin.SJInfoManagerMapper;
import com.ztgeo.general.mapper.penghao.ActModelPrintMapper;
import com.ztgeo.general.util.chenbin.Base64Util;
import com.ztgeo.general.util.chenbin.QRCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("printBiz")
public class PrintSettingBizImpl implements PrintSettingBiz {
    @Value("${receipt.qrImgUrl}")
    private String qrImgUrl;
    @Autowired
    private SJInfoManagerMapper sJInfoManagerMapper;
    @Autowired
    private PrintSettingMapper printSettingMapper;
    @Autowired
    private ActModelPrintMapper actModelPrintMapper;
    @Autowired
    private ActivitiComponent activitiComponent;
    @Autowired
    private SjFjtmComponent sjFjtmComponent;

    @Override
    public PrintParam findPrintSettings(String receiptNumber) {
        PrintParam printParam = new PrintParam();
        PrintWorkBook printWorkBook = new PrintWorkBook();
        //获取收件单对应的模版路径
        List<String> processIds = printSettingMapper.selectModelIdByReceiptNumber(receiptNumber);
        if(processIds==null || processIds.size()<=0){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DO_NOT_HAVE_THIS_RECORDE);
        }
        String processId = processIds.get(0);
        String modelId = activitiComponent.getActivityByProcessId(processId);
        String printPath = actModelPrintMapper.selectPrintPathByModelId(modelId);

        //基础信息
        SJ_Sjsq sjsq = sJInfoManagerMapper.selectSjsqBySjbh(receiptNumber);
        //拼接相关证或相关人信息
        List<SJ_Info_Bdcqlxgxx> qlxxs = sJInfoManagerMapper.selectBdcqlxxsBySqbh(receiptNumber);
        List<Sj_Info_Bdcdyxgxx> dyxxs = sJInfoManagerMapper.selectBdcdyxxsBySqbh(receiptNumber);
        List<Sj_Info_Dyhtxx> dyhts = sJInfoManagerMapper.selectDyhtxxsBySqbh(receiptNumber);
        List<Sj_Info_Jyhtxx> jyhts = sJInfoManagerMapper.selectJyhtxxsBySqbh(receiptNumber);
        String zh = "";
        for(SJ_Info_Bdcqlxgxx qlxx:qlxxs){
            zh = zh + qlxx.getImmovableCertificateNo()+",";
        }
        if(zh.lastIndexOf(",")>0) {
            zh = zh.substring(0,zh.lastIndexOf(","));
        }

        String zmh = "";
        for(Sj_Info_Bdcdyxgxx dyxx:dyxxs){
            zmh = zmh + dyxx.getMortgageCertificateNo()+",";
        }
        if(zmh.lastIndexOf(",")>0) {
            zmh = zmh.substring(0,zmh.lastIndexOf(","));
        }

        String dyr = "";
        String dyqr = "";
        String qlr = "";
        String ywr = "";
        String gyfs = "";
        if(dyhts!=null && dyhts.size()>0){
            if(dyhts.size()==1) {
                Sj_Info_Dyhtxx dyht = dyhts.get(0);
                dyqr = getXGR(dyht.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYQR);
                dyr = getXGR(dyht.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYR);
            }
            if(dyhts.size()>1){
                throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_MORTGAGE_CONTRACT_COUNT_ERROR);
            }
        } else {
            for(Sj_Info_Bdcdyxgxx dyxx:dyxxs){
                dyr = getXGR(dyr,dyxx,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYR);
                dyqr = getXGR(dyqr,dyxx,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYQR);
            }
        }

        if(jyhts!=null && jyhts.size()>0){
            if(jyhts.size()==1){
                Sj_Info_Jyhtxx jyht = jyhts.get(0);
                qlr = getXGR(jyht.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_GFZ);
                ywr = getXGR(jyht.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_SFZ);
                gyfs = getGYFS(jyht.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_GFZ);
            }
            if(jyhts.size()>1){
                throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_TRANSACTION_CONTRACT_COUNT_ERROR);
            }
        } else {
            for(SJ_Info_Bdcqlxgxx qlxx:qlxxs){
                qlr = getXGR(qlr,qlxx,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR);
                ywr = getXGR(ywr,qlxx,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_YWR);
                gyfs = StringUtils.isNotBlank(gyfs)?gyfs:getGYFS(qlxx.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR);
            }
        }

        //附件清单
        String tgcl = "";
        List<Fjtm_Power> fileEntryVoList = (List<Fjtm_Power>)sjFjtmComponent.findFjtmByPositionOrModelId(modelId);
        for(Fjtm_Power fileEntry:fileEntryVoList){
            String entryId = fileEntry.getEntryId();
            String entryName = fileEntry.getEntryName();
            int count = sjFjtmComponent.getCountOfEntryFile(processId,entryId);
            if(count>0){
                entryName = entryName + "（"+count+"份）";
            }
            tgcl = tgcl+entryName+"\n";
        }

        //二维码
        String img = null;
        try {
            QRCodeUtil.encode(receiptNumber, null, qrImgUrl,receiptNumber, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.QR_INIT_EXCEPTION);
        }
        String path = qrImgUrl + receiptNumber + ".jpg";
        try {
            img = Base64Util.getBase64Img(path);
        } catch (IOException e){
            e.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.QR_INIT_EXCEPTION);
        }
        PrintContent<PrintEntity> printContent = new PrintContent<PrintEntity>();
        switch (printPath){
            case "test.xls":
                //PrintContent<PrintEntityMortgage> printContent = new PrintContent<PrintEntityMortgage>();
                PrintEntityMortgage printEntityMortgage = new PrintEntityMortgage();
                setBasePrintEntity(sjsq,printEntityMortgage,img,tgcl);
                printEntityMortgage.setXgzh(StringUtils.isBlank(zh)?zmh:zh);
                printEntityMortgage.setXgzh1(StringUtils.isBlank(zh)?zmh:zh);
                printEntityMortgage.setDyr(dyr);
                printEntityMortgage.setDyqr(dyqr);
                printEntityMortgage.setDyr1(dyr);
                printEntityMortgage.setSqr1(dyqr);
                printContent.setSingle(printEntityMortgage);
                printWorkBook.setContent(printContent);
                break;
            case "test1.xls":
                PrintEntityAdvance printEntityAdvance = new PrintEntityAdvance();
                setBasePrintEntity(sjsq,printEntityAdvance,img,tgcl);
                printEntityAdvance.setXgzh(StringUtils.isBlank(zh)?zmh:zh);
                printEntityAdvance.setXgzh1(StringUtils.isBlank(zh)?zmh:zh);
                printEntityAdvance.setQldyr(dyr);
                printEntityAdvance.setDyqr(dyqr);
                printEntityAdvance.setYwr(ywr);
                printEntityAdvance.setGyfs(gyfs);
                printEntityAdvance.setSqr1(dyqr);
                printEntityAdvance.setDyr1(dyr);
                printEntityAdvance.setYwr1(ywr);
                printEntityAdvance.setGyfs1(gyfs);
                printContent.setSingle(printEntityAdvance);
                printWorkBook.setContent(printContent);
                break;
            case "test2.xls":
                PrintEntityTransfer printEntityTransfer = new PrintEntityTransfer();
                setBasePrintEntity(sjsq,printEntityTransfer,img,tgcl);
                printEntityTransfer.setXgzh(StringUtils.isBlank(zh)?zmh:zh);
                printEntityTransfer.setXgzh1(StringUtils.isBlank(zh)?zmh:zh);
                printEntityTransfer.setQlr(qlr);
                printEntityTransfer.setYwr(ywr);
                printEntityTransfer.setSqr(qlr);
                printEntityTransfer.setYwr1(ywr);
                printContent.setSingle(printEntityTransfer);
                printWorkBook.setContent(printContent);
                break;
            default:

                break;
        }
        printParam.setTemplatePath(printPath);//存放路径
        printParam.setIsPrintPreview(true);//是否预览，是否配置为可配
        List<PrintWorkBook> workbook = new ArrayList<PrintWorkBook>();
        workbook.add(printWorkBook);
        Map<String,List<PrintWorkBook>> map = new HashMap<String,List<PrintWorkBook>>();
        map.put("workbook",workbook);
        printParam.setFillContent(JSONObject.toJSONString(map));//打印内容
        return printParam;
    }

    private void setBasePrintEntity(SJ_Sjsq sjsq, PrintEntity entity,String img,String tgcl){
        entity.setBh(sjsq.getReceiptNumber());
        entity.setBh1(sjsq.getReceiptNumber());
        entity.setSjr(sjsq.getReceiptMan());
        entity.setSjrq(sjsq.getReceiptTime());
        entity.setSjr1(sjsq.getReceiptMan());
        entity.setSjrq1(sjsq.getReceiptTime());

        entity.setJjr(sjsq.getNotifiedPersonName());
        entity.setJjr1(sjsq.getNotifiedPersonName());
        entity.setJjr2(sjsq.getNotifiedPersonName());
        entity.setJjr3(sjsq.getNotifiedPersonName());
        entity.setLxdh(sjsq.getNotifiedPersonTelephone());
        entity.setLxdh1(sjsq.getNotifiedPersonTelephone());
        entity.setYwlx(sjsq.getBusinessType());
        entity.setYwlx1(sjsq.getBusinessType());
        entity.setZl(sjsq.getImmovableSite());
        entity.setZl(sjsq.getImmovableSite());

        entity.setTgcl(tgcl);
        entity.setTgcl1(tgcl);
        entity.setImg(img);
        entity.setImg1(img);
    }

    private String getGYFS(String infoId,String xgrlx){
        String gyfs = "";
        List<SJ_Qlr_Gl> glrs = sJInfoManagerMapper.selectQlrGlByInfoId(infoId,xgrlx);
        if(glrs!=null && glrs.size()>0){
            gyfs = StringUtils.isNotBlank(glrs.get(0).getSharedMode())?glrs.get(0).getSharedMode():"";
        }
        return gyfs;
    }

    private String getXGR(String infoId,String xgrlx){
        String xgr = "";
        List<SJ_Qlr_Gl> glrs = sJInfoManagerMapper.selectQlrGlByInfoId(infoId,xgrlx);
        for (SJ_Qlr_Gl glr:glrs){
            xgr = xgr + glr.getObligeeName()+",";
        }
        if(xgr.lastIndexOf(",")>0){
            xgr = xgr.substring(0,xgr.lastIndexOf(","));
        }
        return xgr;
    }

    private String getXGR(String xgr,SJ_Information info,String xgrlx){
        String xgr_temp = getXGR(info.getInfoId(),xgrlx);
        if(StringUtils.isNotBlank(xgr_temp) && !xgr.contains(xgr_temp)){
            xgr = xgr + "," + xgr_temp;
        }
        return xgr;
    }
}
