package cn.quguai.registrations;

import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiYangSir
 * @date 2021/5/29
 */
public class CompositeOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private Map<String, OAuth2AccessTokenResponseClient> clientMap;

    private static final String DEFAULT_CLIENT_KEY = "default_key";

    public CompositeOAuth2AccessTokenResponseClient() {
        this.clientMap = new HashMap<>();
        this.clientMap.put(DEFAULT_CLIENT_KEY, new NimbusAuthorizationCodeTokenResponseClient());
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
        OAuth2AccessTokenResponseClient client = clientMap.get(clientRegistration.getRegistrationId());
        if (client == null) {
            client = clientMap.get(DEFAULT_CLIENT_KEY);
        }
        return client.getTokenResponse(authorizationCodeGrantRequest);
    }

    public Map<String, OAuth2AccessTokenResponseClient> getClientMap() {
        return clientMap;
    }
}
