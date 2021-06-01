package cn.quguai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@EnableWebSecurity
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpringSessionBackedSessionRegistry sessionRegistry;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .formLogin().permitAll()
                .and().sessionManagement()
                // .invalidSessionUrl("/session/invalid") // Session 失效后的定向网址
//                .invalidSessionStrategy(new MyInvalidSessionStrategy()) // Session 无效的策略，实现InvalidSessionStrategy
                .maximumSessions(1)// 最大用户数量
                .maxSessionsPreventsLogin(true) // 组织新会话的连接：Session QQ就是False
                .sessionRegistry(sessionRegistry);
    }
}
