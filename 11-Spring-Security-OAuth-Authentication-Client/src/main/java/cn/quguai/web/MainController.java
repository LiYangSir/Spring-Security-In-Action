package cn.quguai.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author LiYangSir
 * @date 2021/5/29
 */
@Controller
public class MainController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/")
    public String index(Model model, OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = this.getAuthorizedClient(authentication);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("clientName", client.getClientRegistration().getClientName());
        return "index";
    }

    @GetMapping("/login/oauth2")
    public String login(){
        return "login";
    }


    private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
        return this.authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
    }
}
