package cn.quguai.config;

import cn.quguai.config.MyWebAuthenticationDetails;
import cn.quguai.exception.VerificationCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    public MyAuthenticationProvider(UserDetailsService userDetailsService) {
        this.setPasswordEncoder(new BCryptPasswordEncoder());
        this.setUserDetailsService(userDetailsService);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 实现图像验证码的操作
        MyWebAuthenticationDetails details = (MyWebAuthenticationDetails)authentication.getDetails();
        if (!details.isImageCodeIsRight()) {
            throw new VerificationCodeException();
        }
        // 调用父类完成密码验证的操作
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
