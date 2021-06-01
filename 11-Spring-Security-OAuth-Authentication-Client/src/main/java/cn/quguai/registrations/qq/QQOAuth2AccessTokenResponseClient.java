package cn.quguai.registrations.qq;

import cn.quguai.config.TextHtmlHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author LiYangSir
 * @date 2021/5/29
 */
public class QQOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private RestTemplate restTemplate;

    private RestTemplate getRestTemplate(){
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new TextHtmlHttpMessageConverter());
        }
        return restTemplate;
    }
    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
        OAuth2AuthorizationExchange authorizationExchange = authorizationCodeGrantRequest.getAuthorizationExchange();

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("client_id", clientRegistration.getClientId());
        params.set("client_secret", clientRegistration.getClientSecret());
        params.set("code", authorizationExchange.getAuthorizationResponse().getCode());
        params.set("redirect_uri", authorizationExchange.getAuthorizationRequest().getRedirectUri());
        params.set("grant_type", "authorization_code");
        String response = getRestTemplate().postForObject(clientRegistration.getProviderDetails().getTokenUri(), params, String.class);
        String[] items = response.split("&");
        String accessToken = items[0].substring(items[0].lastIndexOf("=") + 1);
        Long expiresIn = new Long(items[1].substring(items[1].lastIndexOf("=") + 1));
        Set<String> scopes = new LinkedHashSet<>(authorizationExchange.getAuthorizationRequest().getScopes());
        Map<String, Object> additionParameters = new LinkedHashMap<>();
        return OAuth2AccessTokenResponse
                .withToken(accessToken)
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(expiresIn)
                .scopes(scopes)
                .additionalParameters(additionParameters)
                .build();
    }

}
