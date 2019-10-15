package com.ztgeo.general.biz;

import com.ace.cache.annotation.Cache;
import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.merge.core.MergeCore;
import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.github.wxiaoqi.security.common.util.*;
import com.ztgeo.general.constant.AdminCommonConstant;
import com.ztgeo.general.constant.UserConstant;
import com.ztgeo.general.entity.*;
import com.ztgeo.general.mapper.DepartMapper;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.vo.FrontUser;
import com.ztgeo.general.vo.MenuTree;
import com.ztgeo.general.vo.PermissionInfo;
import com.ztgeo.general.vo.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserBiz extends BusinessBiz<UserMapper, User> {

    @Autowired
    private MenuBiz menuBiz;

    @Autowired
    private ElementBiz elementBiz;



    @Autowired
    private DepartMapper departMapper;

    @Autowired
    private MergeCore mergeCore;

    /*
    * 基础信息拓展
    * */
    @Override
    public User selectById(Object id) {
        User user = super.selectById(id);
        try {
            //如果调用此方法 则调用user的merge程序
            mergeCore.mergeOne(User.class, user);
            return user;
        } catch (Exception e) {
            return super.selectById(id);
        }
    }

    /*
     *  注冊使用戶
     */
    @Override
    public void insertSelective(User entity) {
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
        String departId = entity.getDepartId();
        EntityUtils.setCreatAndUpdatInfo(entity);
        entity.setPassword(password);
        entity.setDepartId(departId);
        entity.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
        entity.setIsDisabled(BooleanUtil.BOOLEAN_FALSE);
        String userId = UUIDUtils.generateUuid();
        entity.setTenantId(BaseContextHandler.getTenantID());
        entity.setId(userId);
        entity.setIsSuperAdmin(BooleanUtil.BOOLEAN_FALSE);
        // 如果非超级管理员,无法修改用户的租户信息
        if (BooleanUtil.BOOLEAN_FALSE.equals(mapper.selectByPrimaryKey(BaseContextHandler.getUserID()).getIsSuperAdmin())) {
            entity.setIsSuperAdmin(BooleanUtil.BOOLEAN_FALSE);
        }
        departMapper.insertDepartUser(UUIDUtils.generateUuid(), entity.getDepartId(), entity.getId(),BaseContextHandler.getTenantID());
        //super.insertSelective(entity);
        mapper.insertSelective(entity);
    }

    @Override
    public void updateSelectiveById(User entity) {
        User user = mapper.selectByPrimaryKey(entity.getId());
        if (!user.getDepartId().equals(entity.getDepartId())) {
            departMapper.deleteDepartUser(user.getDepartId(), entity.getId());
            departMapper.insertDepartUser(UUIDUtils.generateUuid(), entity.getDepartId(), entity.getId(),BaseContextHandler.getTenantID());
        }
        // 如果非超级管理员,无法修改用户的租户信息
        if (BooleanUtil.BOOLEAN_FALSE.equals(mapper.selectByPrimaryKey(BaseContextHandler.getUserID()).getIsSuperAdmin())) {
            entity.setTenantId(BaseContextHandler.getTenantID());
        }
        // 如果非超级管理员,无法修改用户的租户信息
        if (BooleanUtil.BOOLEAN_FALSE.equals(mapper.selectByPrimaryKey(BaseContextHandler.getUserID()).getIsSuperAdmin())) {
            entity.setIsSuperAdmin(BooleanUtil.BOOLEAN_FALSE);
        }
        super.updateSelectiveById(entity);
    }

//    public Boolean changePassword(String oldPass, String newPass) {
//        User user = this.getUserByUsername(BaseContextHandler.getUsername());
//        if (!encoder.matches(oldPass, user.getPassword())) return false;
//        String password = encoder.encode(newPass);
//        //加入密码必须包含大小写字母、数字和8-16位正则验证
//        if (newPass.matches("^((?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])).{8,16}$")) {
//            user.setPassword(password);
//            this.updateSelectiveById(user);
//            return true;
//        }
//        return false;
//    }


    @Override
    public void deleteById(Object id) {
        User user = mapper.selectByPrimaryKey(id);
        user.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);
        //this.updateSelectiveById(user);
        mapper.deleteByPrimaryKey(id);
    }
