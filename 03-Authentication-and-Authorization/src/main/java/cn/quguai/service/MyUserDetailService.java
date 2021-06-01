package cn.quguai.service;

import cn.quguai.dao.UserRepository;
import cn.quguai.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setAuthorityList(AuthorityUtils.commaSeparatedStringToAuthorityList(userEntity.getRoles()));
        return userEntity;
    }
}
