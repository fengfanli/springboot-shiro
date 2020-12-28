package com.feng.dao;

import com.feng.bean.SysRolePermission;

import java.util.List;

public interface SysRolePermissionMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    /**
     * 根据菜单权限id获取关联的角色id集合
     * @param permissionId
     * @return
     */
    List<String> getRoleIdsByPermissionId(String permissionId);

    /**
     * 根据角色id获取该角色关联的菜单权限id集合
     * @param roleId
     * @return
     */
    List<String> getPermissionIdsByRoleId(String roleId);

    /**
     * 根据角色id集合 获取 权限id集合
     * @param roleIds
     * @return
     */
    List<String> getPermissionIdsByRoleIds(List<String> roleIds);
}