package com.ztgeo.general.util.jwt;

import com.github.ag.core.constants.CommonConstants;
import com.github.ag.core.context.BaseContextHandler;
import com.github.ag.core.util.jwt.IJWTInfo;
import com.github.ag.core.util.jwt.JWTHelper;
import com.github.wxiaoqi.security.common.exception.auth.NonLoginException;
import com.ztgeo.general.config.UserAuthConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class UserAuthUtil {
    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JWTHelper jwtHelper;

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            IJWTInfo infoFromToken = jwtHelper.getInfoFromToken(token, userAuthConfig.getPubKeyByte());
//            设置BaseContextHandler
            BaseContextHandler.setUsername(infoFromToken.getUniqueName());
            BaseContextHandler.setName(infoFromToken.getName());
            BaseContextHandler.setUserID(infoFromToken.getId());
            BaseContextHandler.setTenantID(infoFromToken.getOtherInfo().get(CommonConstants.JWT_KEY_TENANT_ID));
            BaseContextHandler.setDepartID(infoFromToken.getOtherInfo().get(CommonConstants.JWT_KEY_DEPART_ID));
            if(redisTemplate.hasKey(CommonConstants.REDIS_USER_TOKEN+infoFromToken.getId()+infoFromToken.getExpireTime().getTime())){
                throw new NonLoginException("token验证失败!");
            }
            if(new DateTime(infoFromToken.getExpireTime()).plusMinutes(userAuthConfig.getTokenLimitExpire()).isBeforeNow()){
                throw new NonLoginException("token已过期!");
            }
            return infoFromToken;
        } catch (ExpiredJwtException ex) {
            throw new NonLoginException("token已过期!");
        } catch (SignatureException ex) {
            throw new NonLoginException("token签名异常!");
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            throw new NonLoginException("User token is null or empty!");
        }
    }
}
