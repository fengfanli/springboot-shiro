package com.feng.shiro;

import com.feng.exception.BusinessException;
import com.feng.utils.RedisUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        ShiroUsernamePasswordToken shiroUsernamePasswordToken= (ShiroUsernamePasswordToken) token;
        String accessToken = (String) shiroUsernamePasswordToken.getPrincipal();
        if(!redisUtil.hasKey(accessToken)){
            throw new BusinessException(4001002,"授权信息信息无效请重新登录");
        }
        return true;
    }
}
