package cn.quguai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * @author LiYangSir
 * @date 2021/6/1
 */

@Profile("simple")
@EnableAuthorizationServer
@SpringBootApplication
public class AuthenticationApplication2 {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication2.class, args);
    }
}
