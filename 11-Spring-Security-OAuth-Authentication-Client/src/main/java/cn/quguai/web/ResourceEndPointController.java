package cn.quguai.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author LiYangSir
 * @date 2021/6/1
 */
@RestController
public class ResourceEndPointController {

    private static final String URL_GET_USER_PHONE = "http://oauth.quguai.cn:9999/phone";

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/phone")
    public String userInfo(OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authenticationToken.getAuthorizedClientRegistrationId(), authenticationToken.getName());
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + client.getAccessToken().getTokenValue());
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(null, header);
        ResponseEntity<String> res = restTemplate.exchange(URL_GET_USER_PHONE, HttpMethod.GET, stringHttpEntity, String.class);
        return res.getBody();
    }
}
