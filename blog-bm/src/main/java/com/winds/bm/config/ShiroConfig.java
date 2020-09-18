package com.winds.bm.config;

import com.winds.bm.common.cache.RedisManager;
import com.winds.bm.oauth.AuthRealm;
import com.winds.bm.oauth.OAuthSessionDAO;
import com.winds.bm.oauth.OAuthSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wangl on 2017/11/22.
 * todo:
 */
@Configuration
public class ShiroConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        Map<String, Filter> map = Maps.newHashMap();
//        map.put("authc",new CaptchaFormAuthenticationFilter());
//        shiroFilterFactoryBean.setFilters(map);
//        设置 securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //配置访问权限
        // 登录的 url
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后跳转的 url
        shiroFilterFactoryBean.setSuccessUrl("/");
        // 未授权 url        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        /**
         *      保证顺序使用LinkedHashMap
         *      过滤链定义，从上向下顺序执行，一般将/**放在最为下边
         *      进行身份认证后才能访问
         * authc:   所有url都必须认证通过才可以访问;
         * anon:    所有url都都可以匿名访问
         * user:    如果使用rememberMe的功能可以直接访问
         * perms:   该资源必须授权
         * role：    该资源必须得到角色权限才可以访问
         */
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 设置免认证 url
        String[] anonUrls =  StringUtils.splitByWholeSeparatorPreserveAllTokens("/login/**,/genCaptcha", ",");
        for (String url : anonUrls) {
            filterChainDefinitionMap.put(url, "anon");
        }

        // 配置退出过滤器，其中具体的退出代码 Shiro已经替我们实现了也可以自己设置
        filterChainDefinitionMap.put("/logout", "logout");
        // 除上以外所有 url都必须认证通过才可以访问，未通过认证自动访问 LoginUrl
        filterChainDefinitionMap.put("/**", "user");


        /*
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        filterChainDefinitionMap.put("/", "anon");

        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/blogFile/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/register", "anon");
        filterChainDefinitionMap.put("/genCaptcha", "anon");
        filterChainDefinitionMap.put("/login/main", "anon");
        //配置记住我过滤器或认证通过可以访问的地址(当上次登录时，记住我以后，在下次访问/或/index时，可以直接访问，不需要登陆)
        filterChainDefinitionMap.put("/home/**", "user");

        filterChainDefinitionMap.put("/**", "authc");
  */
        //filterChainDefinitionMap.put("/**/create", "authc");
        //filterChainDefinitionMap.put("/**/update", "authc");
        //filterChainDefinitionMap.put("/**/delete", "authc");
        //filterChainDefinitionMap.put("/upload", "authc");
        //filterChainDefinitionMap.put("/users/currentUser", "authc");




        //开发阶段都不拦截
//        filterChainDefinitionMap.put("/**", "anon");

        //返回json数据，由前端跳转
        shiroFilterFactoryBean.setLoginUrl("/handleLogin");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    /**
     * 1.自定义认证
     * @return
     */
    @Bean
    public AuthRealm oAuthRealm() {
        AuthRealm myShiroRealm = new AuthRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }


    @Bean
    public SecurityManager securityManager(AuthRealm oAuthRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuthRealm);
        securityManager.setSessionManager(sessionManager);
        // 自定义缓存实现 使用redis
        //securityManager.setCacheManager(cacheManager());
        //注入Cookie(记住我)管理器(remenberMeManager)
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * 2.自定义sessionManager，用户的唯一标识，即Token或Authorization的认证
     * @param authSessionDAO
     * @return
     */
    @Bean
    public SessionManager sessionManager(OAuthSessionDAO authSessionDAO) {
        OAuthSessionManager oAuthSessionManager = new OAuthSessionManager();
        oAuthSessionManager.setSessionDAO(authSessionDAO);
        return oAuthSessionManager;
    }


    @Bean
    public OAuthSessionDAO authSessionDAO(RedisManager redisManager) {
        OAuthSessionDAO authSessionDAO = new OAuthSessionDAO();
        authSessionDAO.setRedisManager(redisManager);
        return authSessionDAO;
    }

    /**
     * 3.此处对应前端“记住我”的功能，获取用户关联信息而无需登录
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
        simpleCookie.setHttpOnly(true);
        //记住我cookie生效时间,默认30天 ,单位秒：60 * 60 * 24 * 30
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }
    @Bean
    public CookieRememberMeManager rememberMeManager(){

        LOGGER.info("注入Shiro的记住我(CookieRememberMeManager)管理器-->rememberMeManager", CookieRememberMeManager.class);

        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("one"));
        return cookieRememberMeManager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
