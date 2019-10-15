package com.ztgeo.general.util;

import com.ztgeo.general.exception.chenbin.ZtgeoBizException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DownloadUtil {

    /**
     * 文件下载工具类
     * @param filePath
     * @param fileName
     * @param response
     */
    public static void downLoad(String filePath, String fileName,String contentType, HttpServletResponse response){
        try {
            File file = new File(filePath);
            if(file.exists()){ //判断文件父目录是否存在
                 buildResponseEntity(response, file,contentType);
            } else{
                throw new ZtgeoBizException("文件不存在!"+filePath + File.separator + fileName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 构建下载类
     * @param response
     * @param file
     * @param contentType word文档:application/msword 文本:text/plain; charset=utf-8 xml数据:application/xml html:text/html; charset=utf-8
     * @return
     * @throws IOException
     */
    public static void buildResponseEntity(HttpServletResponse response, File file, String contentType) throws RuntimeException {
        try{
            response.setContentType(contentType);
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));  //转码之后下载的文件不会出现中文乱码
            response.addHeader("Content-Length", "" + file.length());
            //以流的形式下载文件
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 以流的形式下载文件
     *
     * @param file
     * @param response
     * @return
     */
    public static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
        try {
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            //如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                File f = new File(file.getPath());
//                f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     *
     * @param inputFile
     * @param ouputStream
     */
    public static void zipFile(File inputFile, ZipOutputStream ouputStream) {
        try {
            if (inputFile.exists()) {
                /**如果是目录的话这里是不采取操作的，至于目录的打包正在研究中**/
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 1024);
                    //org.apache.tools.zip.ZipEntry
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[1024];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


