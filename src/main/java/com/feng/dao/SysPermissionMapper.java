package com.feng.dao;

import com.feng.bean.SysPermission;

import java.util.List;

public interface SysPermissionMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    /**
     * 根据权限id集合 获取 权限集合
     * @param permissionIds
     * @return
     */
    List<SysPermission> getPermissionByPermissionIds(List<String> permissionIds);
}