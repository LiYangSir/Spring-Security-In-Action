package cn.quguai.config;

import cn.quguai.registrations.CompositeOAuth2AccessTokenResponseClient;
import cn.quguai.registrations.CompositeOAuth2UserService;
import cn.quguai.registrations.qq.QQOAuth2AccessTokenResponseClient;
import cn.quguai.registrations.qq.QQOAuth2UserService;
import cn.quguai.registrations.qq.QQUserInfo;
import cn.quguai.registrations.weibo.SinaOAuth2AccessTokenResponseClient;
import cn.quguai.registrations.weibo.SinaOAuth2UserService;
import cn.quguai.registrations.weibo.SinaUserInfo;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * @author LiYangSir
 * @date 2021/5/29
 */

@EnableWebSecurity(debug = true)
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String QQ_REGISTRATION_ID = "qq";
    public static final String WECHAT_REGISTRATION_ID = "wechat";
    public static final String WEIBO_REGISTRATION_ID = "weibo";

    public static final String LOGIN_PAGE_PATH = "/login/oauth2";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(LOGIN_PAGE_PATH).permitAll()
                .anyRequest().authenticated();
        http.oauth2Login().tokenEndpoint().accessTokenResponseClient(this.accessTokenResponseClient())
                .and().userInfoEndpoint().customUserType(SinaUserInfo.class, WEIBO_REGISTRATION_ID)
                .userService(oauth2UserService());
        http.oauth2Login().loginPage(LOGIN_PAGE_PATH).loginProcessingUrl("/return/*");
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        CompositeOAuth2UserService userService = new CompositeOAuth2UserService();
        userService.getUserServiceMap().put(QQ_REGISTRATION_ID, new QQOAuth2UserService());
        userService.getUserServiceMap().put(WEIBO_REGISTRATION_ID, new SinaOAuth2UserService());
        return userService;
    }

    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        CompositeOAuth2AccessTokenResponseClient client = new CompositeOAuth2AccessTokenResponseClient();
        client.getClientMap().put(QQ_REGISTRATION_ID, new QQOAuth2AccessTokenResponseClient());
        client.getClientMap().put(WEIBO_REGISTRATION_ID, new SinaOAuth2AccessTokenResponseClient());
        return client;
    }
}
