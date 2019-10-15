package com.ztgeo.general.component.penghao;

import com.ztgeo.general.util.DateUtils;
import com.ztgeo.general.util.chenbin.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class ToFTPUploadComponent {


    @Value("${webplus.ftpAddress}")
    private String ftpAddress;
    @Value("${webplus.ftpPort}")
    private String ftpPort;
    @Value("${webplus.ftpUsername}")
    private String ftpUsername;
    @Value("${webplus.ftpPassword}")
    private String ftpPassword;
    //链接
//    private static  FTPClient ftpClient = new FTPClient();
    private static String LOCAL_CHARSET = "GBK";

    public  String uploadFile(MultipartFile uploadFile){
        //链接
        FTPClient ftpClient = new FTPClient();
        boolean returnValue  = false;
        String fileName=null;
        String hz=null;
        //路径年/月/日/entryId名称
        String path=DateUtils.getNowYear()+File.separator+DateUtils.getNowMonth()+ File.separator+DateUtils.getNowDay();
        try {
            log.info("进入附件处理");
            int reply;
            log.info("连接ftp服务器");
            ftpClient.connect(ftpAddress, Integer.parseInt(ftpPort));// 连接FTP服务器
            log.info("登录ftp用户");
            ftpClient.login(ftpUsername, ftpPassword);// 登录
            /**
             * 确认应答状态码是否正确完成响应
             * 凡是 2开头的 isPositiveCompletion 都会返回 true，因为它底层判断是：
             * return (reply >= 200 && reply < 300);
             */
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return null;
            }
            //如果没有需求上传图片的话还ok，
            // 但是要是传图片，就需要设置一下文件类型为二进制
            // 这样上传的图片才不会报错（记得我的错误貌似是什么ASCII编码什么的。。）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            //创建目录
            mkDir(ftpClient,path);//创建目录
            ftpClient.changeWorkingDirectory("/" + path);//创建完了目录需要将当前工作目录切换过来，然后直接在下面创建文件
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }
            hz=uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".")+1);
            fileName=IDUtil.getFinstId()+"."+hz;
            FTPFile[] fs = ftpClient.listFiles(fileName);
            if (fs.length == 0) {
                System.out.println("this file not exist ftp");
            } else if (fs.length == 1) {
                System.out.println("this file exist ftp");
                ftpClient.deleteFile(fs[0].getName());
            }
            InputStream is = uploadFile.getInputStream();
            returnValue = ftpClient.storeFile(fileName, is);
            is.close();
            ftpClient.logout();
        } catch (IOException e) {
            returnValue=false;
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
                if (returnValue==false){
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (returnValue==true){
            return path+File.separator+fileName;
        }
        return null;
    }


    /**
     * 选择上传的目录，没有创建目录
     *
     * @param ftpPath
     *            需要上传、创建的目录
     * @return
     */
    public static boolean mkDir(FTPClient ftpClient,String ftpPath) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpPath.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpPath = sbStr.toString();
            // System.out.println("ftpPath:" + ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.makeDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.makeDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
