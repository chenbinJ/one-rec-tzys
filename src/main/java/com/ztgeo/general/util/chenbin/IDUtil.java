package com.ztgeo.general.util.chenbin;

import java.util.Date;
import java.util.UUID;

public class IDUtil {
    public static String getSvrId(){
        String date = TimeUtil.getDateString(new Date());
        return "SERVE-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }
    public static String getPowerId(){
        String date = TimeUtil.getDateString(new Date());
        return "POWER-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }
    public static String getIntfId(){
        String date = TimeUtil.getDateString(new Date());
        return "INTEF-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }
    public static String getIntfParamId(){
        String date = TimeUtil.getDateString(new Date());
        return "PARAM-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }
    public static String getStepId(){
        String date = TimeUtil.getDateString(new Date());
        return "STEP-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }
    public static String getStepGlId(){
        String date = TimeUtil.getDateString(new Date());
        return "RELAT-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }
    public static String getEntryId(){
        String date = TimeUtil.getDateString(new Date());
        return "ENTRY-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getTemplateId(){
        String date = TimeUtil.getDateString(new Date());
        return "TEMPLATE-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getFinstId(){
        String date = TimeUtil.getDateString(new Date());
        return "FINST-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getFileId(){
        String date = TimeUtil.getDateString(new Date());
        return "FILE-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getSysId(){
        String date = TimeUtil.getDateString(new Date());
        return "SYS-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getMdpId(){
        String date = TimeUtil.getDateString(new Date());
        return "MDP-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getInfoId(){
        String date = TimeUtil.getDateString(new Date());
        return "INFO-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getProce(){
        String date = TimeUtil.getDateString(new Date());
        return "PROCE-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getReceiptNumber(int number){
        String date = TimeUtil.getDateString(new Date());
        return "YCSL-" + date.replaceAll("-","")+String.format("%04d", number);
    }

    public static String getHandleId(){
        String date = TimeUtil.getDateString(new Date());
        return "Handle-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getImmovableId(){
        String date = TimeUtil.getDateString(new Date());
        return "IMMOVE-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getObligeeId(){
        String date = TimeUtil.getDateString(new Date());
        return "OBLIG-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getExceptionId(){
        String date = TimeUtil.getDateString(new Date());
        return "EXT-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getCommonId(){
        String date = TimeUtil.getDateString(new Date());
        return "YCSL-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getRecordId(){
        String date = TimeUtil.getDateString(new Date());
        return "RECORD-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getExtId(){
        String date = TimeUtil.getDateString(new Date());
        return "EXT-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }

    public static String getIntfDataId(){
        String date = TimeUtil.getDateString(new Date());
        return "INDATA-"+date.replaceAll("-","")+"-"+UUID.randomUUID().toString().substring(0,12).replaceAll("-","").toUpperCase();
    }
}
