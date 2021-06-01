package cn.quguai.filter;

import cn.quguai.exception.VerificationCodeException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class VerificationCodeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if ("/login".equals(request.getRequestURI())) {
            verificationCode(request);
        }
        chain.doFilter(request, response);
    }


    private void verificationCode(HttpServletRequest request) throws VerificationCodeException {
        String captcha = request.getParameter("captcha");
        HttpSession session = request.getSession();
        String saveCode = (String) session.getAttribute("captcha");
        if (StringUtils.hasLength(saveCode)) {
            session.removeAttribute("captcha");
        }
        if (!StringUtils.hasLength(captcha) || !StringUtils.hasLength(saveCode) || !captcha.equals(saveCode)) {
            throw new VerificationCodeException();
        }
    }
}
