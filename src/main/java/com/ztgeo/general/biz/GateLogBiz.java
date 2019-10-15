package com.ztgeo.general.biz;
import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.ztgeo.general.entity.GateLog;
import com.ztgeo.general.mapper.GateLogMapper;
import com.ztgeo.general.vo.LogInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @version 2017-07-01 14:36
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GateLogBiz extends BusinessBiz<GateLogMapper,GateLog> {

    @Override
    public void insertSelective(GateLog entity) {
        mapper.insertSelective(entity);
    }


    /*
    * 其它服务调取用来保存相关log信息
    * */
    public void saveLog(LogInfo info){
        GateLog logNow = new GateLog();
        BeanUtils.copyProperties(info,logNow);
        mapper.insertSelective(logNow);
    }
}
