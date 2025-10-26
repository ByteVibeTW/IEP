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
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        
        getJwtFromContext()
            .flatMap(this::extractSub)
            .filter(sub -> !userInfoRepository.existsById(sub))
            .ifPresent(this::createUserFromJwt);
        
        filterChain.doFilter(request, response);
    }

    private Optional<Jwt> getJwtFromContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth instanceof JwtAuthenticationToken jwt ? Optional.of(jwt.getToken()) : Optional.empty();
    }

    private Optional<String> extractSub(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsString("sub"));
    }

    private void createUserFromJwt(String sub) {
        getJwtFromContext().ifPresent(jwt -> {
            String email = jwt.getClaimAsString("email");
            if (email == null) {
                log.warn("JWT token 缺少必要欄位: sub={}, email=null", sub);
                return;
            }

            UserInfo userInfo = new UserInfo();
            userInfo.setSub(sub);
            userInfo.setEmail(email);
            userInfo.setUsername(Optional.ofNullable(jwt.getClaimAsString("preferred_username")).orElse(email));
            userInfo.setRoleCode(extractRoleCode(jwt));

            try {
                userInfoRepository.save(userInfo);
                log.info("自動創建用戶記錄: sub={}, email={}", sub, email);
            } catch (Exception e) {
                log.error("創建用戶記錄失敗: sub={}", sub, e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private String extractRoleCode(Jwt jwt) {
        return extractRolesFromRealmAccess(jwt)
            .or(() -> extractRolesFromResourceAccess(jwt))
            .map(this::mapToRoleCode)
            .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private Optional<String> extractRolesFromRealmAccess(Jwt jwt) {
        return Optional.ofNullable(jwt.<Map<String, Object>>getClaim("realm_access"))
            .map(realm -> (List<String>) realm.get("roles"))
            .filter(roles -> !roles.isEmpty())
            .map(roles -> roles.get(0));
    }

    @SuppressWarnings("unchecked")
    private Optional<String> extractRolesFromResourceAccess(Jwt jwt) {
        return Optional.ofNullable(jwt.<Map<String, Object>>getClaim("resource_access"))
            .flatMap(resources -> resources.values().stream()
                .filter(Map.class::isInstance)
                .map(resource -> (Map<String, Object>) resource)
                .map(resourceMap -> (List<String>) resourceMap.get("roles"))
                .filter(roles -> roles != null && !roles.isEmpty())
                .map(roles -> roles.get(0))
                .findFirst());
    }

    private String mapToRoleCode(String keycloakRole) {
        if (keycloakRole == null) return null;
        
        String roleLower = keycloakRole.toLowerCase();
        if (roleLower.contains("admin")) return "ADMIN";
        if (roleLower.contains("teacher")) return "TEACHER";
        if (roleLower.contains("student")) return "STUDENT";
        
        return null;
    }
}
