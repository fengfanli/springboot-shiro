package com.feng.dao;

import com.feng.bean.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    /**
     * 根据用户  查询关联的  角色id集合
     * @param userId
     * @return
     */
    List<String> getRoleIdsByUserId(String userId);

    /**
     * 根据角色id集合获取所有关联用户di集合
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