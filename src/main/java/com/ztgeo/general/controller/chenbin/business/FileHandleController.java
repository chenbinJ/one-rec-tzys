package com.ztgeo.general.controller.chenbin.business;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import com.ztgeo.general.service.chenbin.FileHandleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;

@Slf4j
@RestController
@RequestMapping("api/biz/fileHandleBusi")
@Api(tags = {"附件后续操作API-cb"})
public class FileHandleController {

    @Autowired
    private FileHandleService fileHandleService;

    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public ObjectRestResponse<SJ_Fjfile> uploadServiceFile(@Param("svrFile") MultipartFile file){
        ObjectRestResponse<SJ_Fjfile> rv = new ObjectRestResponse<SJ_Fjfile>();
//        MultipartFile file = multiRequest.getFile("svrFile");
        return rv.data(fileHandleService.uploadServiceFile(file));
    }

    @RequestMapping(value = "downloadFileById", method = RequestMethod.POST)
    public void downloadFileById(@RequestParam("fileId") String fileId, HttpServletRequest req,
                                   HttpServletResponse resp){
        try {
            SJ_Fjfile file = fileHandleService.getDownloadFileFromFtp(fileId);
            if(file!=null){
                String fileName = file.getFileName();
                if(fileName.lastIndexOf(".")>0) {
                    fileName = fileName.replaceAll(file.getFileName().substring(file.getFileName().lastIndexOf(".")), "");
                }
                //流传文件
                resp.setContentType("application/octet-stream");
                resp.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1")+"."+file.getFileExt() + "\"");
                OutputStream toClient = new BufferedOutputStream(resp.getOutputStream());
                toClient.write(file.getFileContent());
                toClient.flush();
                toClient.close();
            }
        } catch (Exception e) {
            log.error("下载模板失败");
        }
    }
}
