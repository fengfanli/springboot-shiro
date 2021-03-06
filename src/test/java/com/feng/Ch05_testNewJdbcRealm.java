package com.feng;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Ch05_testNewJdbcRealm {

    /**
     * 修改数据库的表,统一加前缀 sys_:: users=>sys_users，user_roles=>sys_user_roles, roles_permissions=>sys_roles_permissions
     * 进行查询自定义的表
     */
    @Test
    public void testNewJdbcRealm() {
        // 配置数据源
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://192.168.131.168:3306/shiro");
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("Dataadt123!");

        // 1. 构建安全管理器环境
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 2. 配置数据源
        // 配置文件中的用户权限信息，文件在类路径下
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(druidDataSource);
        // 使用 JdbcRealm 下面的值需要为 true 不然无法查询用户权限
        jdbcRealm.setPermissionsLookupEnabled(true);

        // 使用自定义的 SQL 查询用户权限
        String sql = "select password from users where username =?";
        jdbcRealm.setAuthenticationQuery(sql);
        String roleSql = "select role_name from user_roles where username = ?";
        jdbcRealm.setUserRolesQuery(roleSql);
        String permissionSql = "select permission from roles_permissions where role_name = ?";
        jdbcRealm.setPermissionsQuery(permissionSql);
        // 设置 Realm
        securityManager.setRealm(jdbcRealm);
        SecurityUtils.setSecurityManager(securityManager);

        //获取主体
        Subject subject = SecurityUtils.getSubject();
        //用户名和密码的token
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("admin", "123456");
        try {
            // 2.主体提交认证
            subject.login(usernamePasswordToken);
            System.out.println("---------->用户认证的状态：isAuthenticated=" + subject.isAuthenticated());
            // 检查是否有角色
            subject.checkRoles("admin");
            System.out.println("---------->有 admin 角色");
            // 检查是够有权限
            subject.checkPermissions("user:list");
            System.out.println("---------->有 user:list 权限");

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
