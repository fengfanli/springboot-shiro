package com.feng.service.impl;

import com.feng.bean.SysUser;
import com.feng.dao.SysUserMapper;
import com.feng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public SysUser getUserInfoByUsername(String username) {
        SysUser user = sysUserMapper.getUserByUsername(username);
        return user;
    }
}
