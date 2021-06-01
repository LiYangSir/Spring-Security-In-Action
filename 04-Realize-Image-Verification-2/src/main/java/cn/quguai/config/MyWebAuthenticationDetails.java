package cn.quguai.config;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author LiYangSir
 */
public class MyWebAuthenticationDetails extends WebAuthenticationDetails {
    private boolean imageCodeIsRight;

    public MyWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        String captcha = request.getParameter("captcha");
        HttpSession session = request.getSession();
        String saveCode = (String) session.getAttribute("captcha");
        if (StringUtils.hasLength(saveCode)) {
            session.removeAttribute("captcha");
        }
        if (StringUtils.hasLength(captcha) && StringUtils.hasLength(saveCode) && captcha.equals(saveCode)) {
            this.imageCodeIsRight = true;
        }
    }

    public boolean isImageCodeIsRight() {
        return imageCodeIsRight;
    }

}
