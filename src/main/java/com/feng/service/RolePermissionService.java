package com.feng.service;

import java.util.List;

public interface RolePermissionService {


    /**
     *根据菜单权限id获取关联的角色id集合
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
     * 根据角色id集合  获取权限id集合
     * @param roleIds
     * @return
     */
    List<String> getPermissionIdsByRoleIds(List<String> roleIds);
}
