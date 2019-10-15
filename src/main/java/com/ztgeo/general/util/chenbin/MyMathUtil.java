package com.ztgeo.general.util.chenbin;

public class MyMathUtil {
    public static long getCeil(long total,int limit){
        long result = 0;
        // 判断式：整除法
        if ((total % limit) == 0) {
            result = total / limit;                    // 保持double型数据类型
        } else {
            result = (total / limit) + 1;             // 保持double型数据类型
        }
        return result;
    }
}
