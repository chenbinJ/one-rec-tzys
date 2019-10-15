package com.ztgeo.general.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/*
* 秘钥相关配置
* 该类在启动时被赋值  以保证每次加密秘钥不同，防止被伪造秘钥
* */
@Configuration
@Data
@ComponentScan
public class KeyConfiguration {
    @Value("${jwt.rsa-secret}")
    private String userSecret;
    private byte[] userPubKey;
    private byte[] userPriKey;
}
