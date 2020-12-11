package com.feng.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomRealmCh08 extends AuthorizingRealm {

    /**
     * 模拟数据库中的用户名和密码
     */
    private Map<String, String> userMap =new HashMap<>();

    /**
     * 使用代码块初始化数据
     */
    {
        userMap.put("admin", "123456");
        userMap.put("test","123456");
    }


    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("---------->开始执行 CustomRealm.doGetAuthorizationInfo 方法啦");
        // SecurityUtils.getSubject().getPrincipal()用户名： 就是SimpleAuthenticationInfo(username,password,getName()); 第一个参数

        String name= (String) SecurityUtils.getSubject().getPrincipal();
        System.out.println("---------->(String)SecurityUtils.getSubject().getPrincipal():"+name);

        String username = (String) principalCollection.getPrimaryPrincipal();
        System.out.println("---------->(String) principalCollection.getPrimaryPrincipal():"+username);
        //从数据库或者缓存中获取角色数据
        List<String> roles = getRolesByUsername(username);
        //从数据库或者缓存中获取权限数据
        List<String> permissions = getPerminssionsByUsername(username);
        //创建AuthorizationInfo，并设置角色和权限信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(permissions);
        authorizationInfo.addRoles(roles);
        return authorizationInfo;
    }

    /**
     * 认证
     * 逻辑：获取用户输入的用户名。密码不用获取，已经在 shiro 中（密码是 HashedCredentialsMatcher 进行 md5  加密的）
     * 根据用户名获取数据库的密码，然后进行MD5加密。
     * 然后将 用户名、加密后密码、类名称  交给shiro去认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        /*
        * AuthenticationToken 是 UsernamePasswordToken 顶级父类，getPrincipal() 和 getCredentials() 是其接口的两个方法，
        * 被 UsernamePasswordToken实现重新，分别是 获取用户名和密码。
        * 这里也就是获取用户输入的用户名和密码
        * */
        System.out.println("---------->开始执行 CustomRealm.doGetAuthenticationInfo 方法啦");
        //获取登录用户名
        String username = (String) authenticationToken.getPrincipal();
        System.out.println("---------->根据authenticationToken获取到的用户名:"+username);
        //通过用户名到数据库获取用户信息
        String password = getPasswordByUsername(username);
        System.out.println("---------->从数据库中获取的密码："+password);
        if (password == null) {
            return null;
        }
        // 加盐加密

        String passwordMatcher = getPasswordMatcher(password, username);
        System.out.println("---------->获取到的密文密码:" + passwordMatcher);
        // 传入用户名、密码、name 。流转到下面去认证
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                username, passwordMatcher,ByteSource.Util.bytes(username), getName());
        // authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username));
        return authenticationInfo;
    }


    /**
     * 模拟通过数据库获取权限数据
     * @Author:     小冯
     * @UpdateUser:
     * @Version:     0.0.1
     * @param username
     * @return       java.util.List<java.lang.String>
     * @throws
     */
    private List<String> getPerminssionsByUsername(String username) {
        List<String> permissions = new ArrayList<>();
        /**
         * 只有是 admin 用户才有 新增、删除、编辑权限
         * 用户只有 遍历 权限
         */
        if(username.equals("admin")){
            permissions.add("user:delete");
            permissions.add("user:add");
            permissions.add("user:edit");
        }
        permissions.add("user:list");
        return permissions;
    }

    /**
     * 模拟通过数据库获取用户角色信息
     * @Author:     小冯
     * @UpdateUser:
     * @Version:     0.0.1
     * @param username
     * @return       java.util.List<java.lang.String>
     * @throws
     */
    private List<String> getRolesByUsername(String username) {
        List<String> roles = new ArrayList<>();
        if(username.equals("admin")){
            roles.add("admin");
        }
        roles.add("test");
        return roles;
    }
    /**
     * 通过用户名查询密码，模拟数据库查询
     * @Author:     小冯
     * @UpdateUser:
     * @Version:     0.0.1
     * @param username
     * @return       java.lang.String
     * @throws
     */
    private String getPasswordByUsername(String username) {
        return userMap.get(username);
    }


    /**
     * 获得密文密码
     * 加盐（用户名）
     *
     * @param currentPassword
     * @param username
     * @return
     */
    private String getPasswordMatcher(String currentPassword,String username){
        return new Md5Hash(currentPassword, username).toString();
    }

}
