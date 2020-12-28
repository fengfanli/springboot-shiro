package com.feng.service;


import com.feng.bean.SysPermission;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    /**
     * 根据 用户id 获取权限名称 集合
     * @param userId
     * @return
     */
    Set<String> getPermissionPermsByUserId(String userId);

    /**
     * 根据用户id 获取 权限集合
     * @param userId
     * @return
     */
    List<SysPermission> getPermissionsByUserId(String userId);
}
