package com.ztgeo.general.service.chenbin.impl;

import com.ztgeo.general.component.penghao.FromFTPDownloadComponent;
import com.ztgeo.general.component.penghao.ToFTPUploadComponent;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import com.ztgeo.general.mapper.penghao.SJ_FjfileMapper;
import com.ztgeo.general.service.chenbin.FileHandleService;
import com.ztgeo.general.util.chenbin.MyFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;

@Service
public class FileHandleServiceImpl implements FileHandleService {

    @Autowired
    private ToFTPUploadComponent toFTPUploadComponent;
    @Autowired
    private FromFTPDownloadComponent fromFTPDownloadComponent;
    @Autowired
    private SJ_FjfileMapper sj_fjfileMapper;

    @Override
    @Transactional
    public SJ_Fjfile uploadServiceFile(MultipartFile file) {
        String ftpPath = toFTPUploadComponent.uploadFile(file);
        SJ_Fjfile sj_fjfile = MyFileUtil.InitSJFile(file);
        sj_fjfile.setFtpPath(ftpPath);
        sj_fjfileMapper.insertSelective(sj_fjfile);
        return sj_fjfile;
    }

    @Override
    public SJ_Fjfile getDownloadFileFromFtp(String fileId) {
        SJ_Fjfile file = sj_fjfileMapper.selectByPrimaryKey(fileId);
        if (fromFTPDownloadComponent.downFile(
                file.getFtpPath().substring(0, file.getFtpPath().lastIndexOf("\\")),
                file.getFtpPath().substring(file.getFtpPath().lastIndexOf("\\") + 1),
                file
        )) {
            return file;
        }
        return null;
    }


}
