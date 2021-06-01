package cn.quguai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@EnableRedisHttpSession
public class HttpSessionConfig {

    @Autowired
    private RedisIndexedSessionRepository sessionRepository;

    @Bean
    public SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry(){
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

}
