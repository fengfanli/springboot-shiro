package com.feng;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Ch01_authentication {
    /**
     * shiro 认证 初使用
     */
    @Test
    public void authentication() {
        // 1. 构建安全管理器环境
        /*
        * 如果爆这个错：java.lang.IllegalArgumentException: SessionContext must be an HTTP compatible implementation.
        * 1）. 因为 版本低，要 1.4.1以上，
        * 2）. 或者使用 DefaultSecurityManager 这个类，这个是单机版，DefaultWebSecurityManager 这个是 web 版本
        * */
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 2. 构建数据源，创建一个SimpleAccountRealm 域
        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
        // 添加一个测试账号(后面可以做成读取动态读取数据库)
        simpleAccountRealm.addAccount("feng", "123456");
        // 设置Realm
        securityManager.setRealm(simpleAccountRealm);
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 获取主体
        Subject subject = SecurityUtils.getSubject();
        // 4. 用户名和密码生成token (这里也就是 用户输入的用户名密码 )
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("feng1", "123456");
        System.out.println(usernamePasswordToken.getUsername());
        System.out.println(usernamePasswordToken.getPassword());
        System.out.println(usernamePasswordToken.getPrincipal());
        System.out.println(usernamePasswordToken.getCredentials());
        System.out.println(usernamePasswordToken.getHost());
        System.out.println(usernamePasswordToken.toString());
        try {
            // 5. 进行登入(提交认证)
            subject.login(usernamePasswordToken);

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
        }

        System.out.println("用户认证的状态：isAuthenticated=" + subject.isAuthenticated());
        // 登出logout
        System.out.println("执行 logout()方法后");
        subject.logout();
        System.out.println("用户认证的状态：isAuthenticated=" + subject.isAuthenticated());
    }

}
