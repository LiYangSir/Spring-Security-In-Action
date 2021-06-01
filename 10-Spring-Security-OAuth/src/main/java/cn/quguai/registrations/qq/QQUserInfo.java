package cn.quguai.registrations.qq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LiYangSir
 * @date 2021/5/29
 */

@Data
public class QQUserInfo implements OAuth2User {

    private List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ROLE_USER");
    private Map<String, Object> attributes;

    private String nickname;

    @JsonProperty("figureurl")
    private String figureUrl30;

    @JsonProperty("figureurl_1")
    private String figureUrl50;

    @JsonProperty("figureurl_2")
    private String figureUrl100;

    private String gender;

    @JsonProperty("figureurl_qq_1")
    private String qqFigureUrl40;

    @JsonProperty("figureurl_qq_2")
    private String qqFigureUrl100;

    private String openId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorityList;
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
            this.attributes.put("nickname", this.getNickname());
            this.attributes.put("figureUrl30", this.getFigureUrl30());
            this.attributes.put("figureUrl50", this.getFigureUrl50());
            this.attributes.put("figureUrl100", this.getFigureUrl100());

            this.attributes.put("qqFigureUrl40", this.getQqFigureUrl40());
            this.attributes.put("qqFigureUrl100", this.getQqFigureUrl100());
            this.attributes.put("gender", this.getGender());
            this.attributes.put("openId", this.getOpenId());
        }
        return attributes;
    }

    @Override
    public String getName() {
        return this.nickname;
    }
}
