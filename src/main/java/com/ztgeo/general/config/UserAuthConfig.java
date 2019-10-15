package com.ztgeo.general.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

public class UserAuthConfig {

    @Value("${auth.user.token-header}")
    private String tokenHeader;
    @Value("${auth.user.limit-expire}")
    private Integer tokenLimitExpire;

    @Autowired
    private KeyConfiguration keyConfiguration;

    private byte[] pubKeyByte;

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public String getToken(HttpServletRequest request){
        return request.getHeader(this.getTokenHeader());
    }

    public byte[] getPubKeyByte() {
        return keyConfiguration.getUserPubKey();
    }

    public void setPubKeyByte(byte[] pubKeyByte) {
        this.pubKeyByte = pubKeyByte;
    }

    public Integer getTokenLimitExpire() {
        return tokenLimitExpire;
    }

    public void setTokenLimitExpire(Integer tokenLimitExpire) {
        this.tokenLimitExpire = tokenLimitExpire;
    }
}
