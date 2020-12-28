package com.feng.service;


import com.feng.bean.SysRole;

import java.util.List;

public interface RoleService {


    /**
     * 根据 用户id 获取 角色名称集合
     * @param userId
     * @return
     */
    List<String> getRoleNamesByUserId(String userId);

    /**
     * 根据用户id 获取 角色信息集合
     * @param userId
     * @return
     */
    List<SysRole> getRoleInfoByUserId(String userId);

}
