package cn.quguai.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author LiYangSir
 * @date 2021/6/1
 */
@Profile("simple")
@RestController
@Slf4j
public class ResourceController {

    @GetMapping("/me")
    public Principal me(Principal principal) {
        log.debug(principal.toString());
        return principal;
    }

    @GetMapping("/phone")
    public String phone(){
        return "phone:1234567890";
    }
}
