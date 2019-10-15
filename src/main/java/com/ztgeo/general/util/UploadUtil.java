package com.ztgeo.general.util;

import com.ztgeo.general.exceptionmsg.FileManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class UploadUtil {

    /**
     * 文件上传
     * @param path 文件路径
     * @param file 文件
     * @return absolutePath(绝对路径)、fileName、relativelyPath(相对路径)
     */
    public static String[] upload(String path, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException(FileManager.FILE_IS_NO_CHOOSE);
        }
        String fileName = file.getOriginalFilename();//获取文件名称
        String[] pathArg = CommonUtils.getRealPath();
        String absolutePath = pathArg[0] + path;//文件绝对路径
        String relativelyPath = pathArg[1] + path;//相对路径
        new File(absolutePath).mkdirs();
        File dest = new File(absolutePath + fileName);
        try {
            file.transferTo(dest);
            return new String[]{absolutePath + fileName, fileName,relativelyPath + fileName};//文件保存路径
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }



}
