package cn.quguai.registrations.qq;

import cn.quguai.config.JacksonFromTextHtmlHttpMessageConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;

/**
 * @author LiYangSir
 * @date 2021/5/29
 */
public class QQOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final String QQ_URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key={appId}&openid={openId}&access_token={accessToken}";

    private RestTemplate restTemplate;

    private RestTemplate getRestTemplate(){
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new JacksonFromTextHtmlHttpMessageConverter());
        }
        return restTemplate;
    }
    @Override
    public QQUserInfo loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String openIdUrl = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri() + "?access_token={accessToken}";
        String result = getRestTemplate().getForObject(openIdUrl, String.class, accessToken);
        String openId = result.substring(result.lastIndexOf(":\"") + 2);
        String appId = userRequest.getClientRegistration().getClientId();
        QQUserInfo qqUserInfo = getRestTemplate().getForObject(QQ_URL_GET_USER_INFO, QQUserInfo.class, appId, openId, accessToken);
        if (qqUserInfo != null) {
            qqUserInfo.setOpenId(openId);
        }
        return qqUserInfo;
    }
}
