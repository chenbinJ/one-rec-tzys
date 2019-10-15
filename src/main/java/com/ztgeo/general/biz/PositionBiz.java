package com.ztgeo.general.biz;
import com.ace.cache.annotation.CacheClear;
import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.github.wxiaoqi.security.common.util.UUIDUtils;
import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.entity.Group;
import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.mapper.PositionMapper;
import com.ztgeo.general.mapper.chenbin.EntryPositManagerMapper;
import com.ztgeo.general.mapper.chenbin.PositSvrManagerMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.vo.DepartTree;
import com.ztgeo.general.vo.GroupTree;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
/**
 * @author weihao
 */
@Service
public class PositionBiz extends BusinessBiz<PositionMapper, Position> {
    @Autowired
    private PositSvrManagerMapper positSvrManagerMapper;//岗位服务mapper
    @Autowired
    private StepManagerMapper stepManagerMapper;//步骤mapper
    @Autowired
    private EntryPositManagerMapper entryPositManagerMapper;//岗位条目mapper

    /**
     * 更改岗位用户
     * @param positionId
     * @param users
     */
    @CacheClear(pre = "permission")
    public void modifyPositionUsers(String positionId, String users) {
        mapper.deletePositionUsers(positionId);//先删除全部posit-user然后再添加
        if(StringUtils.isNotBlank(users)){
            for (String uId : users.split(",")) {
                mapper.insertPositionUser(UUIDUtils.generateUuid(),positionId,uId, BaseContextHandler.getTenantID());
            }
        }
    }

    /**
     * 获取岗位用户
     * @param positionId
     * @return
     */
    public List<User> getPositionUsers(String positionId) {
        return mapper.selectPositionUsers(positionId);
    }

    public void modifyPositionGroups(String positionId, String groups) {
        mapper.deletePositionGroups(positionId);
        if(StringUtils.isNotBlank(groups)) {
            for (String groupId : groups.split(",")) {
                mapper.insertPositionGroup(UUIDUtils.generateUuid(),positionId,groupId, BaseContextHandler.getTenantID());
            }
        }
    }

    public List<GroupTree> getPositionGroups(String positionId) {
        List<Group> groups = mapper.selectPositionGroups(positionId);
        List<GroupTree> trees = new ArrayList<GroupTree>();
        GroupTree node = null;
        for (Group group : groups) {
            node = new GroupTree();
            node.setLabel(group.getName());
            BeanUtils.copyProperties(group, node);
            trees.add(node);
        }
        return trees;
    }

    public void modifyPositionDeparts(String positionId, String departs) {
        mapper.deletePositionDeparts(positionId);
        if(StringUtils.isNotBlank(departs)) {
            for (String groupId : departs.split(",")) {
                mapper.insertPositionDepart(UUIDUtils.generateUuid(),positionId,groupId, BaseContextHandler.getTenantID());
            }
        }
    }

    public List<DepartTree> getPositionDeparts(String positionId) {
        List<Depart> departs = mapper.selectPositionDeparts(positionId);
        List<DepartTree> trees = new ArrayList<>();
        departs.forEach(depart -> {
            trees.add(new DepartTree(depart.getId(), depart.getParentId(), depart.getName(),depart.getCode()));
        });
        return trees;
    }

    @Override
    public void insertSelective(Position entity) {
        String departId = entity.getDepartId();
        entity.setId(UUIDUtils.generateUuid());
        super.insertSelective(entity);
        entity.setDepartId(departId);
        updateSelectiveById(entity);
    }

    @Override
    public void deleteById(Object id) {
        //删除岗位服务权限
        positSvrManagerMapper.deletePositSvrByParam(null,id.toString());
        //删除岗位步骤权限
        stepManagerMapper.deleteStepPositionByParam(null,null,id.toString());
        //删除岗位条目权限
        entryPositManagerMapper.deleteByParam(id.toString(),null);
        mapper.deleteByPrimaryKey(id);
    }
}
