package cn.quguai.config;

import cn.quguai.filter.VerificationCodeFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll()
                .and().authorizeRequests()
                .antMatchers("/app/api", "/captcha.jpg").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
        http.addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
