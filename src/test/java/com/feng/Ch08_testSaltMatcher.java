package com.feng;

import com.feng.shiro.CustomRealmCh08;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class Ch08_testSaltMatcher {

    /**
     *  SpringBoot整合 shiro 之 盐值加密认证
     *
     *  加盐认证:  设置加密次数 1
     *
     *  当两个用户的密码相同时，单纯使用不加盐的MD5加密方式，会发现数据库中存在相同结构的密码，这样也是不安全的。我们希望即
     *  便是两个人的原始密码一样，加密后的结果也不一样。如何做到呢？其实就好像炒菜一样，两道一样的鱼香肉丝，加的盐不一样，炒
     *  出来的味道就不一样。MD5加密也是一样，需要进行盐值加密。
     */
    @Test
    public void testSaltMatcher() {
        // 1. 构建安全管理器环境
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 2. 构建数据源
        CustomRealmCh08 customRealmCh08 = new CustomRealmCh08();

        // 对数据源进行 md5 加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        //设置加密次数
        matcher.setHashIterations(1);
        customRealmCh08.setCredentialsMatcher(matcher);

        // 设置数据源
        securityManager.setRealm(customRealmCh08);
        // 设置 安全管理器
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 获取主体
        Subject subject = SecurityUtils.getSubject();
        //用户名和密码的token
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("admin", "123456");
        System.out.println("从UsernamePasswordToken 获取的用户名和密码："+usernamePasswordToken.getUsername()+usernamePasswordToken.getPassword());
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
