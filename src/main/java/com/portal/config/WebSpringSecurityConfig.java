//package com.portal.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//import com.portal.service.CustomUserDetailsService;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)//开启security注解
//public class WebSpringSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//    	http
//	        //禁用CSRF保护
//	        .csrf().disable()
//            .authorizeRequests()
//            //.antMatchers("/", "/home").permitAll()
//            .anyRequest().fullyAuthenticated()
//            .and()
//        .formLogin()
//            .loginPage("/login")
//            .permitAll()
//            .and()
//        .logout()
//            .permitAll();
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////        auth
////            .inMemoryAuthentication()
////                .withUser("user").password("password").roles("USER");
//
//        auth
//            .userDetailsService(customUserDetailsService());
////            .passwordEncoder(passwordEncoder());
//    }
//    
//    /**
//
//     * 设置用户密码的加密方式为MD5加密
//
//     * @return
//
//     */
//
//    @Bean
//    public Md5PasswordEncoder passwordEncoder() {
//        return new Md5PasswordEncoder();
//    }
//
//
//    /**
//
//     * 自定义UserDetailsService，从数据库中读取用户信息
//
//     * @return
//
//     */
//
//    @Bean
//    public CustomUserDetailsService customUserDetailsService(){
//        return new CustomUserDetailsService();
//    }
//
//}