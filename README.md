# springboot-shiro
shiro的学习，还有springboot整合shiro部分

# 项目架构
## 1. 测试学习
1. test包中是 关于 shiro的 8个由浅入深的demo测试类。
    包括shiro包下的 CustomRealmCh06、CustomRealmCh07、CustomRealmCh08 和 shiro.ini 文件

2. Ch01_authentication.java ：`shiro 用户认证`
3. Ch02_authorization.java  ：` shiro 用户授权 `
4. Ch03_testIniRealm.java + shiro.ini 文件：`使用 Ini文件 进行认证授权`
5. Ch04_testJdbcRealm.java  ： `使用 JdbcRealm 进行认证授权`
6. Ch05_testNewJdbcRealm.java ：`自定义SQL认证授权`
7. Ch06_testCustomRealm.java + CustomRealmCh06.java数据域 : `自定义 Realm 进行认证授权（important）`
8. Ch07_testMatcher.java + CustomRealmCh07.java数据域     : `shiro不加盐加密认证`
9. Ch08_testSaltMatcher.java + CustomRealmCh08.java数据域 : `shiro加盐  加密认证`
    
## 2. springboot整合shiro
1. swagger 做Restful风格的Api生成文档工具
    //@EnableSwagger2 ,配置类注解目前没有开启，开启之后，会报错。
    已解决：mybatis的逆向工程插件依赖放到了 plugin 下面。
2. redis 做 token 的存储，
3. shiro 做 token的过滤和 权限的授权
4. exception/BusinessException类做运行时异常的处理
5. PasswordUtils 密码工具类，为密码进行加密，

# 开发流程
1. 先开发 ShiroConfig，设置自定义过滤器 ShiroAccessControlFilter ，并设置过滤器的白名单
    对登录的请求设置为 anon。
2. 开发 ShiroAccessControlFilter 拦截器，这里是对 token 的简单过滤验证，并进行主体提交，提交到自定义数据域 realm。
    开发拦截器中，还要开发ShiroUsernamePasswordToken，自己实现shiro认证机制，就要重写类 UsernamePasswordToken
3. 开发自定义realm：ShiroRealm类。数据域部分，进行授权和认证。
    认证：对token进行过滤，用户名密码登录部分还是在业务层处理
    授权：从数据库中获取，设计到表 role、permission、user_role、role_permission 四个表
4. 开发 token 的最后过滤处理 ShiroHashedCredentialsMatcher，继承 HashedCredentialsMatcher 类实现方法 doCredentialsMatch()

以上四步其实就是 shiro 开发的全部，基本上都是配置式的代码。

# 接口请求逻辑
1. 登录：用户名、密码=》进入到 user/login接口，这个接口在 shiroConfig 中 的 filter 设置成白名单

2. 在UserServiceImpl 中的 login() 方法中进行登录。密码验证成功后返回，携带 token。
    至此，不涉及到shiro。

3. 接下来调用 /user/getuser 接口, 会携带 token ， 想要获取用户获取信息

4. 会被shiro的过滤器器 CustomAccessControllerFilter 拦截，进行简单验证 是否携带token，并 进行主体提交登录。

5. 进入到自定义数据域 realm 进行认证， 先进入到 doGetAuthenticationInfo() 进行用户认证，返回用户信息。 进入 AuthenticatingRealm 类中的  
    getAuthenticationInfo() 方法，先判断 缓存中是否有信息，没有则获取 doGetAuthenticationInfo()  中返回 用户信息。（这里也是自定义 配置缓存管理器的 关键之处）

6. 然后 进入到  CustomHashedCredentialsMatcher类   doCredentialsMatch()  中，进行对token 进一步 处理、判断。  （重要、这里是自定义的）
   一直下一步 会到 DefaultSecurityManager 类中的 login（）方法。
   然后回到 realm 中进入权限认证。

7. 这个接口 /user/getuser 方法上有注解 @RequiresPermissions("sys:user:detail"), 所以会进入到自定义数据域 realm 中的授权方法 doGetAuthenticationInfo()
   获取角色信息和权限进行，进行判断。
   
流程到这儿 shiro 的代码部分基本上就走完啦

