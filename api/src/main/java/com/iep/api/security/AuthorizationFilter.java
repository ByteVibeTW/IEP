package com.iep.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iep.api.dto.common.ErrorResp;
import com.iep.api.exception.ErrorCode;
import com.iep.api.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 白名單路徑，不需要進行授權驗證
    private static final Set<String> WHITE_LIST = Set.of(
            "/api/v1/health",
            "/api/v1/auth/login"
    );

    public AuthorizationFilter(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // 白名單路徑直接放行
        if (WHITE_LIST.contains(path)) {
            return true;
        }
        // 非 /api/v1 開頭的路徑也不進行過濾
        return !path.startsWith("/api/v1");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            // 檢查 Authorization 標頭
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Authorization header is missing or invalid for path: {}", request.getServletPath());
                handleException(response, ErrorCode.UNAUTHORIZED);
                return;
            }

            // 提取並驗證 JWT
            String token = authHeader.substring(7); // "Bearer ".length() = 7
            Claims claims = jwtUtils.getClaims(token);
            String username = claims.get("username", String.class);

            if (username == null || username.isEmpty()) {
                log.warn("Username not found in token");
                handleException(response, ErrorCode.UNAUTHORIZED);
                return;
            }

            // 載入使用者詳細資訊並設置認證
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 繼續 filter chain
            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
            handleException(response, ErrorCode.TOKEN_EXPIRED);
        } catch (io.jsonwebtoken.JwtException e) {
            log.error("Invalid token: {}", e.getMessage());
            handleException(response, ErrorCode.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error during authorization: {}", e.getMessage(), e);
            handleException(response, ErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * 處理異常並返回 JSON 格式的錯誤響應
     */
    private void handleException(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResp errorResp = new ErrorResp();
        errorResp.setErrorCode(errorCode);
        errorResp.setMessage(errorCode.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(errorResp));
    }
}