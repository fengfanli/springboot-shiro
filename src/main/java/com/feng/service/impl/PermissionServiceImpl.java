package com.feng.service.impl;

import com.feng.bean.SysPermission;
import com.feng.dao.SysPermissionMapper;
import com.feng.service.PermissionService;
import com.feng.service.RolePermissionService;
import com.feng.service.UserRoleService;
import com.feng.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName: PermissionServiceImpl
 * @Description： 描述
 * @createTime:
 * @Author: 冯凡利
 * @UpdateUser: 冯凡利
 * @Version: 0.0.1
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private RolePermissionService rolePermissionService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RedisUtil redisUtil;



    /**
     * 根据 用户id 获取权限名称 集合
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> getPermissionPermsByUserId(String userId) {

        List<SysPermission> sysPermissions = getPermissionsByUserId(userId);
        Set<String> permissions = new HashSet<>();
        if (null == sysPermissions || sysPermissions.isEmpty()) {
            return null;
        }
        for (SysPermission sysPermission : sysPermissions) {
            if (!StringUtils.isEmpty(sysPermission.getPerms())) {
                permissions.add(sysPermission.getPerms());
            }
        }
        return permissions;
    }

    /**
     * 根据用户id 获取 权限集合
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysPermission> getPermissionsByUserId(String userId) {
        // 1. 从用户角色表 中获取 角色id集合
        List<String> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return null;
        }
        // 从角色权限中 根据角色id集合 获取 权限id集合
        List<String> permissionIds = rolePermissionService.getPermissionIdsByRoleIds(roleIds);

        // 从权限表中 根据权限id集合  获取权限信息集合
        List<SysPermission> result = sysPermissionMapper.getPermissionByPermissionIds(permissionIds);
        return result;
    }


}

