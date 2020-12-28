package com.feng.config;

import com.feng.shiro.ShiroAccessControlFilter;
import com.feng.shiro.ShiroHashedCredentialsMatcher;
import com.feng.shiro.ShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * token 的过滤
     * 自定义token 校验
     * 学习说明：其实 下面的 ShiroHashedCredentialsMatcher（自定义的） 也继承了 HashedCredentialsMatcher。
     * 需要在 CustomRealm bean 中进行设置
     * @return
     */
    @Bean(name = "shiroHashedCredentialsMatcher")
    public ShiroHashedCredentialsMatcher shiroHashedCredentialsMatcher() {
        return new ShiroHashedCredentialsMatcher();
    }

    /**
     * 登录的 认证域
     *
     * @param hashedCredentialsMatcher
     * @return
     */
    @Bean(name = "shiroRealm")
    public ShiroRealm getShiroRealm(@Qualifier("shiroHashedCredentialsMatcher") HashedCredentialsMatcher hashedCredentialsMatcher) {
        ShiroRealm shiroRealm = new ShiroRealm();
        // 自定义 处理 token 过滤
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return shiroRealm;
    }


    /**
     * shiro 的安全管理器
     *
     * @param shiroRealm
     * @return
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("shiroRealm") ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }
    /**
     * shiro 的过滤器
     * 需要了解 shiro 的权限关键字含义：
     *  anon，表示不拦截的路径
     *  authc,表示拦截的路径
     *
     *  匹配时，首先匹配 anon 的，然后最后匹配 authc
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        /*
         * 自定义过滤器
         * */
        //自定义拦截器限制并发人数,参考博客：
        LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
        //用来校验token
        filtersMap.put("token", new ShiroAccessControlFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
        /*
         * 以下为权限控制
         * */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/user/login", "anon");
        //放开 swagger-ui 地址
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        // 拦截所有
        filterChainDefinitionMap.put("/**", "token");
        /**
         * 默认 是使用  authc 来过滤的，这里是使用上面定义的token来过滤拦截的。
         */
//        filterChainDefinitionMap.put("/**", "token,authc");

        // 没有登录的用户请求需要登录的页面时自动跳转到登录页面。 配置 shiro 默认登录界面地址，
        shiroFilterFactoryBean.setLoginUrl("/api/user/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 下面两个配置类 AuthorizationAttributeSourceAdvisor 和 DefaultAdvisorAutoProxyCreator，开启 shiro aop 注解 支持.
     * 使用代理方式;所以需要开启代码支持;
     * <p>
     * 如果不加 使用 @RequirePermissions 无效
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }


}
