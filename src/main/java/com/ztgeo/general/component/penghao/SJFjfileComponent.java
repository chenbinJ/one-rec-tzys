package com.ztgeo.general.component.penghao;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjinst;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.penghao.SJ_FjfileMapper;
import com.ztgeo.general.util.DateUtils;
import com.ztgeo.general.util.FtpDownloadUtil;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@Component
public class SJFjfileComponent {

    private List<File> files = new ArrayList<>();

    @Autowired
    private SjFjtmComponent sjFjtmComponent;
    @Autowired
    private SJ_FjfileMapper sj_fjfileMapper;
    @Autowired
    private FromFTPDownloadComponent fromFTPDownloadComponent;


    /**
     * 权限下载全部附件
     * @param processId
     * @return
     */
    public void downloadAllAttachments(String processId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> map=(Map<String, Object>) sjFjtmComponent.findFjtmByPosition(processId);//判断权限
        List<SJ_Fjinst> sjFjinstList= (List<SJ_Fjinst>) map.get("fileInstanceVoList");
        List<SJ_Fjfile> sj_fjfileList=new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        List<byte[]> list = new ArrayList<>();
        String zipName= DateUtils.getCurrentTimestamp()+".zip";
        if (null!=sjFjinstList && sjFjinstList.size()!=0) {
             sj_fjfileList = processingFjinst(sjFjinstList,sj_fjfileList);
        }
//        for (SJ_Fjfile fjfile:sj_fjfileList) {
//            String localFilePath=fjfile.getFtpPath().substring(0,fjfile.getFtpPath().lastIndexOf("\\"));
//            String fileName=fjfile.getFtpPath().substring(fjfile.getFtpPath().lastIndexOf("\\")+1);
            fromFTPDownloadComponent.downloadPictureZip(zipName,sj_fjfileList,response);
//        }
//            FtpDownloadUtil.downLoad(list,response);

    }

    private List<SJ_Fjfile> processingFjinst(List<SJ_Fjinst> sjFjinstList,List<SJ_Fjfile> sj_fjfileList){
        for (SJ_Fjinst fjinst:sjFjinstList) {
            if (null != fjinst.getChildren()) {
                sj_fjfileList =Clshu(fjinst,sj_fjfileList);
            }
        }
        return sj_fjfileList;
    }


    private List<SJ_Fjfile> Clshu(SJ_Fjinst fjinst,List<SJ_Fjfile> sj_fjfileList){
        if (StringUtils.isNotEmpty(fjinst.getFileId()) && fjinst.getCkind().equals(MsgManager.FILE)) {
            SJ_Fjfile sj_fjfile = sj_fjfileMapper.selectByPrimaryKey(fjinst.getFileId());
            sj_fjfileList.add(sj_fjfile);
        }
        if (null != fjinst.getChildren()){
           sj_fjfileList =  processingFjinst(fjinst.getChildren(),sj_fjfileList);
        }
        return sj_fjfileList;
    }




}
