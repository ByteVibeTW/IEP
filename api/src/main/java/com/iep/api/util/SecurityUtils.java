package com.iep.api.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 安全工具類
 * 用於從 SecurityContext 中獲取當前用戶信息
 */
@Component
@RequiredArgsConstructor
public class SecurityUtils {

    /**
     * 從 SecurityContext 中獲取當前用戶的 JWT token
     * 
     * @return 當前用戶的 JWT token
     */
    public static Optional<Jwt> getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return Optional.of(jwtAuthenticationToken.getToken());
        }
        
        return Optional.empty();
    }

    /**
     * 從 SecurityContext 中獲取當前用戶的 sub（subject）
     * 
     * @return 當前用戶的 sub
     */
    public static Optional<String> getCurrentUserSub() {
        return getCurrentJwt()
                .map(jwt -> jwt.getClaimAsString("sub"));
    }

    /**
     * 獲取當前用戶的 sub，如果不存在則拋出異常
     * 
     * @return 當前用戶的 sub
     * @throws RuntimeException 如果無法獲取當前用戶信息
     */
    public static String getCurrentUserSubOrThrow() {
        return getCurrentUserSub()
                .orElseThrow(() -> new RuntimeException("無法獲取當前用戶信息"));
    }
}

