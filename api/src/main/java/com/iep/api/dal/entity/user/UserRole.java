package com.iep.api.dal.entity.user;

import lombok.Getter;

/**
 * 用戶角色枚舉
 * 系統角色定義
 */
@Getter
public enum UserRole {
    /**
     * 系統管理員
     */
    ADMIN("管理員"),
    TEACHER("老師"),
    STUDENT("學生");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }
}