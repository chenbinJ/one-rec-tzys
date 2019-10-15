package com.ztgeo.general.biz;

import com.github.ag.core.constants.CommonConstants;
import com.github.ag.core.context.BaseContextHandler;
import com.github.ag.core.util.jwt.IJWTInfo;
import com.github.ag.core.util.jwt.JWTInfo;
import com.ztgeo.general.dicFinal.FinalStr;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.vo.UserInfo;
import com.ztgeo.general.exception.LoginErrorException;
import com.ztgeo.general.util.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminBiz {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private JwtTokenUtil jwtTokenUtil;

//   完成util的装配
    @Autowired
    public AdminBiz(
            JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private UserBiz userBiz;

    public String login(String username, String password) throws Exception {
        UserInfo info = new UserInfo();
        User user = userBiz.getUserByUsername(username);
        if(user==null){
            throw new LoginErrorException("用户不存在");
        }
        String token = "";
        if (encoder.matches(password, user.getPassword())) {
            Map<String, String> map = new HashMap<>();
            map.put(CommonConstants.JWT_KEY_TENANT_ID, String.valueOf(user.getTenantId()));
            map.put(CommonConstants.JWT_KEY_DEPART_ID, String.valueOf(user.getDepartId()));
            JWTInfo jwtInfo = new JWTInfo(user.getUsername(), user.getId(), user.getName());
            token = jwtTokenUtil.generateToken(jwtInfo, map);
        }else{
            throw new LoginErrorException(FinalStr.LOGIN_ERROR_MSG);
        }
        return token;
    }


    /*
    * 登出的方法
    * */
    public Boolean invalid(String token) throws Exception {
        IJWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(token);
        redisTemplate.opsForValue().set(CommonConstants.REDIS_USER_TOKEN + infoFromToken.getId() + infoFromToken.getExpireTime().getTime(), "1");
        return true;
    }
}
