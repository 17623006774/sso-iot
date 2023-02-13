package com.it.authroiizationserver.config;

import com.it.authroiizationserver.service.Userdetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @date $ $
 * @description:
 * @Param:$
 */

@Configuration
@EnableAuthorizationServer
public class AuthorizationServiceConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Userdetails userdetails;

    @Autowired
    private JwtTokenStore jwtTokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置 授权管理器 和登录逻辑，   存储token，
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userdetails)

                .tokenStore(jwtTokenStore)
                .accessTokenConverter(jwtAccessTokenConverter);

    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {


        // 记住密码 ，  就是
        clients.inMemory()
                // 客户端ID
                .withClient("client")
                //密钥
                .secret(passwordEncoder.encode("lee"))
                // 配置多个客户端的登录uri
                .redirectUris("http://localhost:9999/login", "http://localhost:8082/login")
                .scopes("all")
                //授权类型
                .authorizedGrantTypes("authorization_code", "password", "refresh_token")
                .accessTokenValiditySeconds(60)
                //是否自动授权
                .autoApprove(false);

    }


    //单点登录必备  必须设置
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("isAuthenticated()");
    }
}
