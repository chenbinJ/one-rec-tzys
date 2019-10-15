package com.ztgeo.general.util.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

@Slf4j
public class MyFileUtil {
    public static byte[] getImgByURL(String path) throws IOException {
        File file = new File(path);
        if(file.exists()&&file.isFile()){
            byte[] data = getByteArrayByFile(file,(int)file.length());
            return data;
        }else{
            return null;
        }
    }

    public static boolean deleteByUrl(String path) throws IOException{
        File file = new File(path);
        if(file.exists()&&file.isFile()){
            file.delete();
        }
        return true;
    }

    public static byte[] getByteArrayByFile(File file,int fileSize) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[fileSize];
        int length = inputStream.read(data);
        inputStream.close();
        return data;
    }

    public static SJ_Fjfile InitSJFile(MultipartFile file){
        String fileId = IDUtil.getFileId();
        String fileName = file.getOriginalFilename();
        //扩展名截取和附件名称
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        fileName = fileName.substring(0,fileName.lastIndexOf("."));
        long fileSize = file.getSize();

        SJ_Fjfile sj_fjfile = new SJ_Fjfile();
        sj_fjfile.setFileId(fileId);
        sj_fjfile.setFileName(fileName);
        sj_fjfile.setFileExt(prefix);
        sj_fjfile.setFileSize(""+fileSize);
        sj_fjfile.setFileSubmissionTime(new Date());
        return sj_fjfile;
    }

    /**
     * 下载样表
     * @param newName  下载的展示文件名
     * @return 响应
     */
    public static ResponseEntity<InputStreamResource> download(byte[] bytes, String newName, String kzm) {
        ResponseEntity<InputStreamResource> response = null;
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition",
                    "attachment; filename="
                            + new String(newName.getBytes("gbk"), "utf-8") + kzm);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            response = ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            log.error("获取不到文件流", e);
        }
        return response;
    }
}
