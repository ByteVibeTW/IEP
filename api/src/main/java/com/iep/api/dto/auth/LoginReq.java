package com.iep.api.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.iep.api.dal.entity.user.UserInfo}
 */
@Value
public class LoginReq implements Serializable {
    @NotNull(message = "email 不能為空")
    String email;
    @NotNull(message = "密碼不能為空")
    String password;
}