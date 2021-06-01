package cn.quguai.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    /* 这样就无需在配置文件中配置hasRole等相关配置 .antMatchers("/admin/api/**").hasRole("ADMIN").antMatchers("/user/api/**").hasRole("USER")
     * @PreAuthorize("hasRole('USER')")  在方法之前进行验证
     * @PostAuthorize("hasAuthority('USER')")  在方法以后进行验证
     * @Secured({"ROLE_USER"})
     */

    @GetMapping("api")
    public String hello(){
        return "user Hello";
    }
}