/**
 * SQL语句拼接
 */
    @Override
    public void query2criteria(Query query, Example example) {
        if (query.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                Example.Criteria criteria = example.createCriteria();
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
                example.or(criteria);
            }
        }
    }

    @Override
    public List<User> selectByExample(Object obj) {
        Example example = (Example) obj;
        example.createCriteria().andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        List<User> users = super.selectByExample(example);
        try {
            mergeCore.mergeResult(User.class, users);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return users;
        }
    }


    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        user.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
        user.setIsDisabled(BooleanUtil.BOOLEAN_FALSE);
        return mapper.selectOne(user);
    }

    @Cache(key="permission:u{1}")
    public List<PermissionInfo> getPermissionByUsername(String userid) {
        List<Menu> menus = menuBiz.getUserAuthorityMenuByUserId(userid);
        List<PermissionInfo> result = new ArrayList<PermissionInfo>();
        PermissionInfo info = null;
        menu2permission(menus, result);
        List<Element> elements = elementBiz.getAuthorityElementByUserId(BaseContextHandler.getUserID());
        element2permission(result, elements);
        return result;
    }

    public FrontUser getUserInfo() throws Exception {
        String username = BaseContextHandler.getUsername();
        if (username == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        User user = this.getUserByUsername(username);
        BeanUtils.copyProperties(user, userInfo);
        FrontUser frontUser = new FrontUser();
        BeanUtils.copyProperties(userInfo, frontUser);
        List<PermissionInfo> permissionInfos = this.getPermissionByUsername(BaseContextHandler.getUserID());
        Stream<PermissionInfo> menus = permissionInfos.parallelStream().filter((permission) -> {
            return permission.getType().equals(AdminCommonConstant.RESOURCE_TYPE_MENU);
        });
        frontUser.setMenus(menus.collect(Collectors.toList()));
        Stream<PermissionInfo> elements = permissionInfos.parallelStream().filter((permission) -> {
            return !permission.getType().equals(AdminCommonConstant.RESOURCE_TYPE_MENU);
        });
        frontUser.setElements(elements.collect(Collectors.toList()));
        return frontUser;
    }

    /**
     * 根据用户名获取菜单信息
     * @return
     * @throws Exception
     */
    public List<MenuTree> getMenusByUsername() throws Exception {
        List<Menu> menus = menuBiz.getUserAuthorityMenuByUserId(BaseContextHandler.getUserID());
        return getMenuTree(menus, AdminCommonConstant.ROOT);
    }


    private List<MenuTree> getMenuTree(List<Menu> menus, String root) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        for (Menu menu : menus) {
            node = new MenuTree();
            BeanUtils.copyProperties(menu, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root, new Comparator<MenuTree>() {
            @Override
            public int compare(MenuTree o1, MenuTree o2) {
                return o1.getOrderNum() - o2.getOrderNum();
            }
        });
    }

    /*
    * 获取所有的权限列表
    * */
    public List<PermissionInfo> getAllPermission() {
        List<Menu> menus = menuBiz.selectListAll();
        List<PermissionInfo> result = new ArrayList<PermissionInfo>();
        PermissionInfo info = null;
        menu2permission(menus, result);
        List<Element> elements = elementBiz.getAllElementPermissions();
        element2permission(result, elements);
        return result;
    }

    private void element2permission(List<PermissionInfo> result, List<Element> elements) {
        PermissionInfo info;
        for (Element element : elements) {
            info = new PermissionInfo();
            info.setCode(element.getCode());
            info.setType(element.getType());
            info.setUri(element.getUri());
            info.setMethod(element.getMethod());
            info.setName(element.getName());
            info.setMenu(element.getMenuId());
            result.add(info);
        }
    }

    private void menu2permission(List<Menu> menus, List<PermissionInfo> result) {
        PermissionInfo info;
        for (Menu menu : menus) {
            if (StringUtils.isBlank(menu.getHref())) {
                menu.setHref("/" + menu.getCode());
            }
            info = new PermissionInfo();
            info.setCode(menu.getCode());
            info.setType(AdminCommonConstant.RESOURCE_TYPE_MENU);
            info.setName(AdminCommonConstant.RESOURCE_ACTION_VISIT);
            String uri = menu.getHref();
            if (!uri.startsWith("/")) {
                uri = "/" + uri;
            }
            info.setUri(uri);
            info.setMethod(AdminCommonConstant.RESOURCE_REQUEST_METHOD_GET);
            result.add(info
            );
            info.setMenu(menu.getTitle());
        }
    }


    public List<String> getUserDataDepartIds(String userId) {
        return mapper.selectUserDataDepartIds(userId);
    }
}
