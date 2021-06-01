package cn.quguai.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiYangSir
 * @date 2021/6/1
 */
@RestController
public class ResourceController {

    @GetMapping("/resource")
    public String phone(){
        return "resource";
    }
}
