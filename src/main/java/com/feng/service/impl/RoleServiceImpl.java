package com.feng.service.impl;

import com.feng.bean.SysRole;
import com.feng.dao.SysRoleMapper;
import com.feng.service.PermissionService;
import com.feng.service.RolePermissionService;
import com.feng.service.RoleService;
import com.feng.service.UserRoleService;
import com.feng.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName: RoleServiceImpl
 * @Description： 描述
 * @createTime:
 * @Author: 冯凡利
 * @UpdateUser: 冯凡利
 * @Version: 0.0.1
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private RolePermissionService rolePermissionService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 根据 用户id 获取 角色名称集合
     * @param userId
     * @return
     */
    @Override
    public List<String> getRoleNamesByUserId(String userId) {
        // 通过 用户id 获取 角色信息：：：1. 先从用户角色表获取角色id，2.在从角色表中获取角色信息
        List<SysRole> sysRoles = getRoleInfoByUserId(userId);
        if (null == sysRoles || sysRoles.isEmpty()){
            return null;
        }
        List<String> list = new ArrayList<>();
        for (SysRole sysRole : sysRoles){
            list.add(sysRole.getName());
        }
        return list;
    }

    /**
     * 根据用户id 获取 角色信息集合
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> getRoleInfoByUserId(String userId) {
        // 1. 从用户角色表中 获取 角色id
        List<String> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()){
            return null;
        }
        // 2. 从角色表中 获取 角色信息
        return sysRoleMapper.getRoleInfoByRoleIds(roleIds);
    }
}

