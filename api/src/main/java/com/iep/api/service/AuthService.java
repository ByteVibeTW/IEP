package com.iep.api.service;

import com.iep.api.dal.entity.user.UserInfo;
import com.iep.api.dal.repository.UserInfoRepository;
import com.iep.api.dto.auth.LoginReq;
import com.iep.api.dto.auth.LoginResp;
import com.iep.api.exception.CommonException;
import com.iep.api.exception.ErrorCode;
import com.iep.api.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * 本地帳號登入
     */
    public LoginResp login(LoginReq req) {
        // 查詢用戶
        UserInfo user = userInfoRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new CommonException(ErrorCode.INVALID_CREDENTIALS));

        // 驗證密碼
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new CommonException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 生成 JWT Token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        log.info("用戶登入成功: username={}, userId={}", user.getUsername(), user.getId());

        return new LoginResp(token, user.getId(), user.getUsername(), user.getNickname());
    }
}