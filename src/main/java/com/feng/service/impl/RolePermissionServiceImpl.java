package com.feng.service.impl;

import com.feng.dao.SysRolePermissionMapper;
import com.feng.service.RolePermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: RolePermissionService
 * @Description： 描述
 * @createTime:
 * @Author: 冯凡利
 * @UpdateUser: 冯凡利
 * @Version: 0.0.1
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Resource
    private RolePermissionService rolePermissionService;



    /**
     *根据菜单权限id 获取 关联的角色id集合
     * @param permissionId
     * @return
     */
    @Override
    public List<String> getRoleIdsByPermissionId(String permissionId) {
        return sysRolePermissionMapper.getRoleIdsByPermissionId(permissionId);
    }

    /**
     * 根据角色id获取该角色关联的菜单权限id集合
     * @param roleId
     * @return
     */
    @Override
    public List<String> getPermissionIdsByRoleId(String roleId) {
        return sysRolePermissionMapper.getPermissionIdsByRoleId(roleId);
    }

    /**
     * 根据角色id集合  获取权限id集合
     * @param roleIds
     * @return
     */
    @Override
    public List<String> getPermissionIdsByRoleIds(List<String> roleIds) {
        List<String> permissionIds = sysRolePermissionMapper.getPermissionIdsByRoleIds(roleIds);
        return permissionIds;
    }
}

