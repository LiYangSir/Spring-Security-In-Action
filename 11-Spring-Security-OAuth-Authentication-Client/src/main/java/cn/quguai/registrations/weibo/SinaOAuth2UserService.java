package cn.quguai.registrations.weibo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 通过 access_token 获取用户数据
 * @author LiYangSir
 * @date 2021/5/29
 */

public class SinaOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final String SINA_URL_GET_USER_INFO = "https://api.weibo.com/2/users/show.json?access_token={accessToken}&uid={uid}";

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public SinaUserInfo loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String uidUrl = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("access_token", accessToken);
        String result = restTemplate.postForObject(uidUrl, params, String.class);
        String uid = JSONObject.parseObject(result).getString("uid");
        return this.restTemplate.getForObject(SINA_URL_GET_USER_INFO, SinaUserInfo.class, accessToken, uid);
    }
}
