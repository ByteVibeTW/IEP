package com.iep.api.dal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @Column(name = "sub", nullable = false)
    private String sub;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "role_code")
    private String roleCode;

    
    public boolean isTeacher() {
        return !"ADMIN".equals(roleCode) && !"TEACHER".equals(roleCode);
    }
    
    public boolean isStudent() {
        return "TEACHER".equals(roleCode);
    }
}