package com.ztgeo.general.util.chenbin;

import org.apache.commons.net.ftp.FTPClient;

public class FTPClientUtil {
    private static final ThreadLocal<FTPClient> contextHolder = new ThreadLocal<FTPClient>();
    public static void setFTPClient(FTPClient ftpClient) {
        if(ftpClient==null) {
            ftpClient = new FTPClient();
        }
        contextHolder.set(ftpClient);
    }

    public static FTPClient getFTPClient() {
        return ((FTPClient) contextHolder.get());
    }

    public static void clearFTPClient() {
        contextHolder.remove();
    }
}
