package com.feng;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Ch03_testIniRealm {

    /**
     * 读取 .ini配置文件 作为 数据源Realm
     */
    @Test
    public void testIniRealm() {
        // 配置文件中用户权限信息，文件在类路径下
        IniRealm initRealm = new IniRealm("classpath:shiro.ini");

        // 1. 构建安全管理器环境
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 2. 设置Realm
        securityManager.setRealm(initRealm);
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 获取主体
        Subject subject = SecurityUtils.getSubject();
        // 4. 用户名和密码的token
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("admin", "123456");
        try {
            // 5.主体提交认证请求
            subject.login(usernamePasswordToken);
            System.out.println("---------->用户认证的状态：isAuthenticated=" + subject.isAuthenticated());
            // 6. 检查是否有角色
            subject.checkRoles("admin"); // test用户没有权限访问
            // 检查是够有权限
            subject.checkPermissions("user:add","user:list");
            System.out.println("---------->用户认证的状态：isAuthenticated=" + subject.isAuthenticated());

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
        } catch (UnauthorizedException e) {
            // 关于是够有权限的 异常
            System.out.println("该用户没有权限访问");
        }
    }
}
