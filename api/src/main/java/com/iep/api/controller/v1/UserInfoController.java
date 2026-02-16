package com.iep.api.controller.v1;

import com.iep.api.dto.user.UserInfoDto;
import com.iep.api.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/{id}")
    @Operation(summary = "依 id 取得使用者", description = "根據使用者 id 取得使用者詳細資訊")
    public ResponseEntity<UserInfoDto> getUserById(@PathVariable Long id) {
        UserInfoDto response = userInfoService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "刪除使用者", description = "根據使用者 id 刪除使用者")
    public ResponseEntity<Void> deleteUser(@RequestBody List<Long> ids) {
        userInfoService.deleteBatch(ids);
        return ResponseEntity.noContent().build();
    }
}
