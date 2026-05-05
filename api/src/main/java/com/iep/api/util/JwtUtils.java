package com.iep.api.util;

import com.iep.api.security.CustomUserInfoDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Component
public class JwtUtils {

    private final SecretKey secretKey;
    private final String issuer;
    private final String audience;
    private final JwtParser parser;
    private final long expirationSeconds;

    public JwtUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer:}") String issuer,
            @Value("${jwt.audience:}") String audience,
            @Value("${jwt.expiration-seconds:604800}") long expirationSeconds) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("jwt.secret must be configured");
        }

        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalStateException("jwt.secret must be at least 256 bits (32 bytes)");
        }

        this.secretKey = Keys.hmacShaKeyFor(secretBytes);
        this.issuer = issuer == null ? "" : issuer.trim();
        this.audience = audience == null ? "" : audience.trim();
        this.expirationSeconds = expirationSeconds;

        // 在 parser 層級直接設定 issuer / audience 驗證，無需手動檢查
        var parserBuilder = Jwts.parser().verifyWith(this.secretKey);
        if (!this.issuer.isEmpty()) {
            parserBuilder.requireIssuer(this.issuer);
        }
        if (!this.audience.isEmpty()) {
            parserBuilder.requireAudience(this.audience);
        }
        this.parser = parserBuilder.build();
    }

    /**
     * 生成 Token（使用用戶 ID 和用戶名，過期時間由設定檔控制）
     *
     * @param userId   用戶 ID
     * @param username 用戶名
     * @return JWT Token
     */
    public String generateToken(Long userId, String username) {
        var now = Instant.now();
        var expirationTime = Date.from(now.plusSeconds(expirationSeconds));

        Map<String, Object> claims = new HashMap<>();
        if (userId != null) {
            claims.put("userId", userId);
        }
        claims.put("username", username);

        return buildToken(claims, username, expirationTime);
    }

    /**
     * 從 Token 中取得 Claims
     * <p>
     * 會自動驗證簽名、過期時間、issuer 及 audience。
     * 驗證失敗時會拋出對應的 JwtException 子類別：
     * - ExpiredJwtException：Token 已過期
     * - MalformedJwtException：Token 格式錯誤
     * - SignatureException：簽名驗證失敗
     * - IncorrectClaimException：issuer 或 audience 不符
     *
     * @param token JWT Token
     * @return Claims
     */
    public Claims getClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    /**
     * 檢查 Token 是否有效（未過期且簽名正確）
     *
     * @param token JWT Token
     * @return true 表示有效
     */
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 從當前 SecurityContext 取得用戶 ID
     *
     * @return 當前用戶 ID，若無則為 empty
     */
    public static Optional<Long> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserInfoDetails userDetails) {
            if (userDetails.getUserInfo() != null) {
                return Optional.of(userDetails.getUserInfo().getId());
            }
        }
        return Optional.empty();
    }

    /**
     * 共用的 Token 建構邏輯
     */
    private String buildToken(Map<String, Object> claims, String subject, Date expirationTime) {
        var now = Instant.now();

        var builder = Jwts.builder()
                .claims(claims)
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .notBefore(Date.from(now))
                .expiration(expirationTime)
                .subject(subject);

        if (!issuer.isEmpty()) {
            builder.issuer(issuer);
        }
        if (!audience.isEmpty()) {
            builder.audience().add(audience).and();
        }

        return builder
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}