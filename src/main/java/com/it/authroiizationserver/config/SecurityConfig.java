package com.it.authroiizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @date $ $
 * @description:
 * @Param:$
 */


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login/**", "/oauth/**", "/logout/**")
                .permitAll()
                .anyRequest()
                .authenticated()


                .and()
                .formLogin()
                .loginPage("/login")
                .successForwardUrl("/home")
                .permitAll()
                .and()
                // 多人登录，踢人
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false);

//        // 自定义开放url过滤器配置--无需鉴权
//        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
//
//        registry.anyRequest().authenticated().and()
//                .formLogin()
//                .loginPage("/login")
////                .defaultSuccessUrl("/home")
//                .permitAll()
//                .and()
//                .logout().permitAll()
//                .and()
//                .csrf().disable()
//                .httpBasic();

    }


    // 授权管理器
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/fonts/**","/img/**","/js/**");
    }
}
