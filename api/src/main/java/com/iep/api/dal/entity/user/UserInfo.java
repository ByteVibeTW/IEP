package com.iep.api.dal.entity.user;

import com.iep.api.dal.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_info")
public class UserInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 使用者名稱
     */
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    /**
     * 密碼
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Email
     */
    @Column(name = "email")
    private String email;

    /**
     * 暱稱
     */
    @Column(name = "nickname", length = 100)
    private String nickname;

    /**
     * 頭像 URL
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;
    /**
     * 用戶角色
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role = UserRole.STUDENT;
}