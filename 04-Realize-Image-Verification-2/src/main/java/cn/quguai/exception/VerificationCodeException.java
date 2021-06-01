package cn.quguai.exception;

import org.springframework.security.core.AuthenticationException;

public class VerificationCodeException extends AuthenticationException {
    public VerificationCodeException() {
        super("图形验证码错误");
    }
}
