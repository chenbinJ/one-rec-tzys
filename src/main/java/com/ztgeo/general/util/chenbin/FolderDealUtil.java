package com.ztgeo.general.util.chenbin;

import java.util.Date;

public class FolderDealUtil {

    public static String getPDFFileFolder(String basePath){
        return basePath + "pdfFile/"+TimeUtil.getDateString(new Date()).replaceAll("-","");
    }
}
