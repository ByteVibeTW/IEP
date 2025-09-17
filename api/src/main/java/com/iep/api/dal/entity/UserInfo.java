package com.iep.api.dal.entity;

import com.iep.api.constant.RoleConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role_code")
    private String roleCode;
    
    public boolean isAdmin() {
        return RoleConstants.ADMIN.equals(roleCode);
    }

    public boolean isTeacher() {
        return RoleConstants.TEACHER.equals(roleCode);
    }
    
    public boolean isStudent() {
        return RoleConstants.STUDENT.equals(roleCode);
    }
    

}