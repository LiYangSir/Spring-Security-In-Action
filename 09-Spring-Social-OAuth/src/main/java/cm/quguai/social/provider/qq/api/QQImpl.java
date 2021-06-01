package cm.quguai.social.provider.qq.api;

import cm.quguai.social.provider.qq.config.JacksonFromTextHtmlHttpMessageConverter;
import cm.quguai.social.provider.qq.connect.QQUserInfo;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.web.client.RestTemplate;

public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    private static final String QQ_URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token={accessToken}";

    private static final String QQ_URL_USER_INFO = "https://graph.qq.com/user/get_user/info?oauth_consumer_key={appId}&openid={openId}";

    private String appId;

    private String openId;

    public QQImpl(String appId, String accessToken) {
        this.appId = appId;
        String result = getRestTemplate().getForObject(QQ_URL_GET_OPENID, String.class, accessToken);
        this.openId = result.substring(result.lastIndexOf(":\"") + 2, result.indexOf("\"}"));
    }

    @Override
    public QQUserInfo getUserInfo() {
        QQUserInfo qqUserInfo = getRestTemplate().getForObject(QQ_URL_USER_INFO, QQUserInfo.class, appId, openId);
        if (qqUserInfo != null) {
            qqUserInfo.setOpenId(openId);
        }
        return qqUserInfo;
    }

    @Override
    protected void configureRestTemplate(RestTemplate restTemplate) {
        restTemplate.getMessageConverters().add(new JacksonFromTextHtmlHttpMessageConverter());
    }
}
