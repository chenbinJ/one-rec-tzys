package com.ztgeo.general.runner;

import com.alibaba.fastjson.JSON;
import com.github.ag.core.util.RsaKeyHelper;
import com.github.wxiaoqi.security.common.constant.RedisKeyConstants;
import com.ztgeo.general.config.KeyConfiguration;
import com.ztgeo.general.util.jwt.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/*
* 项目启动时运行，用于初始化user priKey和pubKey
* */
@Configuration
@Slf4j
@Component
public class AuthServerRunner implements CommandLineRunner {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_USER_PRI_KEY = "AG:AUTH:JWT:PRI";
    private static final String REDIS_USER_PUB_KEY = "AG:AUTH:JWT:PUB";

    @Autowired
    private KeyConfiguration keyConfiguration;

    @Autowired
    private AESUtil aecUtil;

    @Autowired
    private RsaKeyHelper rsaKeyHelper;

    @Override
    public void run(String... args) throws Exception {
        boolean flag = false;
        if (redisTemplate.hasKey(REDIS_USER_PRI_KEY)&&redisTemplate.hasKey(REDIS_USER_PUB_KEY)) {
            try {
                keyConfiguration.setUserPriKey(rsaKeyHelper.toBytes(aecUtil.decrypt(redisTemplate.opsForValue().get(REDIS_USER_PRI_KEY).toString())));
                keyConfiguration.setUserPubKey(rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(REDIS_USER_PUB_KEY).toString()));
            }catch (Exception e){
                log.error("初始化公钥/密钥异常...",e);
                flag = true;
            }
        } else {
            flag = true;
        }
        if(flag){
            Map<String, byte[]> keyMap = rsaKeyHelper.generateKey(keyConfiguration.getUserSecret());
            keyConfiguration.setUserPriKey(keyMap.get("pri"));
            keyConfiguration.setUserPubKey(keyMap.get("pub"));
            redisTemplate.opsForValue().set(REDIS_USER_PRI_KEY, aecUtil.encrypt(rsaKeyHelper.toHexString(keyMap.get("pri"))));
            redisTemplate.opsForValue().set(REDIS_USER_PUB_KEY, rsaKeyHelper.toHexString(keyMap.get("pub")));
        }
        log.info("完成公钥/密钥的初始化...");
    }

}
