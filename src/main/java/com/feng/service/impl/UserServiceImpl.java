package com.feng.service.impl;

import com.feng.bean.SysUser;
import com.feng.dao.SysUserMapper;
import com.feng.exception.BusinessException;
import com.feng.service.UserService;
import com.feng.utils.PasswordUtils;
import com.feng.utils.RedisUtil;
import com.feng.vo.LoginReqVO;
import com.feng.vo.LoginRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public LoginRespVO login(LoginReqVO loginReqVO) {
        SysUser userByName = sysUserMapper.getUserByUsername(loginReqVO.getUsername());
        if(userByName==null){
            throw new BusinessException(4001004,"无此用户");
        }
        if(userByName.getStatus()==2){
            throw new BusinessException(4001004,"该账户已经被锁定，请联系系统管理员");
        }
//
//        if(!getPasswordMatcher(vo.getPassword(),userByName.getSalt()).equals(userByName.getPassword())){
//           throw new BusinessException(4001004,"用户名密码不匹配");
//       }
        if(!PasswordUtils.matches(userByName.getSalt(),loginReqVO.getPassword(),userByName.getPassword())){
            throw new BusinessException(4001004,"用户名密码不匹配");
        }
        LoginRespVO loginRespVO=new LoginRespVO();
        loginRespVO.setId(userByName.getId());
        // 设置token。
        String token= UUID.randomUUID().toString();
        loginRespVO.setToken(token);
        redisUtil.set(token, userByName.getId(), 60, TimeUnit.MINUTES);
        return loginRespVO;

    }

    @Override
    public SysUser detail(String id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }
}
