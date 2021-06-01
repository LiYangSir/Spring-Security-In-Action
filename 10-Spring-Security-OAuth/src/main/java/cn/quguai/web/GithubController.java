package cn.quguai.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author LiYangSir
 * @date 2021/5/29
 */

@RestController
public class GithubController {

    @GetMapping("/hello")
    public String hello(Principal principal) {
        return "Hello " + principal.getName();
    }
}

