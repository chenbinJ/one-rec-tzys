package com.ztgeo.general.service.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileHandleService {
    SJ_Fjfile uploadServiceFile(MultipartFile file);
    SJ_Fjfile getDownloadFileFromFtp(String fileId);
}
