package com.feng;

import com.feng.shiro.CustomRealmCh07;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Ch07_testMatcher {

    /**
     *  SpringBoot整合 shiro 之 不加盐 加密认证
     *
     *  不加盐认证::  设置加密次数 2
     *  1、使用 类：HashedCredentialsMatcher，并设置加密算法和加密次数，
     *  2、自定义数据源 在设置 此类HashedCredentialsMatcher
     *
     *  3、然后在
     */
    @Test
    public void testMatcher() {
        // 1. 构建安全管理器环境
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 2. 构建数据源
        CustomRealmCh07 customRealmCh07 = new CustomRealmCh07();
        // 3. 初始化 md5 加密类
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        //设置加密次数
        matcher.setHashIterations(2);
        // 对数据源设置加密
        customRealmCh07.setCredentialsMatcher(matcher);
        // 设置数据源
        securityManager.setRealm(customRealmCh07);

        // 设置 安全管理器
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 获取主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("admin", "123456");
        System.out.println("从UsernamePasswordToken 获取的用户名和密码："+usernamePasswordToken.getUsername()+"/"+usernamePasswordToken.getPassword());
        System.out.println("获取用户名和密码的 token："+usernamePasswordToken.toString());
        try {
            // 2.主体提交认证
            subject.login(usernamePasswordToken);
            System.out.println("---------->用户认证的状态：isAuthenticated=" + subject.isAuthenticated());
            // 检查是否有角色
            subject.checkRoles("admin");
            System.out.println("---------->有 admin 角色");
            // 检查是够有权限
            subject.checkPermissions("user:delete", "user:edit", "user:list", "user:add");
            System.out.println("---------->有 user:delete, user:edit, user:list, user:add 权限");

            System.out.println("执行 logout()方法后");
            subject.logout();
            System.out.println("---------->用户认证的状态：" + subject.isAuthenticated());
        } catch (UnknownAccountException e) {
            //username wasn't in the system, show them an error message?
            System.out.println("账号不存在");
        } catch (IncorrectCredentialsException ice) {
            System.out.println("用户名密码不匹配");
            //password didn't match, try again?
        } catch (LockedAccountException lae) {
            //account for that username is locked - can't login.  Show them a message?
            System.out.println("账号已被禁用，请联系系统管理员");
        } catch (AuthenticationException ae) {
            //unexpected condition - error?
            System.out.println("用户认证失败");
        } catch (UnauthorizedException ae) {
            System.out.println("用户没有权限");
        }
    }
}
