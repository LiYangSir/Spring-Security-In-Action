package cn.quguai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    /*
    核心就是比较两个token值是否相同
    1. HttpSessionCsrfTokenRepository
        比较的是请求过来的csrf的值 和 Session当中存储的值
    2. CookieCsrfTokenRepository
        比较的是请求带来的值（手动进行填写）和 Cookie当中存储的值
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().permitAll()
                .and().authorizeRequests()
                .anyRequest().authenticated();
        // 另外一种方式，不需要使用Session的
                // .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
