package com.ztgeo.general.component.penghao;

import com.alibaba.fastjson.JSONObject;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
public class FromFTPDownloadComponent {
    @Value("${webplus.ftpAddress}")
    private String ftpAddress;
    @Value("${webplus.ftpPort}")
    private String ftpPort;
    @Value("${webplus.ftpUsername}")
    private String ftpUsername;
    @Value("${webplus.ftpPassword}")
    private String ftpPassword;
    //链接
    private static FTPClient ftpClient = new FTPClient();

    /**
     * 下载zip文件
     * @param zipName  名称
     * @param sj_fjfileList ftp路径及名称
     * @param response
     */
    public void downloadPictureZip(String zipName, List<SJ_Fjfile> sj_fjfileList, HttpServletResponse response) {
        ZipOutputStream zipos = null;
        DataOutputStream os = null;
        InputStream is = null;
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            for (SJ_Fjfile fjfile : sj_fjfileList) {
                initFtpClient();
                if (null != fjfile) {
                    if (null != fjfile.getFtpPath() && null != fjfile.getFileName()) {
                        String localFilePath = fjfile.getFtpPath().substring(0, fjfile.getFtpPath().lastIndexOf("\\"));//截取路径
                        String fileName = fjfile.getFtpPath().substring(fjfile.getFtpPath().lastIndexOf("\\") + 1);//截取名字
                        ftpClient.changeWorkingDirectory(localFilePath);//切换到目录
                        ftpClient.enterLocalPassiveMode();//主动模式
                        zipos.setMethod(ZipOutputStream.DEFLATED);// 设置压缩方法DEFLATED
                        FTPFile[] files = ftpClient.listFiles();
                        for (FTPFile file : files) {
                            if (file.getName().equals(fileName)) {  //在ftp列表判断
//                            File tpFile = new File(fileName);
                                // 添加ZipEntry，并ZipEntry中写入文件流
                                zipos.putNextEntry(new ZipEntry(fileName));
                                os = new DataOutputStream(zipos);
                                is = ftpClient.retrieveFileStream(fileName);//输出
                                byte[] b = new byte[100];
                                int length = 0;
                                while ((length = is.read(b)) != -1) {
                                    os.write(b, 0, length);
                                }
                                is.close();
                                zipos.closeEntry();
                                ftpClient.disconnect();
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                zipos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public List<byte[]> fetch(String localFilePath, String fileName, List<File> fileList, List<byte[]> byteList) throws IOException {
        initFtpClient();
        try {
            boolean flag = ftpClient.changeWorkingDirectory(localFilePath);
            System.out.println(flag);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.getName().equals(fileName)) {
                    InputStream inputStream = ftpClient.retrieveFileStream(file.getName());
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    //下边两行代码代码只管重要，不然会报空，返回的字节数组，
                    // 调用后再转换成InputStream;inputStream = new ByteArrayInputStream(bytes);
                    inputStream.close();
                    byteList.add(bytes);
//                    ftpClient.completePendingCommand();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {

                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteList;
    }


    public byte[] downFile(String remotePath, String fileName, byte[] bytes) {
        FTPClient ftpClient = new FTPClient();
        try {
            int reply;
            ftpClient.connect(ftpAddress, Integer.parseInt(ftpPort));
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftpClient.login(ftpUsername, ftpPassword);// 登录
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return bytes;
            }
            System.out.println(remotePath + "   " + fileName);
            String innerPath = remotePath.replace("\\", "/");
            String[] path = innerPath.split("/");
            //设置主动模式，防止在Linux上，由于安全限制，可能某些端口没有开启，出现阻塞
            ftpClient.enterLocalPassiveMode();


            ftpClient.makeDirectory(path[0]);
            boolean fl = ftpClient.changeWorkingDirectory(path[0]);
            log.info("进入上层目录");
            ftpClient.makeDirectory(path[1]);
            boolean f2 = ftpClient.changeWorkingDirectory(path[1]);
            log.info("进入二级目录");
            ftpClient.makeDirectory(path[2]);
            boolean f3 = ftpClient.changeWorkingDirectory(path[2]);
            //下载指定文件
            InputStream is = ftpClient.retrieveFileStream(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            System.out.println(is);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(is, out);
            bytes = out.toByteArray();
            int length = bytes.length;
            System.out.println(length);
            out.close();
            is.close();
            System.out.println("流操作完毕");
            ftpClient.logout();
            System.out.println(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return bytes;
    }


    /**
     * 删除文件 *
     *
     * @param pathname FTP服务器保存目录 *
     * @param filename 要删除的文件名称 *
     * @return
     */
    public boolean deleteFile(String pathname, String filename) {
        boolean flag = false;
        try {
            System.out.println("开始删除文件");
            initFtpClient();
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.dele(filename);
            ftpClient.logout();
            flag = true;
            System.out.println("删除文件成功");
        } catch (Exception e) {
            System.out.println("删除文件失败");
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }


    /**
     * 初始化ftp服务器
     */
    public void initFtpClient() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            System.out.println("connecting...ftp服务器:" + this.ftpAddress + ":" + this.ftpPort);
            ftpClient.connect(ftpAddress, Integer.parseInt(ftpPort)); //连接ftp服务器
            ftpClient.login(ftpUsername, ftpPassword); //登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("connect failed...ftp服务器:" + this.ftpUsername + ":" + this.ftpPort);
            }
            System.out.println("connect successfu...ftp服务器:" + this.ftpUsername + ":" + this.ftpPort);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean downFile(String remotePath, String fileName, SJ_Fjfile fj) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(ftpAddress, Integer.parseInt(ftpPort));
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(ftpUsername, ftpPassword);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            System.out.println(remotePath + "   " + fileName);
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            System.out.println("path:" + remotePath + ",name:" + fileName + ",fj:" + JSONObject.toJSONString(fj));
            //下载指定文件
            InputStream is = ftp.retrieveFileStream(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(is, out);
            byte[] bs = out.toByteArray();
            int length = bs.length;
            System.out.println("附件字节长度：" + length);
            fj.setFileSize(Integer.toString(length));
            fj.setFileContent(bs);
            out.close();
            is.close();
            System.out.println("流操作完毕");
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }
}
