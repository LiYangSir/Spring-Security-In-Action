package cn.quguai.registrations;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiYangSir
 * @date 2021/5/29
 */
public class CompositeOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private Map<String, OAuth2UserService> userServiceMap;

    private static final String DEFAULT_CLIENT_KEY = "default_key";
    public CompositeOAuth2UserService() {
        this.userServiceMap = new HashMap<>();
        this.userServiceMap.put(DEFAULT_CLIENT_KEY, new DefaultOAuth2UserService());
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService userService = userServiceMap.get(clientRegistration.getRegistrationId());
        if (userService == null) {
            userService = userServiceMap.get(DEFAULT_CLIENT_KEY);
        }
        return userService.loadUser(userRequest);
    }

    public Map<String, OAuth2UserService> getUserServiceMap() {
        return userServiceMap;
    }
}
