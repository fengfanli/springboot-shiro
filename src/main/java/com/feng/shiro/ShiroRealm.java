package com.feng.shiro;

import com.feng.bean.SysUser;
import com.feng.service.PermissionService;
import com.feng.service.RoleService;
import com.feng.service.UserService;
import com.feng.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 设置支持令牌校验
     *
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ShiroUsernamePasswordToken;
    }

    /**
     * 授权
     * 主要业务：
     * 系统业务出现要验证用户的角色权限的时候，就会调用这个方法
     * 来获取该用户所拥有的角色/权限
     * 这个用户授权的方法我们可以缓存起来不用每次都调用这个方法。
     * 后续的课程我们会结合 redis 实现它
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("ShiroRealm.doGetAuthorizationInfo()");
        String token= (String) principalCollection.getPrimaryPrincipal();
        String userId= (String) redisUtil.get(token);
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();

        //返回该用户的 角色信息 给授权器
        List<String> roleNames = roleService.getRoleNamesByUserId(userId);
        if (null != roleNames && !roleNames.isEmpty()) {
            info.addRoles(roleNames);
        }
        //返回该用户的 权限信息 给授权器
        Set<String> permissionPerms = permissionService.getPermissionPermsByUserId(userId);
        if (permissionPerms != null) {
            info.addStringPermissions(permissionPerms);
        }
        return info;
    }

    /**
     * 认证
     * 主要业务：
     * 当业务代码调用 subject.login(customPasswordToken); 方法后
     * 就会自动调用这个方法 验证用户名/密码
     * 这里我们改造成 验证 token 是否有效 已经自定义了 shiro 验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("ShiroRealm.doGetAuthenticationInfo()");
        ShiroUsernamePasswordToken token = (ShiroUsernamePasswordToken) authenticationToken;
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo((String)token.getPrincipal(), (String)token.getCredentials(), ShiroRealm.class.getName());
        return info;
    }

    private List<String> getRoleByUserId(String userId){
        List<String> roles=new ArrayList<>();
        if(userId.equals("8a938151-53e6-4182-925a-684f3be840e8")){
            roles.add("admin");
        }
        roles.add("test");
        return roles;
    }

    private List<String> getPermissionsByUserId(String userId){
        List<String> permissions=new ArrayList<>();
        if(userId.equals("8a938151-53e6-4182-925a-684f3be840e8")){
            permissions.add("*");
        }
        permissions.add("sys:user:detail");
        permissions.add("sys:user:edit");
        return permissions;
    }
}
