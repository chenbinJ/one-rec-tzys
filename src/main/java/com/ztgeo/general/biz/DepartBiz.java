package com.ztgeo.general.biz;

import com.alibaba.fastjson.JSONObject;
import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.github.wxiaoqi.security.common.exception.base.BusinessException;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.UUIDUtils;
import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.mapper.DepartMapper;
import com.ztgeo.general.mapper.PositionMapper;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *
 * @author Mr.AG
 * @email 463540703@qq.com
 * @version 2018-02-04 19:06:43
 */
@Slf4j
@Service
public class DepartBiz extends BusinessBiz<DepartMapper, Depart> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DepartMapper departMapper;
    @Autowired
    private PositionMapper positionMapper;

    /*@MergeResult(resultParser = TableResultParser.class)*/
    public TableResultResponse<User> getDepartUsers(String departId, String userName) {
        List<User> users = this.mapper.selectDepartUsers(departId,userName);
        return new TableResultResponse<User>(users.size(),users);
    }

    public Object findDepartOrPosition(){
        List<Depart> departList=departMapper.selectAllDepart();
        for (Depart depart:departList) {
            depart.setChildren(positionMapper.getPositionByDepart(depart.getId()));
        }
        return departList;
    }




    public void addDepartUser(String departId, String userIds) {
        if (!StringUtils.isEmpty(userIds)) {
            String[] uIds = userIds.split(",");
            for (String uId : uIds) {
                this.mapper.insertDepartUser(UUIDUtils.generateUuid(),departId,uId, BaseContextHandler.getTenantID());
            }
        }
    }

    /**
     * 根据ID批量获取部门值
     * @param departIDs
     * @return
     */
    public Map<String,String> getDeparts(String departIDs){
        log.info("触发了merge程序");
        if(StringUtils.isBlank(departIDs)) {
            return new HashMap<>();
        }
        departIDs = "'"+departIDs.replaceAll(",","','")+"'";
        List<Depart> departs = mapper.selectByIds(departIDs);
        return departs.stream().collect(Collectors.toMap(Depart::getId, depart -> JSONObject.toJSONString(depart)));
    }

    public void delDepartUser(String departId, String userId) {
        User user = userMapper.selectByPrimaryKey(userId);
//        if(user.getDepartId().equals(departId)){
//            throw new BusinessException("无法移除用户的默认关联部门,若需移除,请前往用户模块更新用户部门!");
//        }
        this.mapper.deleteDepartUser(departId,userId);
    }

    @Override
    public void insertSelective(Depart entity) {
        entity.setId(UUIDUtils.generateUuid());
        super.insertSelective(entity);
    }
}
