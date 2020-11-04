package com.nood.auth;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;


@Configuration
public class ShiroConfiguration {

    @Bean("credentialMatcher")
    public CredentialMatcher credentialsMatcher() {
        return new CredentialMatcher();
    }

    @Bean("authRealm")
    public AuthRealm authRealm(@Qualifier("credentialMatcher") CredentialMatcher credentialMatcher) {
        AuthRealm authRealm = new AuthRealm();
//        authRealm.setCacheManager(new MemoryConstrainedCacheManager());
        authRealm.setCredentialsMatcher(credentialMatcher);
        return authRealm;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        return manager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        filterFactory.setSecurityManager(securityManager);

        filterFactory.setLoginUrl("/login");
        filterFactory.setSuccessUrl("/index");
        filterFactory.setUnauthorizedUrl("/unauthorized");

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//         DefaultFilter enum
        filterChainDefinitionMap.put("/index", "authc");        // 需要在做身份认证，username,password
        filterChainDefinitionMap.put("/login", "anon");         // 不需要认证
        filterChainDefinitionMap.put("/loginUser", "anon");     // 不需要认证
        filterChainDefinitionMap.put("/admin", "roles[admin]"); // 只允许角色为admin的用户访问 /admin
        filterChainDefinitionMap.put("/edit", "perms[edit]");   // 只允许permissions有edit的访问 /edit
        filterChainDefinitionMap.put("/druid/*", "anon");       // 放行druid所有的数据监控
        filterChainDefinitionMap.put("/**", "user");

        filterFactory.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return filterFactory;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}
