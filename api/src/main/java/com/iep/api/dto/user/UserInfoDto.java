package com.iep.api.dto.user;

import com.iep.api.dal.entity.user.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link UserInfo}
 */
@Schema(description = "使用者資訊 DTO")
@Data
public class UserInfoDto implements Serializable {
    @Schema(description = "使用者ID", example = "1")
    Long id;

    @Schema(description = "電子郵件", example = "user@gmail.com")
    String email;

    @Schema(description = "使用者名稱", example = "user")
    String username;

    @Schema(description = "密碼", example = "password")
    String password;

    @Schema(description = "角色代碼", example = "STUDENT")
    String roleCode;
}