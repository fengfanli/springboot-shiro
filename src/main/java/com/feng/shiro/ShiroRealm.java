package com.feng.shiro;

import com.feng.bean.SysUser;
import com.feng.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("ShiroRealm.doGetAuthenticationInfo()");
        //获取用户的输入的账号.
        String username = (String)authenticationToken.getPrincipal();
        //通过username从数据库中查找 User对象
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        SysUser userInfo = userService.getUserInfoByUsername(username);
        if(userInfo == null){
            return null;
        }
        String passwordMatcher = getPasswordMatcher(userInfo.getPassword());
        // 三个参数：user，password，ShiroRealm 字符串
        // SimpleAuthenticationInfo 有好多构造函数
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                username,
                passwordMatcher,
                ShiroRealm.class.getName());
        return info;
    }

    /**
     * 获得密文密码
     * 不加盐
     * @Author:     小冯
     * @UpdateUser:
     * @Version:     0.0.1
     * @param currentPassword
     * @return       java.lang.String
     * @throws
     */
    private String getPasswordMatcher(String currentPassword){
        return new Md5Hash(currentPassword, null,2).toString();
    }
}
