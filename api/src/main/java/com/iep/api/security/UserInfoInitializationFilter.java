package com.iep.api.security;

import com.iep.api.dal.entity.UserInfo;
import com.iep.api.dal.repository.UserInfoRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * 用戶資訊初始化過濾器
 * 用於在每次請求時檢查並自動創建用戶記錄（如果不存在）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoInitializationFilter extends OncePerRequestFilter {

    private final UserInfoRepository userInfoRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 如果有已認證的用戶（JWT token）
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            String sub = jwt.getClaimAsString("sub");
            
            if (sub != null) {
                // 檢查用戶是否存在
                if (!userInfoRepository.existsById(sub)) {
                    // 從 JWT token 中提取用戶資訊並創建記錄
                    UserInfo userInfo = extractUserInfoFromJwt(jwt);
                    if (userInfo != null) {
                        try {
                            userInfoRepository.save(userInfo);
                            log.info("自動創建用戶記錄: sub={}, email={}", sub, userInfo.getEmail());
                        } catch (Exception e) {
                            log.error("創建用戶記錄失敗: sub={}", sub, e);
                        }
                    }
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * 從 JWT token 中提取用戶資訊
     */
    private UserInfo extractUserInfoFromJwt(Jwt jwt) {
        String sub = jwt.getClaimAsString("sub");
        String email = jwt.getClaimAsString("email");
        String preferredUsername = jwt.getClaimAsString("preferred_username");
        
        // 如果必要欄位缺失，返回 null
        if (sub == null || email == null) {
            log.warn("JWT token 缺少必要欄位: sub={}, email={}", sub, email);
            return null;
        }
        
        // 提取角色資訊
        String roleCode = extractRoleCode(jwt);
        
        UserInfo userInfo = new UserInfo();
        userInfo.setSub(sub);
        userInfo.setEmail(email);
        userInfo.setUsername(preferredUsername != null ? preferredUsername : email);
        userInfo.setRoleCode(roleCode);
        
        return userInfo;
    }

    /**
     * 從 JWT token 中提取角色代碼
     * 優先從 realm_access.roles 中提取
     */
    private String extractRoleCode(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null) {
            @SuppressWarnings("unchecked")
            java.util.List<String> roles = (java.util.List<String>) realmAccess.get("roles");
            if (roles != null && !roles.isEmpty()) {
                // 假設第一個角色是主要角色
                String role = roles.get(0);
                // 映射 Keycloak 角色到系統角色代碼
                return mapKeycloakRoleToRoleCode(role);
            }
        }
        
        // 如果沒有 realm_access，嘗試從 resource_access 中提取
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            // 您需要根據實際的 resource-id 調整
            for (Object resource : resourceAccess.values()) {
                if (resource instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> resourceMap = (Map<String, Object>) resource;
                    @SuppressWarnings("unchecked")
                    java.util.List<String> roles = (java.util.List<String>) resourceMap.get("roles");
                    if (roles != null && !roles.isEmpty()) {
                        String role = roles.get(0);
                        return mapKeycloakRoleToRoleCode(role);
                    }
                }
            }
        }
        
        return null; // 如果找不到角色，返回 null
    }

    /**
     * 將 Keycloak 角色映射到系統角色代碼
     */
    private String mapKeycloakRoleToRoleCode(String keycloakRole) {
        if (keycloakRole == null) {
            return null;
        }
        
        String roleLower = keycloakRole.toLowerCase();
        
        // 根據您的 Keycloak 角色命名規則調整
        if (roleLower.contains("teacher")) {
            return "TEACHER";
        } else if (roleLower.contains("student")) {
            return "STUDENT";
        } else if (roleLower.contains("admin")) {
            return "ADMIN";
        }
        
        // 預設返回 null，讓資料庫保持 null
        return null;
    }
}

