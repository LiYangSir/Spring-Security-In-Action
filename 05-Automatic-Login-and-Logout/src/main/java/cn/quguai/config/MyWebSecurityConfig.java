package cn.quguai.config;

import cn.quguai.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailService userDetailsService;

    @Override  // VWtrYnFhJTJGN3dDVnBxMGJ0TWMwJTJCQ1ElM0QlM0Q6JTJCWE5BRERveUtXZXgxcVJtMkJWVnJnJTNEJTNE
    protected void configure(HttpSecurity http) throws Exception {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(true);
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().rememberMe().userDetailsService(userDetailsService).tokenRepository(jdbcTokenRepository).tokenValiditySeconds(60)
                .and().csrf().disable();
        http.logout().logoutUrl("/myLogout").logoutSuccessUrl("/").logoutSuccessHandler((request, response, authentication) -> {
        }).invalidateHttpSession(true).deleteCookies("cookie").addLogoutHandler((request, response, authentication) -> {});
    }
}
