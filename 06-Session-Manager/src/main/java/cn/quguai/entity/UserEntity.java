package cn.quguai.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "t_user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private Boolean enable;
    private String roles;

    @Transient
    private List<GrantedAuthority> authorityList;

    public void setAuthorityList(List<GrantedAuthority> authorityList) {
        this.authorityList = authorityList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorityList;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UserEntity && this.username.equals(((UserEntity) o).username);
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
}
