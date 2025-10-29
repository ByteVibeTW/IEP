package com.iep.api.controller.v1;

import com.iep.api.dal.dto.UserInfoDto;
import com.iep.api.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "使用者模組", description = "使用者模組相關API")
@RequiredArgsConstructor
public class UserInfoController {
    
    private final UserInfoService userInfoService;

    @GetMapping
    @Operation(summary = "取得所有使用者", description = "取得所有使用者的列表")
    public ResponseEntity<List<UserInfoDto>> getAllUsers() {
        List<UserInfoDto> users = userInfoService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{sub}")
    @Operation(summary = "依Sub取得使用者", description = "根據使用者Sub取得使用者詳細資訊")
    public ResponseEntity<UserInfoDto> getUserBySub(@PathVariable String sub) {
        Optional<UserInfoDto> user = userInfoService.getUserBySub(sub);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{sub}")
    @Operation(summary = "刪除使用者", description = "根據使用者Sub刪除使用者")
    public ResponseEntity<Void> deleteUser(@PathVariable String sub) {
        try {
            userInfoService.deleteUser(sub);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
