package com.iep.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登入響應 DTO
 */
@Data
@AllArgsConstructor
public class LoginResp {
    /**
     * JWT Token
     */
    private String token;


    /**
     * 用戶 ID
     */
    private Long userId;

    /**
     * 用戶名
     */
    private String username;

    /**
     * 暱稱
     */
    private String nickname;
}