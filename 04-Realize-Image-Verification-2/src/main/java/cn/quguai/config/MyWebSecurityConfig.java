package cn.quguai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@EnableWebSecurity
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> myWebAuthenticationDetailsSource;

    @Resource
    private AuthenticationProvider authenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin().authenticationDetailsSource(myWebAuthenticationDetailsSource).loginPage("/login.html").loginProcessingUrl("/login").permitAll()
                .and().authorizeRequests()
                .antMatchers("/app/api", "/captcha.jpg").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
