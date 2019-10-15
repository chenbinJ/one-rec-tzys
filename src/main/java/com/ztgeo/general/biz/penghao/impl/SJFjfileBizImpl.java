package com.ztgeo.general.biz.penghao.impl;

import com.ztgeo.general.biz.penghao.SJFjfileBiz;
import com.ztgeo.general.component.penghao.FromFTPDownloadComponent;
import com.ztgeo.general.component.penghao.ToFTPUploadComponent;
import com.ztgeo.general.component.pubComponent.ServiceAuthorizationComponent;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjinst;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.FileManager;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.penghao.SJ_FjfileMapper;
import com.ztgeo.general.mapper.penghao.SJ_FjinstMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Slf4j
@Service
public class SJFjfileBizImpl implements SJFjfileBiz {

    @Autowired
    private SJ_FjfileMapper sj_fjfileMapper;
    @Autowired
    private SJ_FjinstMapper sj_fjinstMapper;
    @Autowired
    private FromFTPDownloadComponent fromFTPDownloadComponent;
    @Autowired
    private ServiceAuthorizationComponent serviceAuthorizationComponent;
    @Autowired
    private ToFTPUploadComponent toFTPUploadComponent;


    @Override
    public byte[] selectByViewAttachment(String fileId, byte[] yte) {
        SJ_Fjfile sjFjfile = sj_fjfileMapper.selectByPrimaryKey(fileId);
        if (sjFjfile == null) {
            throw new ZtgeoBizException(MsgManager.FILE_ID_NULL);
        }
        String filename = sjFjfile.getFtpPath().substring(sjFjfile.getFtpPath().lastIndexOf("\\") + 1).trim();
        String path = sjFjfile.getFtpPath().substring(0, sjFjfile.getFtpPath().lastIndexOf("\\"));
        byte[] bytes = fromFTPDownloadComponent.downFile(path, filename, yte);
        return bytes;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT,
            rollbackFor = RuntimeException.class)
    public Object insertByAttachmentsAndEntryProcess(String fjinstEntryId, MultipartFile file, String finstId, String processId) {
        boolean flag = serviceAuthorizationComponent.checkPositionByFjtm(fjinstEntryId);
        if (flag == false) {
            throw new ZtgeoBizException(MsgManager.AUTHON_FJTM_LEVEL);
        }
        Integer number = sj_fjinstMapper.selectMaxByPid(finstId);//查询最大数
        if (number == null) {
            number = 0;
        }
        if (file == null) {
            throw new ZtgeoBizException(MsgManager.FILE_NULL);
        }
        SJ_Fjinst sjFjinst = sj_fjinstMapper.selectByPrimaryKey(finstId);
        if (sjFjinst == null) {
            throw new ZtgeoBizException(MsgManager.FINST_ID_NULL);
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (getImageType(suffix) == false) {
            throw new ZtgeoBizException(MsgManager.IMAGE_TYPE);
        }
        //新增附件条目表
        SJ_Fjinst fjinst = new SJ_Fjinst();
        fjinst.setCid(IDUtil.getFinstId());
        if (sj_fjinstMapper.selectCountNameByPid(file.getOriginalFilename(), finstId, fjinst.getCid()) > 0) {
            return MsgManager.SHFINST_CNAME_EXISTISE;
        }
        fjinst.setPid(finstId);
        fjinst.setCname(file.getOriginalFilename());
        fjinst.setPnode(processId);
        fjinst.setEntryId(sjFjinst.getEntryId());
        fjinst.setCreateTime(new Date());
        number++;
        fjinst.setOrderNumber(number);
        fjinst.setCreateBy(UserUtil.checkAndGetUser());
        fjinst.setCtype(sjFjinst.getCtype());
        fjinst.setPnodeType(FileManager.SJFJINST_PROCESS);
        fjinst.setCkind(FileManager.SJFJINST_WENJIAN);
        fjinst.setFileId(IDUtil.getFinstId());
        //先新增fjinst表
        if (sj_fjinstMapper.insert(fjinst) < 1) {
            throw new ZtgeoBizException(MsgManager.ENTRY_INSERT_FILE_TIAOMU_BAD);
        }
        //获取file对象
        SJ_Fjfile sj_fjfile = new SJ_Fjfile();
        sj_fjfile.setFileId(fjinst.getFileId());
        sj_fjfile.setFileExt(file.getOriginalFilename());
        //取后缀
        String hz = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        sj_fjfile.setFileExt(hz);
        sj_fjfile.setFileName(file.getOriginalFilename());
        sj_fjfile.setContentType(FileManager.FILE_TP);
        sj_fjfile.setFileSize(Long.toString(file.getSize()));
        sj_fjfile.setFileSubmissionTime(new Date());
        String returnValue = toFTPUploadComponent.uploadFile(file);
        if (StringUtils.isEmpty(returnValue)) {
            throw new ZtgeoBizException(MsgManager.FILE_BAD);
        } else {
            sj_fjfile.setFtpPath(returnValue);
        }
        if (sj_fjfileMapper.insert(sj_fjfile) < 1) {
            throw new ZtgeoBizException(MsgManager.ENTRY_INSERT_FILE_BAD);
        }
        return sj_fjfile;
    }

    @Override
    public Object selectByPrimaryKey(String fileId) {
        return sj_fjfileMapper.selectByPrimaryKey(fileId);
    }

    public boolean getImageType(String suffix) {
        if (!(MsgManager.IMG_TYPE_DMG.equals(suffix.toUpperCase()) ||
                MsgManager.IMG_TYPE_GIF.equals(suffix.toUpperCase()) ||
                MsgManager.IMG_TYPE_JPEG.equals(suffix.toUpperCase()) ||
                MsgManager.IMG_TYPE_JPG.equals(suffix.toUpperCase()) ||
                MsgManager.IMG_TYPE_PNG.equals(suffix.toUpperCase()) ||
                MsgManager.IMG_TYPE_SVG.equals(suffix.toUpperCase()))) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object insertSjFjinst(String fjinstEntryId, String cid, String processId, String cname) {
        boolean flag = serviceAuthorizationComponent.checkPositionByFjtm(fjinstEntryId);
        if (flag == false) {
            throw new ZtgeoBizException(MsgManager.AUTHON_FJTM_LEVEL);
        }
        if (StringUtils.isEmpty(cname)) {
            throw new ZtgeoBizException(MsgManager.FJINST_CNAME_NULL);
        }
        Integer number = sj_fjinstMapper.selectMaxByPid(cid);//查询最大数
        if (number == null) {
            number = 0;
        }
        SJ_Fjinst sjFjinst = sj_fjinstMapper.selectByPrimaryKey(cid);
        SJ_Fjinst fjinst = new SJ_Fjinst();
        fjinst.setCid(IDUtil.getFinstId());
        if (sj_fjinstMapper.selectCountNameByPid(sjFjinst.getCname(), sjFjinst.getCid(), fjinst.getCid()) > 0) {
            throw new ZtgeoBizException(MsgManager.SHFINST_CNAME_EXISTISE);
        }
        number++;
        fjinst.setPid(cid);
        fjinst.setCname(cname);
        fjinst.setPnode(processId);
        fjinst.setEntryId(sjFjinst.getEntryId());
        fjinst.setCtype(MsgManager.BIXUAN);
        fjinst.setCreateTime(new Date());
        fjinst.setOrderNumber(number);
        fjinst.setCreateBy(UserUtil.checkAndGetUser());
        fjinst.setPnodeType(FileManager.SJFJINST_PROCESS);
        fjinst.setCkind(FileManager.SJFJINST_FILE);
        if (sj_fjinstMapper.insert(fjinst) < 1) {
            throw new ZtgeoBizException(MsgManager.ENTRY_DELETE_TIAOMU_FILE_BAD);
        }
        return fjinst;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT,
            rollbackFor = RuntimeException.class)
    public String deleteEntryOrAttachmentsAndEntryProcess(String fjinstEntryId, String cid, String fileId) {
        boolean flag = serviceAuthorizationComponent.checkPositionByFjtm(fjinstEntryId);
        if (flag == false) {
            throw new ZtgeoBizException(MsgManager.AUTHON_FJTM_LEVEL);
        }
        if (sj_fjinstMapper.selectByPid(cid) > 0) {
            throw new ZtgeoBizException(MsgManager.SJFINST_PID_EXISTENCE);
        }
        if (StringUtils.isEmpty(fileId)) {
            if (sj_fjinstMapper.deleteByPrimaryKey(cid) < 1) {
                throw new ZtgeoBizException(MsgManager.ENTRY_DELETE_TIAOMU_FILE_BAD);
            }
            return MsgManager.OPERATING_SUCCESS;
        }
        SJ_Fjfile sjFjfile = sj_fjfileMapper.selectByPrimaryKey(fileId);
        if (sjFjfile == null) {
            throw new ZtgeoBizException(MsgManager.FINST_ID_NULL);
        }
        if (StringUtils.isEmpty(sjFjfile.getFtpPath())) {
            throw new ZtgeoBizException(MsgManager.FTP_TUPIAN_BAD);
        }
        String filename = sjFjfile.getFtpPath().substring(sjFjfile.getFtpPath().lastIndexOf("\\") + 1).trim();
        String path = sjFjfile.getFtpPath().substring(0, sjFjfile.getFtpPath().lastIndexOf("\\"));
        //删除附件表
        if (sj_fjfileMapper.deleteByPrimaryKey(fileId) < 1) {
            throw new ZtgeoBizException(MsgManager.ENTRY_DELETE_FILE_BAD);
        }
        //删除条目附件表
        if (sj_fjinstMapper.deleteByPrimaryKey(cid) < 1) {
            throw new ZtgeoBizException(MsgManager.ENTRY_DELETE_TIAOMU_FILE_BAD);
        }
        //删除ftp服务器图片
        fromFTPDownloadComponent.deleteFile(path, filename);
        return MsgManager.OPERATING_SUCCESS;
    }
}
