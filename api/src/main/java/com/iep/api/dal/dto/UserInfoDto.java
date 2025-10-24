package com.iep.api.dal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.iep.api.dal.entity.UserInfo}
 */
@Schema(description = "使用者資訊 DTO")
@Data
public class UserInfoDto implements Serializable {
    @Schema(description = "使用者Sub", example = "7349e783-e2d8-4787-90a0-e44e4240ae44")
    String sub;
    @Schema(description = "電子郵件", example = "user@gmail.com")
    String email;
    @Schema(description = "使用者名稱", example = "user")
    String username;
    @Schema(description = "角色代碼", example = "STUDENT")
    String roleCode;
}