package cn.quguai.registrations.weibo;

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
 * @date 2021/5/30
 */

@Data
public class SinaUserInfo implements OAuth2User {
    private List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ROLE_USER");
    private Map<String, Object> attributes;

    private String name;

    private String location;

    private String description;

    private String url;

    private String gender;

    @JsonProperty("followers-count")
    private String followersCount;

    @JsonProperty("favourites-count")
    private String favouritesCount;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorityList;
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
            this.attributes.put("name", this.getName());
            this.attributes.put("location", this.getLocation());
            this.attributes.put("description", this.getDescription());
            this.attributes.put("url", this.getUrl());

            this.attributes.put("followersCount", this.getFollowersCount());
            this.attributes.put("favouritesCount", this.getFavouritesCount());
            this.attributes.put("gender", this.getGender());
        }
        return attributes;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
