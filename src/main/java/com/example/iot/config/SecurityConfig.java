//package com.example.iot.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String pass = passwordEncoder.encode("root");
//        auth.inMemoryAuthentication().withUser("root").password(pass).roles("admin");
//    }
//
//
//    // 自定义登录页面
//    protected void configure(HttpSecurity http) throws Exception {
//        http.formLogin()   //自定义登陆页面
////                .loginPage("/login.html") //登录页面设置
////                .loginProcessingUrl("/user/login")     //登陆访问路径  这里同 action
////                .defaultSuccessUrl("/index.html").permitAll()   //登陆成功之后，跳转路径
////                .and().authorizeRequests()
////                .antMatchers( "/login.html", "/user/login").permitAll()  //设置哪些路径可以直接访问，不需要认证
////                .anyRequest().authenticated()   //所有请求都可以访问
//                .and().csrf().disable();  //关闭csrf
//    }
//
//    @Bean
//    PasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}