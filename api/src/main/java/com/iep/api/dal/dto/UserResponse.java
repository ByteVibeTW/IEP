package com.iep.api.dal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String sub;
    private String email;
    private String username;
    private String roleCode;
}
