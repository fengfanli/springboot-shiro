package com.feng;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Ch02_authorization {
    /**
     * shiro 授权 初使用
     */
    @Test
    public void authorization() {
        // 1. 构建安全管理器环境
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        // 2. 构建数据源,创建一个SimpleAccountRealm 域
        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
        //添加一个测试账号、和所拥有的角色(后面可以做成读取动态读取数据库)
        simpleAccountRealm.addAccount("feng", "123456", "admin", "test");
        // 设置Realm
        securityManager.setRealm(simpleAccountRealm);
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 获取主体
        Subject subject = SecurityUtils.getSubject();
        // 4. 用户名和密码(用户输入的用户名密码)生成token
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("feng", "123456");
        try {
            // 5.进行登入(提交认证)
            subject.login(usernamePasswordToken);
            // 6. 检查是否有角色
            subject.checkRoles("user", "test");
//            subject.checkRoles("admin", "user"); // 该用户没有权限访问
//            subject.checkRoles("user");  // 该用户没有权限访问
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
        } catch (UnauthorizedException e) {
            System.out.println("该用户没有权限访问");
        }

        System.out.println("---------->用户认证的状态：" + subject.isAuthenticated());
        //登出logout
        System.out.println("执行 logout()方法后");
        subject.logout();
        System.out.println("---------->用户认证的状态：" + subject.isAuthenticated());
    }
}
