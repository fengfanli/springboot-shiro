package com.feng.service.impl;

import com.feng.dao.SysUserRoleMapper;
import com.feng.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: UserRoleServiceImpl
 * @Description： 描述
 * @createTime:
 * @Author: 冯凡利
 * @UpdateUser: 冯凡利
 * @Version: 0.0.1
 */
@Slf4j
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 根据用户id 获取 角色Id
     * @param userId
     * @return
     */
    @Override
    public List<String> getRoleIdsByUserId(String userId) {
        List<String> roleIdsByUserId = sysUserRoleMapper.getRoleIdsByUserId(userId);
        return roleIdsByUserId;
    }


    /**
     * 用户 用户id集合 根据 角色id集合，在用户角色表中
     * @param roleIds
     * @return
     */
    @Override
    public List<String> getUserIdsByRoleIds(List<String> roleIds) {
        return sysUserRoleMapper.getUserIdsByRoleIds(roleIds);
    }

    /**
     * 根据角色id 获取 用户id集合
     * @param roleId
     * @return
     */
    @Override
    public List<String> getUserIdsByRoleId(String roleId) {
        return sysUserRoleMapper.getUserIdsByRoleId(roleId);
    }
}

