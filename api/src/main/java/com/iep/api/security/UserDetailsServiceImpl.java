package com.iep.api.security;

import com.iep.api.dal.entity.user.UserInfo;
import com.iep.api.dal.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserInfoRepository userInfoRepository;

    public UserDetailsServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalized = username == null ? "" : username.trim();
        if (normalized.isEmpty()) {
            throw new UsernameNotFoundException("username is blank");
        }

        Optional<UserInfo> userInfo = userInfoRepository.findByUsername(normalized);
        return userInfo
                .map(CustomUserInfoDetails::new)
                .orElseThrow(() -> {
                    log.warn("Username not found: {}", normalized);
                    return new UsernameNotFoundException("user not found");
                });
    }

}