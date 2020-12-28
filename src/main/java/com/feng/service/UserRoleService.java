package com.feng.service;


import java.util.List;

/**
 * @ClassName: UserRoleService
 * @Description： 用户 角色 业务层
 * @createTime: 2020/2/19 22:49
 * @Author: 冯凡利
 * @UpdateUser: 冯凡利
 * @Version: 0.0.1
 */
public interface UserRoleService {
    /**
     * 根据用户id 获取 角色Id
     * @param userId
     * @return
     */
    List<String> getRoleIdsByUserId(String userId);


    /**
     * 用户 用户id集合 根据 角色id集合，在用户角色表中
     * @param roleIds
     * @return
     */
    List<String> getUserIdsByRoleIds(List<String> roleIds);

    /**
     * 根据角色id 获取 用户id集合
     * @param roleId
     * @return
     */
    List<String> getUserIdsByRoleId(String roleId);


}

