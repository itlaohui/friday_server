package net.laohui.config;

import net.laohui.interceptor.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class shiroConfig {



//    @Bean
//    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
//        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
//
//        // logged in users with the 'admin' role
//        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");
//
//        // logged in users with the 'document:read' permission
////        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");
//
//        // all other paths require a logged in user
////        chainDefinition.addPathDefinition("/index", "authc");
//        Map<String, String> map = new LinkedHashMap<>();
//        map.put("/doLogin", "anon");
//        map.put("/index", "authc");
//        chainDefinition.addPathDefinitions(map);
//        return chainDefinition;
//    }

    /**
     * 注入 securityManager
     * @return
     */
    @Bean(name = "defaultWebSecurityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(MyRealm myRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 使用自定义Realm
        defaultWebSecurityManager.setRealm(myRealm);
        // 关闭Shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        // 设置自定义Cache缓存
        return defaultWebSecurityManager;
    }

    /**
     * 添加注解支持
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        Map<String, Filter> filters = new LinkedHashMap<>();
        Map<String, String> filterMap = new LinkedHashMap<>();
        bean.setSecurityManager(defaultWebSecurityManager);
        filters.put("JwtFilter", new JwtFilter());
//        bean.setLoginUrl("/login");
//        bean.setSuccessUrl("/index");
//        bean.setUnauthorizedUrl("/unauthorizedurl");
        bean.setFilters(filters);
//        filterMap.put("/admin/**", "JwtFilter");
//        filterMap.put("/user/login", "anon");
//        filterMap.put("/user/register", "anon");
//        filterMap.put("/user/forget", "anon");
//        filterMap.put("/user/logout", "anon");
//        filterMap.put("/user/**", "JwtFilter");
//        filterMap.put("/user/**", "JwtFilter");
        filterMap.put("/**", "JwtFilter");
//        map.put("/**", "authc");
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }
}