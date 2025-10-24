package com.iep.api.util;

import com.iep.api.dal.entity.UserInfo;
import com.iep.api.dal.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 安全工具類
 * 提供獲取當前認證用戶的方法
 */
@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserInfoRepository userInfoRepository;

    /**
     * 獲取當前認證用戶的 sub
     */
    public Optional<String> getCurrentUserSub() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            String sub = jwt.getClaimAsString("sub");
            return Optional.ofNullable(sub);
        }
        
        return Optional.empty();
    }

    /**
     * 獲取當前認證用戶的 UserInfo
     */
    public Optional<UserInfo> getCurrentUser() {
        return getCurrentUserSub()
                .flatMap(userInfoRepository::findById);
    }

    /**
     * 獲取當前用戶或拋出異常
     */
    public UserInfo getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * 檢查當前用戶是否已登入
     */
    public boolean isAuthenticated() {
        return getCurrentUserSub().isPresent();
    }
}

