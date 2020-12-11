package com.feng;

import com.feng.shiro.CustomRealmCh06;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Ch06_testCustomRealm {

    /**
     * 自定义 数据源 CustomRealm 。
     */
    @Test
    public void testCustomRealm() {
        // 1. 构建安全管理器环境
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 2. 构建数据源
        CustomRealmCh06 customRealmCh06 = new CustomRealmCh06();
        securityManager.setRealm(customRealmCh06);

        SecurityUtils.setSecurityManager(securityManager);

        // 3. 获取主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("dev", "123456");
        System.out.println(usernamePasswordToken.getUsername());
        System.out.println(usernamePasswordToken.getPassword());
        System.out.println(usernamePasswordToken.getPrincipal());
        System.out.println(usernamePasswordToken.getCredentials());
        try {
            // 2.主体提交认证
            subject.login(usernamePasswordToken);
            System.out.println("---------->用户认证的状态：isAuthenticated=" + subject.isAuthenticated());
            // 检查是否有角色
            subject.checkRoles("admin");
            System.out.println("---------->有 admin 角色");
            // 检查是够有权限
            subject.checkPermissions("user:delete", "user:add", "user:edit", "user:list");
            System.out.println("---------->有 user:delete, user:add, user:edit 权限");

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
        } catch (UnauthorizedException ue){
            System.out.println("用户没有权限");
        }
    }
}
