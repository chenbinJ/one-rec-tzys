package com.ztgeo.general.util.chenbin;

import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Base64Util {
    public static String getBase64Img(String path) throws IOException {
        String base64 = "";
        File file = new File(path);
        if(file.exists()&&file.isFile()){
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[(int)file.length()];
            int length = inputStream.read(data);
            inputStream.close();
            BASE64Encoder encoder = new BASE64Encoder();
            if(data!=null){
                base64 = encoder.encode(data);
            }else{
                throw new ZtgeoBizException(BizOrBizExceptionConstant.QR_INIT_EXCEPTION);
            }
        }else{
            throw new ZtgeoBizException(BizOrBizExceptionConstant.QR_INIT_EXCEPTION);
        }
        return base64;
    }
}
