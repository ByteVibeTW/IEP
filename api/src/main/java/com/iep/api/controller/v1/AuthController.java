package com.iep.api.controller.v1;

import com.iep.api.dto.auth.LoginReq;
import com.iep.api.dto.auth.LoginResp;
import com.iep.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(path = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "認證模組", description = "用戶登入 API")
public class AuthController {
    private final AuthService authService;

    /**
     * 本地帳號登入
     */
    @PostMapping("/login")
    @Operation(summary = "登入", description = "使用用戶名和密碼登入")
    public ResponseEntity<LoginResp> login(@Valid @RequestBody LoginReq req) {
        LoginResp response = authService.login(req);
        return ResponseEntity.ok(response);
    }
}